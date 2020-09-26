package com.jiuzhang.url.ascept;

import com.jiuzhang.url.annotation.Limit;
import com.jiuzhang.url.common.LimitType;
import com.jiuzhang.url.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther: WZ
 * @Date: 2020/9/13 17:32
 * @Description: 限流切面，注解编程
 */
@Aspect
@Configuration
public class LimitInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);

    private static final String UNKNOWN = "unknown";

    private final RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    /**
     * @param pjp
     * @author wz
     * @description 切面
     * @date 2020/9/15 13:04
     */
    @Around("execution(public * *(..)) && @annotation(com.jiuzhang.url.annotation.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        // 根据上下文信息提取 HttpRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        double limitSpeed = limitAnnotation.speed();
        BigDecimal bigDecimal = new BigDecimal(limitSpeed);
        System.out.println(bigDecimal);
        int limitCount = limitAnnotation.count();

        /**
         * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
         */
        switch (limitType) {
            case IP:
                key = IpUtil.getIpAddr(request);
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        List<String> keys = new ArrayList<>();
        keys.add(StringUtils.join(limitAnnotation.prefix(), key));
        keys.add(StringUtils.join("LastTime", key));
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        try {
            DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redislimit.lua")));
            redisScript.setResultType(Number.class);
            Number count = limitRedisTemplate.execute(
                redisScript,
                keys,
                bigDecimal.doubleValue(),
                limitCount,
                currentTimeMillis,
                1);
            logger.info("Access try count is {} for name={} and key = {}", count, name, key);
            if (count.intValue() == 1) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("你已经被限制访问了");
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }
}
