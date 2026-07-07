package com.example.demo.interceptor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 14:33
 */

//添加 json 格式转换器 ——》 用来处理 SpringBoot 中 统一返回类型时 controller 直接返回一个 String 的情况
@SpringBootApplication
public class Application {
    @Bean
    public HttpMessageConverters converters() {
        return new HttpMessageConverters(
                false, Arrays.asList(new MappingJackson2HttpMessageConverter()));
    }
}
