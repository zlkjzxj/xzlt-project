package com.zlkj.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-03-08 16:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_department")
public class Department extends Model<Department> {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部门名称
     */
    @TableField("bmmc")
    private String bmmc;
    /**
     * 状态(0：禁用，1:启用)
     */
    @TableField("isshow")
    private Integer isshow;
    /**
     * 父ID
     */
    @TableField("pid")
    private Integer pid;
    /**
     * 父ID
     */
    @TableField("manager")
    private Integer manager;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Department(Integer id, String bmmc, Integer isshow, Integer pid, Integer manager) {
        this.id = id;
        this.bmmc = bmmc;
        this.isshow = isshow;
        this.pid = pid;
        this.manager = manager;
    }
}
