package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@TableName("test_user")
@Data
public class TestUser {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("user_name")
    private String userName;
    /**
     * 项目编号
     */
    @TableField("user_pass")
//    @JsonIgnore
    private String userPass;
    /**
     * 项目编号
     */
    @TableField("salt")
    @JsonIgnore
    private String salt;
    /**
     * 服务特点
     */
    @TableField("phone")
    private String phone;
    /**
     * 顾问背景
     */
    @TableField("gender")
    private String gender;
    /**
     * 工作流程
     */
    @TableField("age")
    private String age;
    /**
     * 工作流程
     */
    @TableField("birth")
    private String birth;
    /**
     * 工作流程
     */
    @TableField("qualification")
    private String qualification;
    /**
     * 工作流程
     */
    @TableField("station")
    private String station;
    /**
     * 工作流程
     */
    @TableField("school")
    private String school;
    /**
     * 工作流程
     */
    @TableField("id_number")
    private String idNumber;
}
