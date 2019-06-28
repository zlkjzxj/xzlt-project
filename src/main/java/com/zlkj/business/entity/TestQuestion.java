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
@TableName("test_question")
@Data
public class TestQuestion {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("test_type_id")
    private String testTypeId;
    /**
     * 服务特点
     */
    @TableField("question_name")
    private String questionName;
    /**
     * 顾问背景
     */
    @TableField("question_answer")
    private String questionAnswer;
    /**
     * 工作流程
     */
    @TableField("question_mark")
    private String questionMark;
}
