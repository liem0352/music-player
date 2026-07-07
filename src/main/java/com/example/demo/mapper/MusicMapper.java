package com.example.demo.mapper;

import com.example.demo.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-09
 * Time: 13:19
 */
@Mapper
public interface MusicMapper {

    /**
     * 上传音乐
     * @param music 音乐信息
     * @return  数据库被影响的行数
     */
    Integer upload(Music music);

    /**
     * 通过歌名和歌手获取音乐
     * @param music 存放了歌手和歌名的Music对象
     * @return 查到的音乐信息
     */
    Music getMusicByNameAndSingerOrId(Music music);

    /**
     * 单个音乐的删除操作
     * @param musicId 音乐的 id
     * @return  数据库受影响的行数
     */
    Integer deleteMusicById(@Param(value = "mid") Integer musicId);

    /**
     * 查询音乐（可跟据名称查，可查全部。并支持模糊匹配）
     * @param musicName 音乐名称(可有可无)
     * @return  查到的音乐的集合
     */
    List<Music> getMusicByNameOrAll(@Param(value = "mname") String musicName);

}
