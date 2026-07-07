package com.example.demo.interceptor;

import com.example.demo.tools.CheckTypeMP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 16:02
 */

// 将自定义拦截器加入到系统配置中
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private UserLoginIdInterceptor userLoginIdInterceptor;


    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加用户登录拦截器
        registry.addInterceptor(userLoginIdInterceptor)
                .addPathPatterns("/**")         // 拦截所有 url
                .excludePathPatterns("/user/login")       // 排除登录接口
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/login.html")       // 排除登录页面
                .excludePathPatterns("/**/*.js")          // 排除静态资源
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/register.html");

    }


    // 通过 IOC 来管理 BCrypt 对象
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // 提前将 检查MP3格式的类交给 IOC容器来管理
    @Bean
    public CheckTypeMP3 getCheckTypeMP3(){
        return new CheckTypeMP3();
    }
}
