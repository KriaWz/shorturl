package com.jiuzhang.url.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiuzhang.url.entity.VisitInfo;
import com.jiuzhang.url.mapper.VisitInfoMapper;
import com.jiuzhang.url.service.VisitInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuzhang.url.util.IpUtil;
import com.jiuzhang.url.vo.LatestSumMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void setVisitInfo(String shortUrl, HttpServletRequest request) {
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

    @Override
    public List<LatestSumMax> getList(){
        List<LatestSumMax> visitInfos = baseMapper.countList();
        return visitInfos;
    }

}
