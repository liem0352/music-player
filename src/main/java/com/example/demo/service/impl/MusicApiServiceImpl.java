package com.example.demo.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.service.IMusicApiService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 音乐API服务实现类
 * 集成UnblockNeteaseMusic多源音源
 * 使用Hutool进行HTTP请求和JSON解析
 */
@Service("musicApiService")
public class MusicApiServiceImpl implements IMusicApiService {

    // UnblockNeteaseMusic 公共API实例列表
    private static final String[] UNM_APIS = {
        "https://api.injahow.cn",           // ToolKal API
        "https://neteasecloudmusicapi.vercel.app",  // Vercel增强版
        "https://unblockneteasemusic-utils.vercel.app"  // UNM Utils
    };

    /**
     * 获取歌曲播放URL（多源匹配）
     * @param neteaseId 网易云歌曲ID
     * @return 音频URL
     */
    @Override
    public String getAudioUrl(String neteaseId) {
        System.out.println("\n🎵 [音乐服务] 正在获取歌曲URL: " + neteaseId);

        // 方案1：尝试UnblockNeteaseMusic API
        for (int i = 0; i < UNM_APIS.length; i++) {
            String api = UNM_APIS[i];
            try {
                System.out.println("🔄 [" + (i + 1) + "/" + UNM_APIS.length + "] 尝试: " + api);
                
                String url = tryUnmApi(api, neteaseId);
                if (url != null && !url.isEmpty()) {
                    System.out.println("✅ [" + api + "] 成功获取URL");
                    return url;
                }
            } catch (Exception e) {
                System.out.println("⚠️ [" + api + "] 失败: " + e.getMessage());
                continue;
            }
        }

        // 方案2：使用网易云备用链接
        String fallbackUrl = "https://music.163.com/song/media/outer/url?id=" + neteaseId;
        System.out.println("📌 所有API失败，使用网易云备用: " + fallbackUrl);
        
        return fallbackUrl;
    }

    /**
     * 尝试单个UNM API
     * @param api API基础地址
     * @param neteaseId 网易云歌曲ID
     * @return 音频URL或null
     */
    private String tryUnmApi(String api, String neteaseId) throws Exception {
        String requestUrl;

        if (api.contains("injahow")) {
            // ToolKal API格式
            requestUrl = api + "/song/url?id=" + neteaseId;
        } else if (api.contains("neteasecloud")) {
            // Vercel增强版API格式
            requestUrl = api + "/song/url/v1?id=" + neteaseId + "&level=standard";
        } else if (api.contains("utils")) {
            // UNM Utils API格式
            requestUrl = api + "/match?id=" + neteaseId;
        } else {
            return null;
        }

        System.out.println("   请求: " + requestUrl);

        // 使用Hutool发送HTTP GET请求
        String response = HttpUtil.get(requestUrl, 5000);  // 5秒超时
        
        System.out.println("   响应长度: " + response.length() + " 字符");

        // 解析响应
        return parseApiResponse(api, response);
    }

    /**
     * 解析不同API的响应格式
     * @param api API名称
     * @param response 响应字符串
     * @return 音频URL或null
     */
    private String parseApiResponse(String api, String response) {
        try {
            JSONObject json = JSONUtil.parseObj(response);
            
            // 检查是否成功
            Integer code = json.getInt("code");
            
            if (api.contains("injahow")) {
                // ToolKal格式: { code: 200, data: { url: "..." } }
                if (code != null && code == 200 && json.containsKey("data")) {
                    Object data = json.get("data");
                    if (data instanceof JSONObject) {
                        String url = ((JSONObject) data).getStr("url");
                        if (url != null && !url.isEmpty() && !url.equals("null")) {
                            return url;
                        }
                    }
                }
                
            } else if (api.contains("neteasecloud")) {
                // Vercel格式: { code: 200, data: [{ url: "..." }] }
                if (code != null && code == 200 && json.containsKey("data")) {
                    Object data = json.get("data");
                    if (data instanceof JSONArray) {
                        JSONArray dataArray = (JSONArray) data;
                        if (!dataArray.isEmpty()) {
                            String url = dataArray.getJSONObject(0).getStr("url");
                            if (url != null && !url.isEmpty() && !url.equals("null")) {
                                return url;
                            }
                        }
                    }
                }
                
            } else if (api.contains("utils")) {
                // UNM Utils格式: { code: 200, data: { url: "..." } }
                if (code != null && code == 200 && json.containsKey("data")) {
                    Object data = json.get("data");
                    if (data instanceof JSONObject) {
                        String url = ((JSONObject) data).getStr("url");
                        if (url != null && !url.isEmpty() && !url.equals("null")) {
                            return url;
                        }
                    }
                }
            }

            System.out.println("   ⚠️ 解析结果为空或无效");
            return null;
            
        } catch (Exception e) {
            System.err.println("   ❌ 解析响应异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取歌曲详情
     */
    @Override
    public Map<String, Object> getSongDetail(String neteaseId) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("neteaseId", neteaseId);
        detail.put("url", getAudioUrl(neteaseId));
        detail.put("timestamp", System.currentTimeMillis());
        
        return detail;
    }

    /**
     * 批量获取URL
     */
    @Override
    public Map<String, String> getBatchUrls(String[] ids) {
        Map<String, String> urlMap = new HashMap<>();
        
        System.out.println("\n📦 [音乐服务] 批量获取URL，共" + ids.length + "首");
        
        for (String id : ids) {
            if (id != null && !id.trim().isEmpty()) {
                String trimmedId = id.trim();
                try {
                    String url = getAudioUrl(trimmedId);
                    urlMap.put(trimmedId, url);
                    
                    // 稍微延迟，避免请求过快
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println("❌ 获取失败 [" + trimmedId + "]: " + e.getMessage());
                    urlMap.put(trimmedId, "https://music.163.com/song/media/outer/url?id=" + trimmedId);
                }
            }
        }
        
        System.out.println("✅ 批量获取完成，成功 " + urlMap.size() + " 首\n");
        
        return urlMap;
    }
}
