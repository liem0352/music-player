package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.tools.Constant;
import com.example.demo.tools.EncryptionTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 9:41
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private EncryptionTool encryptionTool;



    // 调用用户登录所需要的接口
    public Object login(HttpServletRequest request, User user){

        // 调用 mybatis 中的用户查找接口
        User userTest = userMapper.getUserByName(user.getUsername());

        // 判断一下查到的数据是否为null
        if(userTest != null){
            // 判断密码是不是md5+加盐产生的（数据库是从空表开始插入数据的话,应该不用判）
            if(userTest.getPassword().charAt(0) != '$' && encryptionTool.decrypt(user.getPassword(),userTest.getPassword())){

                userTest.setPassword("");  //密码隐藏

                //将用户信息添加到 session 中
                HttpSession session = request.getSession(true);
                session.setAttribute(Constant.USER_INFO_SESSION_KEY,userTest);
                //返回查询出来的 user 对象信息
                return userTest;
            }
        }
        // 返回 msg 信息
        return "用户名或密码有误";
    }



    // 用户注册
    public Object register(User user){

        //查询该用户名是否被注册
        User userByName = userMapper.getUserByName(user.getUsername());
        if(userByName != null){
            // 该用户已被注册
            return "该账号已被注册！";
        }
        // 获取加密密码
        String encryptPassword = encryptionTool.encrypt(user.getPassword());
        user.setPassword(encryptPassword);

        int register = userMapper.register(user);

        if(register == 1){
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","success");
            map.put("redirect","/login.html");
            return map;
        }else{
            return "注册失败!";
        }
    }
}
