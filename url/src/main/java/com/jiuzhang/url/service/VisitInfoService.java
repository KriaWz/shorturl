package com.jiuzhang.url.service;

import com.jiuzhang.url.domain.VisitInfo;
import com.jiuzhang.url.vo.LatestSumMax;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wenzhen
 * @since 2020-09-07
 */
public interface VisitInfoService {

    void setVisitInfo(String shortUrl, HttpServletRequest request);

    List<LatestSumMax> getList();

    List<VisitInfo> listLatestVisitInfo();
}
