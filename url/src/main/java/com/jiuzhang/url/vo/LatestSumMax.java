package com.jiuzhang.url.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: WZ
 * @Date: 2020/9/13 10:47
 * @Description: 最近访问数量
 */
@Data
public class LatestSumMax implements Serializable {

    private String longUrl;

    private Integer sum;
}
