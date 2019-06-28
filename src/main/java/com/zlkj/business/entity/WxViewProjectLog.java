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
@TableName("wx_view_project_log")
public class WxViewProjectLog {
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
    @TableField("project_type")
    private String projectType;
    /**
     * 企业编号
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Ignore
    private Date createTime;

}
