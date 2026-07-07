package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.tools.Constant;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 9:18
 */

@RestController
@RequestMapping("/user")            //一级路径
public class UserController {

    @Resource
    private UserService userService;


    // post 方法进行登录
    @PostMapping("/login")
    public Object login(HttpServletRequest request,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password){
        //参数校验
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            //说明参数非法
            return "用户名或密码为null";
        }

        //封装成一个 user 对象,方便 service 层的使用
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        //调用 service 中的接口查询 user 对象信息
        return userService.login(request,user);
    }



    // 用户注册
    @PostMapping("/register")
    public Object register(@RequestParam("username")String username,
                           @RequestParam("password1")String password1,
                           @RequestParam("password2")String password2){

        // 1. 检查参数
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password1) || !StringUtils.hasLength(password2)){
            // 参数有误
            return "用户名或密码为空";
        }

        if(!password1.equals(password2)){
            return "密码错误";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password1);
        return userService.register(user);
    }


    // 用户注销
    @GetMapping("/cancel")
    public Object cancelUser(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute(Constant.USER_INFO_SESSION_KEY) != null){
            session.removeAttribute(Constant.USER_INFO_SESSION_KEY);
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","success");
            map.put("message","注销成功!");
            return map;
        }
        return "注销失败!";
    }


    // 获取用户信息
    @GetMapping("/getuser")
    public Object getUserMessage(@SessionAttribute(Constant.USER_INFO_SESSION_KEY) User user){
        user.setPassword("");
        return user;
    }
}
