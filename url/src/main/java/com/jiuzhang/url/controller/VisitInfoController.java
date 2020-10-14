package com.jiuzhang.url.controller;

import com.jiuzhang.url.annotation.Limit;
import com.jiuzhang.url.common.LimitType;
import com.jiuzhang.url.domain.VisitInfo;
import com.jiuzhang.url.service.VisitInfoService;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @author wenzhen
 * @since 2020-09-07
 * @Description 短网址访问信息
 */
@RestController
@RequestMapping("/views")
@CrossOrigin
public class VisitInfoController {

    @Autowired
    private VisitInfoService visitInfoService;

    /**
     * 最常访问的10个短网址及对应长网址
     * @return
     */
    @Limit(speed = 3000, count = 3000,limitType = LimitType.IP)
    @GetMapping("/countLatest")
    public List<LatestSumMax> latestSumMaxList(){
        List<LatestSumMax> list = visitInfoService.getList();
        //return Result.ofSuccess(list).setCode(200);
        return list;
    }

    /**
     * 最近10条访客信息
     * @return
     */
    @GetMapping("/visitors")
    public List<VisitInfo> infoList(){
        List<VisitInfo> visitInfoList = visitInfoService.listLatestVisitInfo();
        //return Result.ofSuccess(visitInfoList).setCode(200);
        return visitInfoList;
    }

}

