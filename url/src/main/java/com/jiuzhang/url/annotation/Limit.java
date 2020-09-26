package com.jiuzhang.url.annotation;

import com.jiuzhang.url.common.LimitType;

import java.lang.annotation.*;

/**
 * @author 黄文镇
 * @Date: 2020/9/13 17:31
 * @Description: 改成RateLimiter
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {
    /**
     * 名字
     */
    String name() default "";

    /**
     * key
     */
    String key() default "";

    /**
     * Key的前缀
     */
    String prefix() default "";

    /**
     * 每秒放入令牌桶个数
     * 可以改成float或者double
     */
    int speed();
    /**
     * 一定时间内最多访问次数
     */
    int count();

    /**
     * 限流的类型(用户自定义key或者请求ip)
     */
    LimitType limitType() default LimitType.CUSTOMER;
}
