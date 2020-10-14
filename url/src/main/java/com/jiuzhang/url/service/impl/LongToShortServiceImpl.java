package com.jiuzhang.url.service.impl;

import com.jiuzhang.url.domain.LongToShort;
import com.jiuzhang.url.repo.LongToShortRepository;
import com.jiuzhang.url.service.LongToShortService;
import com.jiuzhang.url.utils.IpUtil;
import com.jiuzhang.url.utils.RandomUtil;
import com.jiuzhang.url.utils.UrlUtil;
import com.jiuzhang.url.vo.UrlVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @auther: WZ
 * @Date: 2020/9/7 14:58
 * @Description:
 */
@Service
public class LongToShortServiceImpl implements LongToShortService {

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private LongToShortRepository longToShortRepository;

    @Autowired
    private RedisService redisService;

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
    @Transactional
    public UrlVO transfer(String url, HttpServletRequest request)  {
        UrlVO urlVo = new UrlVO();
        // 传入网址验证是否为有效长网址 （独立出一个工具类）
        // if非法处理，直接返回，避免缩进
        if (UrlUtil.isShortUrl(url)) {
            String s = url.substring(24);
            String longUrl = shortToLong(s);
            urlVo.setUrl(longUrl);
            return urlVo;
        }
        if (!UrlUtil.isLongUrl(url)){
            // 都不是上面的一种，返回格式错误
            logger.error("格式错误");
            return null;
        }
        // 先从Redis读取
        String shortExist = (String) redisService.get(url);
        redisService.expire(url,60);
        if (!StringUtils.isEmpty(shortExist)) {
            // 调用一个常量类型 + short （分不同的环境建立不同域名） application配置文件分环境
            String fullUrl = shortUrlPrefix + shortExist;
            urlVo.setUrl(fullUrl);
            return urlVo;
        }
        // redis中没有，查询MySQL是否有长网址信息
        /*QueryWrapper<LongToShort> wrapperLong = new QueryWrapper<>();
        wrapperLong.eq("long_url",url);
        LongToShort LongData = baseMapper.selectOne(wrapperLong);*/

        Optional<LongToShort> LongDataOpt = longToShortRepository.findByLongUrl(url);

        // 有，返回长网址对应短网址
        if (LongDataOpt.isPresent()) {
            String longUrlMeta = LongDataOpt.get().getLongUrl();
            String shortUrlMeta = LongDataOpt.get().getShortUrl();
            redisService.setLongAndShort(longUrlMeta, shortUrlMeta, 60);
            String fullUrl = shortUrlPrefix + shortUrlMeta;
            urlVo.setUrl(fullUrl);
            return urlVo;
        }
        // 没有，存放long_to_short信息
        String ipAddr = IpUtil.getIpAddr(request);
        LongToShort longToShort = new LongToShort();
        String shortUrl = RandomUtil.BaseTrans(url);
        // 查询短网址是否重复，不重复存放，重复重新生成
        /*
        QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        LongToShort one = baseMapper.selectOne(wrapper);*/

        Optional<LongToShort> shortUrlOptional = longToShortRepository.findByShortUrl(shortUrl);

        if (!shortUrlOptional.isPresent()) {
            // 存放 redis
            // longUrl:shortUrl  shortUrl:longUrl  shortUrlSum:sum
            // redis操作抽取出来
            redisService.setLongAndShort(url, shortUrl, 60);
            executor.execute(() -> {
                longToShort.setLongUrl(url);
                longToShort.setShortUrl(shortUrl);
                //longToShort.setFromIp(ipAddr);
                //baseMapper.insert(longToShort);

                longToShortRepository.save(longToShort);

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
        String longUrl = (String) redisService.get(shortUrl);
        redisService.expire(shortUrl, 60);
        if(!StringUtils.isEmpty(longUrl)){
            return longUrl;
        }
        /*QueryWrapper<LongToShort> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        LongToShort one = baseMapper.selectOne(wrapper);*/

        Optional<LongToShort> longUrlOptional = longToShortRepository.findByShortUrl(shortUrl);

        if(longUrlOptional.isPresent()) {
            longUrl =  longUrlOptional.get().getLongUrl();
            redisService.set(shortUrl, longUrl, 60);
        }
        else {
            longUrl = null;
        }


        return longUrl;
    }

}
