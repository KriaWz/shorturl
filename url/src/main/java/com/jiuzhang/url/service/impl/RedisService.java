package com.jiuzhang.url.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @auther: WZ
 * @Date: 2020/9/27 13:29
 * @Description:
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setLongAndShort(String longUrl, String shortUrl,long time) {
        redisTemplate.opsForValue().set(longUrl, shortUrl, time, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(shortUrl, longUrl, time, TimeUnit.MINUTES);
        //redisTemplate.opsForValue().set(shortUrl + "sum", 0, 60, TimeUnit.MINUTES);
    }

    public void expire(String key, long time) {
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }


    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}
