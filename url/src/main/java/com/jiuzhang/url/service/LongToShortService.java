package com.jiuzhang.url.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuzhang.url.entity.LongToShort;
import com.jiuzhang.url.vo.UrlVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther: WZ
 * @Date: 2020/9/7 14:57
 * @Description:
 */
public interface LongToShortService extends IService<LongToShort> {

    UrlVO transfer(String longUrl, HttpServletRequest request);

    String shortToLong(String shortUrl);
}
