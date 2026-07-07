package com.example.demo.service;

import com.example.demo.mapper.LoveMusicMapper;
import com.example.demo.model.LoveMusic;
import com.example.demo.model.Music;
import com.example.demo.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-13
 * Time: 21:59
 */
@Service
public class LoveMusicService {

    @Resource
    private LoveMusicMapper loveMusicMapper;


    // 收藏音乐
    public Object loveMusic(User user, Integer musicId){
        // 根据 userId 和 musicId 查询 lovemusic 数据
        LoveMusic loveMusic = loveMusicMapper.getLoveMusicByUidAndMid(user.getUid(), musicId);
        if(loveMusic != null){
            return "重复收藏!";
        }

        // 添加歌曲
        Boolean result = loveMusicMapper.addLoveMusic(user.getUid(), musicId);
        if(!result){
            return "添加失败!";
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("result","success");
        map.put("message","收藏音乐成功");

        return map;
    }


    // 查询收藏音乐文件（两种情况：有参模糊查，无参查全部）
    public Object getMusicSingleOrList(int pageNum , int pageSize ,User user,String musicName){
        List<Music> ret = loveMusicMapper.getMusicSingleOrList(user.getUid(),musicName);
        HashMap<String,Object> map = new HashMap<>();
        // 判断查询结果
        if(ret == null || ret.size() == 0){
            return "用户收藏音乐为空";
        }
        map.put("num",ret.size());

        PageHelper.startPage(pageNum,pageSize);
        List<Music> musicList = loveMusicMapper.getMusicSingleOrList(user.getUid(),musicName);
        PageInfo<Music> pageInfo = new PageInfo<>(musicList);
        map.put("musicList",pageInfo.getList());
        return map;
    }


    // 取消音乐的收藏
    public Object removeLoveMusic(Integer userId,Integer musicId){
        Integer result = loveMusicMapper.removeCollectMusic(userId, musicId);
        if(result != 1){
            return "音乐取消收藏失败";
        }
        List<Music> list = loveMusicMapper.getMusicSingleOrList(userId, null);
        HashMap<String,Object> map = new HashMap<>();
        map.put("result","success");
        map.put("message","取消收藏成功!");
        map.put("musicTotal",list.size());
        return map;
    }
}
