package com.example.demo.assist;

import com.example.demo.mapper.LoveMusicMapper;
import com.example.demo.mapper.MusicMapper;
import com.example.demo.model.Music;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-13
 * Time: 11:13
 */

// MusicService 类的辅助工具
@Component
public class MusicAssistTools {

    @Resource
    private MusicMapper musicMapper;

    @Resource
    private LoveMusicMapper loveMusicMapper;

    @Value("${music.path}")
    private String path;



    // 单个音乐的删除（三方面删除：music表、lovemusic表 和 服务器）
    @Transactional
    public Object deleteSingleMusic(Integer musicId){
        // 先查出来,看看数据库里有没有这个音乐的信息
        Music music = musicMapper.getMusicByNameAndSingerOrId(new Music(musicId));

        // 没找到直接返回错误信息
        if(music == null){
            return "删除失败,数据库中没有音乐！";
        }
        // 删除数据库里的音乐
        Integer result = musicMapper.deleteMusicById(musicId);
        if(result != 1){
            // 删除操作失败了
            return "数据库删除失败！";
        }

        // 删除 lovemusic 表里的音乐 收藏记录
        Integer res = loveMusicMapper.removeCollectMusicByMusicId(musicId);
        if(res == null){
            return "收藏音乐表删除失败!";
        }

        // 删除服务器里的音乐文件(.mp3)
        String filename = path + music.getMname()+".mp3";
        File file = new File(filename);

        if(!file.exists()){
            // 手动回滚，还原数据库中的原有数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "删除失败,找不到该文件";
        }
        boolean delete = file.delete();
        if(delete){
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","success");
            map.put("message","删除成功");
            return map;
        }else{
            // 手动回滚，还原数据库中的原有数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "音乐文件删除失败!";
        }
    }
}
