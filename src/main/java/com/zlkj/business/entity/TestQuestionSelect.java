package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-06-04 16:52
 */
@TableName("test_question_select")
@Data
public class TestQuestionSelect {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名称
     */
    @TableField("question_id")
    private String questionId;
    /**
     * 服务特点
     */
    @TableField("select_name")
    private String selectName;
    /**
     * 顾问背景
     */
    @TableField("select_value")
    private String selectValue;
    /**
     * 工作流程
     */
    @TableField("select_mark")
    private String selectMark;
}
