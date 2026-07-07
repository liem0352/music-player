package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-09
 * Time: 13:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Music {
    private Integer mid;
    private String mname;
    private String msinger;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    private String url;
    private Integer uid;
    private User userInfo;


    public Music(String mname,String msinger){
        this.mname = mname;
        this.msinger = msinger;
    }


    public Music(Integer mid){
        this.mid = mid;
    }

}
