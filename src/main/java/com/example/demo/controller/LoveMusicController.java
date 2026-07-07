package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.LoveMusicService;
import com.example.demo.tools.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-13
 * Time: 21:20
 */
@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {

    @Autowired
    private LoveMusicService loveMusicService;



    // 收藏音乐
    @PostMapping("/collectmusic")     // 必须要有用户登录
    public Object loveMusic(@SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user,
                            @RequestParam("id") Integer musicId){

        if(musicId == null){
            return "参数为空!";
        }

        return loveMusicService.loveMusic(user,musicId);
    }


    //查询收藏的音乐（有参数就根据音乐名来模糊匹配，没有就查询全部）
    @GetMapping("/getlovemusic")
    public Object getMusicSingleOrList(@SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user,
                                       @RequestParam(value = "musicname",required = false) String musicName,
                                       @RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize){

        if(user == null || user.getUid() == 0){
            return "没有用户信息";
        }
        return loveMusicService.getMusicSingleOrList(pageNum,pageSize,user,musicName);
    }


    // 音乐取消收藏
    @PostMapping("/removelovemusic")
    public Object removeLoveMusic(@SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user,
                                  @RequestParam("id") Integer musicId){
        if(user == null || user.getUid() == 0){
            return "用户未登录!";
        }
        return loveMusicService.removeLoveMusic(user.getUid(),musicId);
    }
}
