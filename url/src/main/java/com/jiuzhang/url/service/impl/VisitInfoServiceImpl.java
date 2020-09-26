package com.jiuzhang.url.service.impl;

import com.jiuzhang.url.entity.VisitInfo;
import com.jiuzhang.url.mapper.VisitInfoMapper;
import com.jiuzhang.url.service.VisitInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuzhang.url.utils.IpUtil;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wenzhen
 * @since 2020-09-07
 */
@Service
public class VisitInfoServiceImpl extends ServiceImpl<VisitInfoMapper, VisitInfo> implements VisitInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存放访客信息
     * @param shortUrl
     * @param request
     */
    @Override
    public void setVisitInfo(String shortUrl, HttpServletRequest request) {
        // 直接从redis里读取，因为之前短转长已经存放键值对
        String longUrl = (String) redisTemplate.opsForValue().get(shortUrl);
        VisitInfo visitInfo = new VisitInfo();
        visitInfo.setShortUrl(shortUrl);
        visitInfo.setLongUrl(longUrl);
        String ipAddr = IpUtil.getIpAddr(request);
        visitInfo.setFromIp(ipAddr);
        String referer = request.getHeader("Referer");
        visitInfo.setFormUrl(referer);
        baseMapper.insert(visitInfo);
    }

    /**
     * 返回对应最近访问最多10条短网址长网址信息
     * @return
     */
    @Override
    public List<LatestSumMax> getList(){
        List<LatestSumMax> visitInfos = baseMapper.countList();
        return visitInfos;
    }

    /**
     * 返回对应最近访问10条访客记录
     * @return
     */
    @Override
    public List<VisitInfo> listLatestVisitInfo() {
        List<VisitInfo> visitInfoList = baseMapper.listLatestVisitInfo();
        return visitInfoList;
    }

}
