package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 9:02
 */
@Mapper
public interface UserMapper {

    /**
     * 用户登录
     * @param user 用户信息
     * @return  成功登录的用户信息
     */
    User login(User user);

    /**
     * 用户注册
     * @param use 用户信息
     * @return  数据库受影响的行数
     */
    int register(User use);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 查找的用户信息
     */
    User getUserByName(String username);
}
