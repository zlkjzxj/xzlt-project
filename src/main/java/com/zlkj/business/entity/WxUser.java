package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 微信用户
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
@TableName("wx_user")
public class WxUser {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 企业名称
     */
    @TableField("openid")
    private String openid;
    /**
     * 企业编号
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 企业负责人
     */
    @TableField("gender")
    private String gender;
    /**
     * 联系人电话
     */
    @TableField("language")
    private String language;
    /**
     * 企业描述
     */
    @TableField("city")
    private String city;
    /**
     * 企业评分
     */
    @TableField("province")
    private String province;
    /**
     * 企业人数
     */
    @TableField("country")
    private String country;
    /**
     * 用户头像
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Ignore
    private Date createTime;

}
