package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.MusicService;
import com.example.demo.tools.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-09
 * Time: 13:01
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Value("${music.path}")
    private String path;



    // 上传音乐
    @PostMapping("/upload")
    public Object upload(@SessionAttribute(value = Constant.USER_INFO_SESSION_KEY) User user,
                         @RequestParam(value = "singer") String singer,
                         @RequestParam(value = "filename") MultipartFile file){

        // 检查前端 singer 和 file 参数是否为null
        if(!StringUtils.hasLength(singer) || file.isEmpty() || file.getSize() == 0 ){
            // 返回 msg 信息
            return "歌手或音乐文件不合法！";
        }

        return musicService.upload(user,singer,file);

    }


    // 删除单个音乐
    @PostMapping("/delete")
    public Object deleteMusicById(  @SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user,
                                    @RequestParam("mid") Integer id){
        if(id == null){
            return "id不合法!";
        }
        if(user.getStatus().equals(false)){
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","fail");
            map.put("message","不是管理员，无法删除!");
            return map;
        }
        return musicService.deleteMusicById(id);
    }



    // 批量删除音乐
    @PostMapping("/deletesel")
    public Object deleteMusicList(@SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user,
                                  @RequestParam("id[]")List<Integer> idList){
        // 判空
        if(idList == null || idList.size() == 0){
            return "参数不正确!";
        }
        if(user.getStatus().equals(false)){
            return  "非管理员不能删除音乐！";
        }

        return musicService.deleteMusicList(idList);
    }



    // 查询音乐（传入为 null 时，表示查询全部，有参数时表示根据名称来模糊查询）
    @GetMapping("/getmusic")
    public Object getMusicByNameOrAll(@RequestParam(value = "musicname",required = false) String musicName,
                                      @RequestParam Integer pageNum,         // 第几页
                                      @RequestParam Integer pageSize){       // 每页有几条数据
        return musicService.getMusicByNameOrAll(pageNum,pageSize,musicName);
    }

}
