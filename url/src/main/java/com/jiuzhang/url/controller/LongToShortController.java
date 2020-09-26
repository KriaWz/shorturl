package com.jiuzhang.url.controller;

import com.jiuzhang.url.annotation.Limit;
import com.jiuzhang.url.common.LimitType;
import com.jiuzhang.url.common.Result;
import com.jiuzhang.url.entity.LongToShort;
import com.jiuzhang.url.service.LongToShortService;
import com.jiuzhang.url.vo.UrlVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther: WZ
 * @Date: 2020/9/7 14:59
 * @Description: 长网址/短网址 相互转换
 */
@RestController
@RequestMapping("/url")
@Api
@CrossOrigin
public class LongToShortController {

    @Autowired
    private LongToShortService longToShortService;

    /**
     * transform接口
     * @param urlVo
     * @param request
     * @return json：{"url" : "http://www.baidu.com"}
     */
    @PostMapping("/transform")
    public UrlVo longTransfer(@RequestBody UrlVo urlVo, HttpServletRequest request){
        String Url = urlVo.getUrl();
        UrlVo longToShort = longToShortService.transfer(Url,request);
        //return Result.ofSuccess(longToShort).setCode(200);
        return longToShort;
    }

}
