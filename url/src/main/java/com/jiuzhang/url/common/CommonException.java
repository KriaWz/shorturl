package com.jiuzhang.url.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @auther: WZ
 * @Date: 2020/9/26 11:13
 * @Description:
 */
@Data
@AllArgsConstructor
public class CommonException extends Exception{

    private Integer code;

    private String message;
}
