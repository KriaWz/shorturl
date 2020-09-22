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
 * @Description:
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


    @RequestMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletRequest request,HttpServletResponse response) throws IOException {
        String longUrl = longToShortService.shortToLong(shortUrl);
        visitInfoService.setVisitInfo(shortUrl,request);
        response.sendRedirect(longUrl);
    }

}
