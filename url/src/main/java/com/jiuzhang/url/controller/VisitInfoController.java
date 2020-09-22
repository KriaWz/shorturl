package com.jiuzhang.url.controller;

import com.jiuzhang.url.annotation.Limit;
import com.jiuzhang.url.common.LimitType;
import com.jiuzhang.url.entity.VisitInfo;
import com.jiuzhang.url.service.VisitInfoService;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wenzhen
 * @since 2020-09-07
 */
@RestController
@RequestMapping("/views")
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

    @GetMapping("/visitors")
    public List<VisitInfo> infoList(){
        List<VisitInfo> visitInfoList = visitInfoService.listLatestVisitInfo();
        return visitInfoList;
    }

}

