package com.jiuzhang.url.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LatestSumMax implements Serializable {

    private String longUrl;

    private Long sum;

    public LatestSumMax(String longUrl, Long sum) {
        this.longUrl = longUrl;
        this.sum = sum;
    }
}
