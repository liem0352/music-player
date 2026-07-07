package com.example.demo.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 9:01
 */
@Data
public class User {
    private Integer uid;
    private String username;
    private String password;
    private Boolean status;
}
