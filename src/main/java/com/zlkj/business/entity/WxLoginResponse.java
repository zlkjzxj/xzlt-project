package com.zlkj.business.entity;

import lombok.Data;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-06-13 01:01
 */
@Data
public class WxLoginResponse {
    private String session_key;
    private String openid;
    private Integer errcode;
    private String errmsg;
}
