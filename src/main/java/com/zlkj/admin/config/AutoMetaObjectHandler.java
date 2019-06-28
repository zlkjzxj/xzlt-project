package com.zlkj.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 公共字段自动填充
 *
 * @author sunny
 */

public class AutoMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
        setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
    }

}