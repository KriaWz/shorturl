package com.jiuzhang.url.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: WZ
 * @Date: 2020/7/29 04:00
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class GlobalExecptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.ofFail(500,"执行了全局异常处理");
    }

//    @ExceptionHandler(CommonException.class)
//    @ResponseBody
//    public Result error(CommonException e){
//        log.error(e.getMessage());
//        e.printStackTrace();
//        return Result.ofThrowable();
//    }

}
