package com.jiuzhang.url.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @Auther: WZ
 * @Date: 2020/7/30 04:06
 * @Description:
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("gmtCreate", new Date(),metaObject);
//        this.setFieldValByName("gmtModified",new Date(),metaObject);
        this.strictInsertFill(metaObject,"gmtCreate",LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"gmtModified",LocalDateTime.class,LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("gmtModified",new Date(),metaObject);
        this.strictUpdateFill(metaObject,"gmtModified",LocalDateTime.class,LocalDateTime.now());
    }
}
