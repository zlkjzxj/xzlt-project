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
@TableName("biz_project_progress")
@Data
public class ProjectProgress {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonIgnore
    private Integer id;
    /**
     * 项目Id
     */
    @TableField("project_id")
    @JsonIgnore
    private Integer projectId;
    /**
     * 进度条value
     */
    @TableField("progress_value")
    private Integer progressValue;
    /**
     * 对应进度
     */
    @TableField("progress")
    private String progress;
    /**
     * 进度时间
     */
    @TableField(value = "time")
    private String time;
}
