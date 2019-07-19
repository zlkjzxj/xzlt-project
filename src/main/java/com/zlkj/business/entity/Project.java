package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
@TableName("biz_project")
public class Project {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("name")
    private String name;
    /**
     * 项目编号
     */
    @TableField("number")
    private String number;
    /**
     * 立项时间
     */
    @TableField(value = "lxsj", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lxsj;

    /**
     * 结束时间
     */
    @TableField(value = "jssj", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date jssj;

    /**
     * 项目经理Id
     */
    @TableField("manager")
    private Integer manager;
    /**
     * 软件开发进度
     */
    @TableField("members")
    private String members;
    /**
     * 所属公司
     */
    @TableField("company")
    private Integer company;
    /**
     * 评分
     */
    @TableField("grade")
    private String grade;
    /**
     * 联系人
     */
    @TableField("contacts")
    private String contacts;
    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 项目进度
     */
    @TableField("progress")
    private String progress;
    /**
     * 项目二维码
     */
    @TableField("qrcode")
    private String qrcode;
    /**
     * 录入人
     */
    @TableField("lrr")
    private Integer lrr;

    /**
     * 创建时间
     */
    @JsonIgnore
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonIgnore
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
}
