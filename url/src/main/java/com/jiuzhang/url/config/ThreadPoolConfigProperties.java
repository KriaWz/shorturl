package com.jiuzhang.url.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther: WZ
 * @Date: 2020/9/9 15:28
 * @Description:
 */
@ConfigurationProperties(prefix = "tinyurl.thread")
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;

}
