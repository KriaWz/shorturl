package com.jiuzhang.url.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: WZ
 * @Date: 2020/9/13 10:47
 * @Description:
 */
@Data
public class LatestSumMax implements Serializable {

    private String longUrl;

    private Integer sum;
}
