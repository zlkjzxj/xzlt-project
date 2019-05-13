package com.zlkj.admin.dto;

import lombok.Data;

/**
 * @Description app调用实体类
 * @Author sunny
 * @Date 2019-05-13 16:03
 */
@Data
public class AppParam<T> {
    private String appId;
    private String appSecret;
    private String appToken;
    private T param;
}
