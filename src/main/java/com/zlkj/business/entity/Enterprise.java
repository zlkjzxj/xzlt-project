package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 企业实体类
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
@TableName("biz_enterprise")
public class Enterprise {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 企业名称
     */
    @TableField("name")
    private String name;
    /**
     * 企业编号
     */
    @TableField("number")
    private String number;
    /**
     * 企业负责人
     */
    @TableField("manager")
    private String manager;
    /**
     * 联系人电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 企业描述
     */
    @TableField("desc")
    private String desc;
    /**
     * 企业评分
     */
    @TableField("grade")
    private String grade;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

}
