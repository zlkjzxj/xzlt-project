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
@TableName("test_code")
@Data
public class TestCode {
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
    @TableField("code")
    private String code;
    /**
     * 服务特点
     */
    @TableField("code_name")
    private String codeName;
    /**
     * 顾问背景
     */
    @TableField("code_value")
    private String codeValue;
    /**
     * 工作流程
     */
    @TableField("code_desc")
    private String codeDesc;
    /**
     * 工作流程
     */
    @TableField("code_desc1")
    private String codeDesc1;
    /**
     * 工作流程
     */
    @TableField("code_desc2")
    private String codeDesc2;
}
