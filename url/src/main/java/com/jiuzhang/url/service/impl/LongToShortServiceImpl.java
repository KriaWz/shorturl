package com.jiuzhang.url.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuzhang.url.entity.LongToShort;
import com.jiuzhang.url.mapper.LongToShortMapper;
import com.jiuzhang.url.service.LongToShortService;
import com.jiuzhang.url.util.IpUtil;
import com.jiuzhang.url.util.RandomUtil;
import com.jiuzhang.url.vo.UrlVo;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

/**
 * @auther: WZ
 * @Date: 2020/9/7 14:58
 * @Description:
 */
@Service
public class LongToShortServiceImpl extends ServiceImpl<LongToShortMapper, LongToShort> implements LongToShortService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    ThreadPoolExecutor executor;

    private final static Logger logger = LoggerFactory.getLogger(LongToShortServiceImpl.class);

    @Override
    //@Cacheable(value = {"longToshort"},key = "#root.method.name")
    public UrlVo transFer(String longUrl, HttpServletRequest request) {
        UrlVo urlVo = new UrlVo();
        if( !longUrl.startsWith("http://localhost") && (longUrl.startsWith("http://")
                || longUrl.startsWith("https://"))){
            String shortExist = (String) redisTemplate.opsForValue().get(longUrl);
            redisTemplate.expire(longUrl,60,TimeUnit.MINUTES);
            if(!StringUtils.isEmpty(shortExist)){
                if (redisTemplate.opsForValue().get(shortExist +"sum") == null){
                    redisTemplate.opsForValue().set(shortExist + "sum" , 0,60,TimeUnit.MINUTES);
                }
                redisTemplate.opsForValue().increment(shortExist+"sum");
                String fullUrl = "http://localhost:8080/s/"+shortExist;
                urlVo.setUrl(fullUrl);
                return urlVo;
            }
            QueryWrapper<LongToShort> wrapperLong = new QueryWrapper<>();
            wrapperLong.eq("long_url",longUrl);
            LongToShort LongData = baseMapper.selectOne(wrapperLong);
            if( LongData != null ){
                String longUrlMeta = LongData.getLongUrl();
                String shortUrlMeta = LongData.getShortUrl();
                redisTemplate.opsForValue().set(shortUrlMeta+"sum",0,60,TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(longUrlMeta,shortUrlMeta,60,TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(shortUrlMeta,longUrlMeta,60,TimeUnit.MINUTES);
                String fullUrl = "http://localhost:8080/s/"+shortUrlMeta;
                urlVo.setUrl(fullUrl);
                return urlVo;
            }
            String ipAddr = IpUtil.getIpAddr(request);
            LongToShort longToShort = new LongToShort();
            String shortUrl = RandomUtil.BaseTrans(longUrl);
            QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
            wrapper.eq("short_url", shortUrl);
            LongToShort one = baseMapper.selectOne(wrapper);
            if(one == null){
                redisTemplate.opsForValue().set(longUrl,shortUrl,60,TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(shortUrl,longUrl,60,TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(shortUrl+"sum",0,60,TimeUnit.MINUTES);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        longToShort.setLongUrl(longUrl);
                        longToShort.setShortUrl(shortUrl);
                        longToShort.setFromIp(ipAddr);
                        baseMapper.insert(longToShort);
                        System.out.println(Thread.currentThread().getName());
                    }
                });
                String fullurl = "http://localhost:8080/s/"+shortUrl;
                urlVo.setUrl(fullurl);
                return urlVo;
            }
            else {
                transFer(longUrl,request);
            }
            return null;
        }
        if(longUrl.startsWith("http://localhost:8080")){
            String s = longUrl.substring(22);
            String url = shortToLong(s);
            urlVo.setUrl(url);
            return urlVo;
        }
        logger.error("格式错误");
        return null;
    }

    @Override
    public String shortToLong(String shortUrl) {
        String longUrl = (String) redisTemplate.opsForValue().get(shortUrl);
        redisTemplate.expire(shortUrl,60,TimeUnit.MINUTES);
        if(!StringUtils.isEmpty(longUrl)){
            redisTemplate.opsForValue().increment(shortUrl+"sum");
            return longUrl;
        }
        QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        LongToShort one = baseMapper.selectOne(wrapper);
        longUrl = one.getLongUrl();
        redisTemplate.opsForValue().set(shortUrl,longUrl,60,TimeUnit.MINUTES);
        return longUrl;
    }

}
