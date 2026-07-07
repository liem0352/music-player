package com.example.demo.interceptor;

import com.example.demo.tools.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 15:56
 */

// 用户身份验证拦截器
@Component
public class UserLoginIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 获取 session
        HttpSession session = request.getSession(false);
        // 2. 判断
        if(session == null || session.getAttribute(Constant.USER_INFO_SESSION_KEY) == null){
            // 没有登录就重定向到 用户登录页面
            response.sendRedirect("/login.html");
            return false;
        }
        return true;
    }
}
