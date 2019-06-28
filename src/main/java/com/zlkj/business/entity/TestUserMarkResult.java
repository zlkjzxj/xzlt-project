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
@TableName("test_user_mark_result")
@Data
public class TestUserMarkResult {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 项目编号
     */
    @TableField("question_type_id")
    @JsonIgnore
    private String questionTypeId;
    /**
     * 服务特点
     */
    @TableField("code")
    private String code;
    /**
     * 顾问背景
     */
    @TableField("value")
    private String value;
}
