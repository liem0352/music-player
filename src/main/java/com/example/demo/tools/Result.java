package com.example.demo.tools;

import lombok.Data;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 14:57
 */

// 自定义响应数据类型
@Data
public class Result{
    private int status;      // 表示通讯是否成功
    private String msg;      // 响应信息
    private Object data;     // 响应数据

    private Result(){
    }

    // 响应成功的数据模板
    public static Result success(Object data){
        Result result = new Result();
        result.setStatus(1);
        result.setMsg("");
        result.setData(data);
        return result;
    }

    // 响应失败的数据模板
    public static Result fail(Object msg){
        String str = msg.toString();
        Result result = new Result();
        result.setStatus(0);
        result.setMsg(str);
        result.setData("");
        return result;
    }
}
