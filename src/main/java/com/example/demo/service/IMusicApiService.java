package com.example.demo.service;

import java.util.Map;

/**
 * 音乐API服务接口
 * 提供获取真实中文歌URL的功能
 */
public interface IMusicApiService {

    /**
     * 获取歌曲播放URL（多源匹配）
     * 优先级：UnblockNeteaseMusic → QQ音乐 → 酷我 → 网易云备用
     * @param neteaseId 网易云歌曲ID
     * @return 音频URL
     */
    String getAudioUrl(String neteaseId);

    /**
     * 获取歌曲详情
     * @param neteaseId 网易云歌曲ID
     * @return 歌曲详情
     */
    Map<String, Object> getSongDetail(String neteaseId);

    /**
     * 批量获取URL
     * @param ids ID数组
     * @return URL映射
     */
    Map<String, String> getBatchUrls(String[] ids);
}
