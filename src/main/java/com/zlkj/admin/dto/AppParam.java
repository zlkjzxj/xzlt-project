package com.zlkj.admin.dto;

import lombok.Data;

/**
 * @Description app调用实体类
 * @Author sunny
 * @Date 2019-05-13 16:03
 */
@Data
public class AppParam {
    /**
     * 加密后的签名
     */
    private String signature;
    /**
     * 企业ID
     */
    private String enterpriseId;
    /**
     * 调用接口时间戳
     */
    private String timestamp;
    /**
     * 生成二维码时间戳
     */
    private String createTime;
}
