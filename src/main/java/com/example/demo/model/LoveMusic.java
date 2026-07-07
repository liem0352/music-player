package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-13
 * Time: 21:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoveMusic {

    private Integer loveId;
    private Integer userId;
    private Integer musicId;

}
