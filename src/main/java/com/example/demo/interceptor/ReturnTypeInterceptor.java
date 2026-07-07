package com.example.demo.interceptor;

import com.example.demo.tools.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 9:25
 */

// 统一返回类型处理
@ControllerAdvice
public class ReturnTypeInterceptor implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 如果触发了异常拦截，就不用进行统一处理
        String name = returnType.getMethod().getName();
        if("exceptionInterceptor".equals(name)){
            return false;
        }
        // 如果是播放音乐，就不经过统一类型返回
        if("getMusic".equals(name)){
            return false;
        }
        return true;
    }



    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                                   MediaType selectedContentType,
                                                   Class selectedConverterType,
                                                   ServerHttpRequest request,
                                                   ServerHttpResponse response) {

        Object obj = null;
        // 通过判断是否是一个 String 类型来决定是否有错误信息
        if(body instanceof String){
            obj = Result.fail(body);

            // 解决 不能返回一个 String 的第二种方式 ，封装一个 toJsonString 方法
//            return toJsonString(obj);
        } else{
            obj = Result.success(body);
        }
        return obj;
    }



    private Object toJsonString(Object response){
        try {
            // 通过 jackson 来将对象转换成一个 json 格式的字符串
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            // 表示转换不了
            e.printStackTrace();
        }
        return null;
    }
}
