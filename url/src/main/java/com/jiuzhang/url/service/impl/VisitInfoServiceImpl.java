package com.jiuzhang.url.service.impl;

import com.jiuzhang.url.domain.VisitInfo;
import com.jiuzhang.url.repo.VisitInfoRepository;
import com.jiuzhang.url.service.VisitInfoService;
import com.jiuzhang.url.utils.IpUtil;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class VisitInfoServiceImpl implements VisitInfoService {

    @Autowired
    private VisitInfoRepository visitInfoRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存放访客信息
     * @param shortUrl
     * @param request
     */
    @Override
    @Transactional
    public void setVisitInfo(String shortUrl, HttpServletRequest request) {
        // 直接从redis里读取，因为之前短转长已经存放键值对
        String longUrl = (String) redisTemplate.opsForValue().get(shortUrl);
        VisitInfo visitInfo = new VisitInfo();
        visitInfo.setShortUrl(shortUrl);
        visitInfo.setLongUrl(longUrl);
        String ipAddr = IpUtil.getIpAddr(request);
        visitInfo.setFromIp(ipAddr);
        String referer = request.getHeader("Referer");
        visitInfo.setFromUrl(referer);
        visitInfo.setGmtCreate(new Date());

        visitInfoRepository.save(visitInfo);
    }

    /**
     * 返回对应最近访问最多10条短网址长网址信息
     * @return
     */
    @Override
    public List<LatestSumMax> getList(){
        List<Object[]> visitInfoObjs = visitInfoRepository.findLatestSumMax();
        List<LatestSumMax> visitInfos = visitInfoObjs.stream().map(i-> new LatestSumMax((String)i[0], ((Number)i[1]).longValue())).collect(Collectors.toList());
        return visitInfos;
    }

    /**
     * 返回对应最近访问10条访客记录
     * @return
     */
    @Override
    public List<VisitInfo> listLatestVisitInfo() {
        List<VisitInfo> visitInfoList = visitInfoRepository.findTop10ByOrderByGmtCreateDesc();
        return visitInfoList;
    }

}
