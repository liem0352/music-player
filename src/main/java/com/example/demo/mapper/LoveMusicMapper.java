package com.example.demo.mapper;

import com.example.demo.model.LoveMusic;
import com.example.demo.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-13
 * Time: 21:39
 */
@Mapper
public interface LoveMusicMapper {

    /**
     * 根据 userId 和 musicId 来查询用户收藏音乐信息
     * @param userId    用户id
     * @param musicId   音乐id
     * @return  查询到的用户收藏音乐的信息
     */
    LoveMusic getLoveMusicByUidAndMid(@Param("userid") Integer userId,
                                      @Param("musicid") Integer musicId);


    /**
     * 用户收藏音乐
     * @param userId    用户id
     * @param musicId   音乐id
     * @return  添加数据成功与否
     */
    Boolean addLoveMusic(@Param("userid") Integer userId,
                         @Param("musicid") Integer musicId);


    /**
     * 查询 收藏的音乐集合（有 musicName 就根据歌名进行模糊查询，没有就查全部）
     * @param userId     用户id（必传）
     * @param musicName  音乐名称（非必传）
     * @return 查询到的音乐信息集合
     */
    List<Music> getMusicSingleOrList(@Param("userid") Integer userId,
                                     @Param("musicname") String musicName);

    /**
     * 用户取消收藏某一首音乐
     * @param userId   用户id
     * @param musicId  音乐id
     * @return  数据库表受影响的行数
     */
    Integer removeCollectMusic(@Param("userid") Integer userId,
                               @Param("musicid") Integer musicId);


    /**
     * 一旦删除音乐后，就需要将收藏了这条音乐的 lovemusic 记录给删除掉
     * @param musicId  音乐id
     * @return     数据库表被影响的行数
     */
    Integer removeCollectMusicByMusicId(@Param("musicid")Integer musicId);
}
