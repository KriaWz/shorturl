package com.jiuzhang.url.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiuzhang.url.annotation.Limit;
import com.jiuzhang.url.common.LimitType;
import com.jiuzhang.url.config.ThreadPoolConfigProperties;
import com.jiuzhang.url.entity.VisitInfo;
import com.jiuzhang.url.service.VisitInfoService;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wenzhen
 * @since 2020-09-07
 */
@RestController
@RequestMapping("/visitInfo")
@CrossOrigin
public class VisitInfoController {

    @Autowired
    private VisitInfoService visitInfoService;

    @Limit(speed = 3000, count = 3000,limitType = LimitType.IP)
    @GetMapping("/countLatest")
    public List<LatestSumMax> latestSumMaxList(){
        List<LatestSumMax> list = visitInfoService.getList();
        return list;
    }

    @GetMapping("/list")
    public List<VisitInfo> infoList(){
        List<VisitInfo> visitInfoList = visitInfoService.list();
        return visitInfoList;
    }

}

