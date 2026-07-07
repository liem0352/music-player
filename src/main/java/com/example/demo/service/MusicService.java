package com.example.demo.service;

import com.example.demo.assist.MusicAssistTools;
import com.example.demo.mapper.MusicMapper;
import com.example.demo.model.Music;
import com.example.demo.model.User;
import com.example.demo.tools.CheckTypeMP3;
import com.example.demo.tools.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-09
 * Time: 13:18
 */
@Service
public class MusicService {

    @Resource
    private MusicMapper musicMapper;

    @Autowired
    private CheckTypeMP3 checkTypeMP3;

    @Value("${music.path}")
    private String path;

    @Autowired
    private MusicAssistTools musicAssistTools;



    // 上传音乐
    public Object upload(User user, String singer, MultipartFile file) {

        File f = null;
        // 上传文件
        try {
            // 拿到 整个文件名(文件名+后缀)
            String filename = file.getOriginalFilename();

            // 拿到文件名
            assert filename != null;
            int index = filename.lastIndexOf(".");
            String title = filename.substring(0,index);

            // 检查该首音乐是否重复上传（歌名和歌手都相同就表示重复）
            Music musicByName = musicMapper.getMusicByNameAndSingerOrId(new Music(title,singer));
            if(musicByName != null){
                return "歌曲已在音乐库中!";
            }

            // 检查 歌手 不同， 歌名 相同的情况：返回上传失败（歌名不能重复信息）
            Music result = musicMapper.getMusicByNameAndSingerOrId(new Music(title, null));
            if(result != null){
                // 说明有相同的歌名，直接上传会覆盖掉之前的音乐
                return "歌名重复!";
            }


            // 拼接文件路径（后面需要请求到后端接口来实现传输字节数据）
            String musicPath = path + filename;
            f = new File(musicPath);
            // 如果路径不存在就创建一个
            if (!f.exists()) {
                f.mkdirs();
            }
            // 将上传的文件写入到本地
            file.transferTo(f);

            // 检查上传的文件是否是 mp3 格式
            boolean flag = checkTypeMP3.checkTypeMP3(f);
            if(!flag){
                // 删除该文件
                f.delete();
                return "文件不是mp3格式!";
            }

            // 将文件封装成一个 Music对象
            Music music = new Music();
            music.setMsinger(singer);
            music.setUid(user.getUid());
            music.setUrl(Constant.MUSIC_PATH+filename);
            music.setMname(title);

            // 将音乐信息更新到数据库中
            Integer upload = musicMapper.upload(music);
            if (upload == 1) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("result","success");
                map.put("redirect","list.html");       // 文件上传成功,应该需要返回到列表页（后序实现）
                return map;
            }
            // 走到这里说明 服务器上传成功，但是 数据库上传 失败了。所以需要删除一些文件。
            if(f != null){
                boolean d = f.delete();
                if(!d){
                    return "收尾删除音乐文件工作失败!";
                }
            }

        } catch (Exception e) {
            if(f != null){
                f.delete();
            }
            // 一旦抛出异常就表示上传失败
            e.printStackTrace();
            return "上传失败!";
        }
        // 说明上传失败了！
        return "上传失败!";
    }



    // 单个音乐的删除
    public Object deleteMusicById(Integer id){
        Object obj = musicAssistTools.deleteSingleMusic(id);
        if(!(obj instanceof String)){
            HashMap<String,Object> ret = (HashMap<String, Object>) obj;
            List<Music> list = musicMapper.getMusicByNameOrAll(null);
            ret.put("musicTotal",list.size());
            return ret;
        }
        return obj;
    }



    // 批量音乐的删除
    public Object deleteMusicList(List<Integer> idList) {
        int number = idList.size();
        // 统计被影响的行数
        int count = 0;
        // 循环处理所有的 id
        for(int i = 0; i < number; i++){
            Integer id = idList.get(i);
            Object o = musicAssistTools.deleteSingleMusic(id);
            if(o instanceof String){
                if(count != 0){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("result","error");
                    map.put("message","成功删除部分音乐");
                    map.put("delete_total",count);
                    return map;
                }
            }else{
                // 累计被影响的行数
                count++;
            }
        }
        // 总共被影响的行数 == id 数，说明批量删除成功
        if(count == idList.size()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","success");
            map.put("message","批量删除成功！");
            map.put("delete_total",count);
            return map;
        }
        // 一条都没删除成功就走这里
        return "批量删除失败!";
    }



    // 模糊查询音乐 或 查询所有音乐
    public Object getMusicByNameOrAll(int pageNum,int pageSize, String musicName){
        List<Music> ret = musicMapper.getMusicByNameOrAll(musicName);
        HashMap<String,Object> map = new HashMap<>();
        if(ret == null || ret.size() == 0){
            return "查询结果为空!";
        }
        map.put("num",ret.size());

        PageHelper.startPage(pageNum,pageSize);
        List<Music> musicList = musicMapper.getMusicByNameOrAll(musicName);

        PageInfo<Music> pageInfo = new PageInfo<>(musicList);
        map.put("musicList",pageInfo.getList());
        // 返回查询的结果集
        return map;
    }
}
