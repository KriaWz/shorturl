package com.jiuzhang.url.service;

import com.jiuzhang.url.domain.LongToShort;
import com.jiuzhang.url.vo.UrlVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther: WZ
 * @Date: 2020/9/7 14:57
 * @Description:
 */
public interface LongToShortService {

    UrlVO transfer(String longUrl, HttpServletRequest request);

    String shortToLong(String shortUrl);
}
