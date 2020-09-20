package com.jiuzhang.url.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @auther: WZ
 * @Date: 2020/9/9 14:53
 * @Description:
 */
@Configuration
public class  MyRedissonConfig {


    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException{
        Config config = new Config();
//        config.useClusterServers()
//                .addNodeAddress("172.81.211.143");
        config.useSingleServer().setAddress("redis://172.81.211.143:6379").setPassword("hwz041597..");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
//        RedissonClient redisson = Redisson.create(
//                Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
//        return redisson;
    }


}
