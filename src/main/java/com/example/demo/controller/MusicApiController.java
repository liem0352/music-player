package com.example.demo.controller;

import com.example.demo.service.IMusicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 音乐API控制器 - 微信小程序专用
 * 提供获取真实中文歌播放URL的接口
 * 集成UnblockNeteaseMusic多源音源匹配
 */
@RestController
@RequestMapping("/api/music")
@CrossOrigin(origins = "*")  // 允许跨域访问
public class MusicApiController {

    @Autowired
    private IMusicApiService musicApiService;

    /**
     * 获取歌曲播放URL
     * @param neteaseId 网易云歌曲ID
     * @return 播放URL
     */
    @GetMapping("/url/{neteaseId}")
    public Map<String, Object> getMusicUrl(@PathVariable String neteaseId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String url = musicApiService.getAudioUrl(neteaseId);
            result.put("code", 200);
            result.put("msg", "获取成功");
            result.put("data", url);
            
            System.out.println("✅ [音乐API] 成功获取URL: " + neteaseId + " -> " + url.substring(0, Math.min(50, url.length())) + "...");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "获取失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量获取多首歌曲URL（用于歌单）
     * @param ids 网易云ID列表，逗号分隔
     * @return URL映射
     */
    @GetMapping("/urls")
    public Map<String, Object> getBatchUrls(@RequestParam String ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] idArray = ids.split(",");
            Map<String, String> urlMap = musicApiService.getBatchUrls(idArray);
            
            result.put("code", 200);
            result.put("msg", "批量获取成功");
            result.put("data", urlMap);
            
            System.out.println("✅ [音乐API] 批量获取成功，共" + urlMap.size() + "首");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "批量获取失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取歌曲详情
     * @param neteaseId 网易云歌曲ID
     * @return 歌曲信息
     */
    @GetMapping("/detail/{neteaseId}")
    public Map<String, Object> getMusicDetail(@PathVariable String neteaseId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> detail = musicApiService.getSongDetail(neteaseId);
            result.put("code", 200);
            result.put("data", detail);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "获取详情失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 健康检查接口 - 用于测试连接
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "音乐API服务正常运行");
        result.put("service", "Music API for WeChat Mini Program");
        result.put("version", "1.0.0");
        
        System.out.println("💚 [音乐API] 健康检查通过");
        
        return result;
    }
}
