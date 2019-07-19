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
@TableName("test_question_type")
@Data
public class TestQuestionType {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("test_type_name")
    private String testTypeName;
    /**
     * 项目编号
     */
    @TableField("test_type_header")
    private String testTypeHeader;
    /**
     * 项目编号
     */
    @TableField("test_type_code")
    private String testTypeCode;
    /**
     * 服务特点
     */
    @TableField("test_question_type")
    private String testQuestionType;
    /**
     * 顾问背景
     */
    @TableField("test_question_type_name")
    private String testQuestionTypeName;
    /**
     * 顾问背景
     */
    @TableField("test_question_result")
    private String testQuestionResult;
    /**
     * 顾问背景
     */
    @TableField("back_img")
    private String backImg;
    /**
     * 顾问背景
     */
    @TableField("result_desc")
    private String resultDesc;
    /**
     * 顾问背景
     */
    @TableField("open_sign")
    private Integer openSign;
    /**
     * 顾问背景
     */
    @TableField("ymlx")
    private Integer ymlx;
}
