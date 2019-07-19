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
@TableName("test_user_mark")
@Data
public class TestUserMark {
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
    @TableField("user_id")
    private String userId;
    /**
     * 项目编号
     */
    @TableField("question_type_id")
    private String questionTypeId;
    /**
     * 服务特点
     */
    @TableField("test_result")
    private String testResult;
    /**
     * 顾问背景
     */
    @TableField("test_job")
    private String testJob;
    /**
     * 顾问背景
     */
    @TableField("cp_sign")
    private Integer cpSign;
}
