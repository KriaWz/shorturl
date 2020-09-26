package com.jiuzhang.url.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuzhang.url.entity.LongToShort;
import com.jiuzhang.url.mapper.LongToShortMapper;
import com.jiuzhang.url.service.LongToShortService;
import com.jiuzhang.url.utils.IpUtil;
import com.jiuzhang.url.utils.RandomUtil;
import com.jiuzhang.url.utils.RedisUtils;
import com.jiuzhang.url.utils.UrlUtil;
import com.jiuzhang.url.vo.UrlVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ThreadPoolExecutor executor;

    @Autowired
    private RedisUtils redisUtil;

    @Value("${shorturl.prefix}")
    private String shortUrlPrefix;

    private final static Logger logger = LoggerFactory.getLogger(LongToShortServiceImpl.class);

    /**
     * 长/短网址转换
     * @param url
     * @param request
     * @return
     */
    @Override
    // transfer （注释开头加" " 空格）
    public UrlVO transfer(String url, HttpServletRequest request)  {
        UrlVO urlVo = new UrlVO();
        //传入网址验证是否为有效长网址 （独立出一个工具类）
        // if非法处理，直接返回，避免缩进
        if (UrlUtil.isShortUrl(url)) {
            String s = url.substring(24);
            String longUrl = shortToLong(s);
            urlVo.setUrl(longUrl);
            return urlVo;
        }
        if (!UrlUtil.isLongUrl(url)){
            //都不是上面的一种，返回格式错误
            logger.error("格式错误");
            return null;
        }
        //先从Redis读取
        String shortExist = (String) redisUtil.get(url);
        redisUtil.expire(url,60);
        if (!StringUtils.isEmpty(shortExist)) {
            // 调用一个常量类型 + short （分不同的环境建立不同域名） application配置文件分环境
            String fullUrl = shortUrlPrefix + shortExist;
            urlVo.setUrl(fullUrl);
            return urlVo;
        }
        //redis中没有，查询MySQL是否有长网址信息
        QueryWrapper<LongToShort> wrapperLong = new QueryWrapper<>();
        wrapperLong.eq("long_url",url);
        LongToShort LongData = baseMapper.selectOne(wrapperLong);
        //有，返回长网址对应短网址
        if (LongData != null) {
            String longUrlMeta = LongData.getLongUrl();
            String shortUrlMeta = LongData.getShortUrl();
            redisUtil.setLongAndShort(longUrlMeta, shortUrlMeta, 60);
            String fullUrl = shortUrlPrefix + shortUrlMeta;
            urlVo.setUrl(fullUrl);
            return urlVo;
        }
        //没有，存放long_to_short信息
        String ipAddr = IpUtil.getIpAddr(request);
        LongToShort longToShort = new LongToShort();
        String shortUrl = RandomUtil.BaseTrans(url);
        //查询短网址是否重复，不重复存放，重复重新生成
        QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        LongToShort one = baseMapper.selectOne(wrapper);
        if (one == null) {
            //存放 redis
            // longUrl:shortUrl  shortUrl:longUrl  shortUrlSum:sum
            // redis操作抽取出来
            redisUtil.setLongAndShort(url, shortUrl, 60);
            executor.execute(() -> {
                longToShort.setLongUrl(url);
                longToShort.setShortUrl(shortUrl);
                longToShort.setFromIp(ipAddr);
                baseMapper.insert(longToShort);
                System.out.println(Thread.currentThread().getName());
            });
            String fullUrl = shortUrlPrefix + shortUrl;
            urlVo.setUrl(fullUrl);
            return urlVo;
        }
        else {
            transfer(url,request);
        }
        return null;
    }

    /**
     * 短网址转长网址
     * @param shortUrl
     * @return
     */
    @Override
    public String shortToLong(String shortUrl) {
        String longUrl = (String) redisUtil.get(shortUrl);
        redisUtil.expire(shortUrl, 60);
        if(!StringUtils.isEmpty(longUrl)){
            return longUrl;
        }
        QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        LongToShort one = baseMapper.selectOne(wrapper);
        longUrl = one.getLongUrl();
        redisUtil.set(shortUrl, longUrl, 60);
        return longUrl;
    }

}
