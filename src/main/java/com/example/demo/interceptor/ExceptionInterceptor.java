package com.example.demo.interceptor;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 10:32
 */


// 对异常进行统一处理
@RestControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(Exception.class)
    public Object exceptionInterceptor(Exception e){
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",0);
        map.put("data","");
        map.put("msg","异常信息:"+e.getMessage());
        return map;
    }
}
