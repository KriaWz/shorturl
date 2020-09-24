package com.jiuzhang.url.controller;

import com.jiuzhang.url.service.LongToShortService;
import com.jiuzhang.url.service.VisitInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: WZ
 * @Date: 2020/9/8 15:21
 * @Description: 短网址处理重定向
 */
@Controller
@RequestMapping("/s")
@Api
@CrossOrigin
public class ShortToLongController {

    @Autowired
    private LongToShortService longToShortService;

    @Autowired
    private VisitInfoService visitInfoService;


    /**
     * 重定向到原来长网址
     * @param shortUrl
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletRequest request,HttpServletResponse response) throws IOException {
        String longUrl = longToShortService.shortToLong(shortUrl);
        visitInfoService.setVisitInfo(shortUrl,request);
        response.sendRedirect(longUrl);
    }

}
