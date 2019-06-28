package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@TableName("biz_tc")
@Data
public class Tc {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("code")
    private String code;
    /**
     * 项目编号
     */
    @TableField("fwtd")
    private String fwtd;
    /**
     * 服务特点
     */
    @TableField("td")
    private String td;
    /**
     * 顾问背景
     */
    @TableField("gwbj")
    private String gwbj;
    /**
     * 工作流程
     */
    @TableField("gzlc")
    private String gzlc;
    /**
     * 总顾问背景
     */
    @TableField("zgwbj")
    private String zgwbj;
    /**
     * 总工作流程
     */
    @TableField("zgzlc")
    private String zgzlc;
}
