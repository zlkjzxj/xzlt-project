package com.zlkj.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Data
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 企业代号
     */
    @TableField("enterprise_id")
    private String enterpriseId;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 密码
     */
    @TableField("password")
    @JsonIgnore
    private String password;
    /**
     * 电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 所属公司
     */
    @TableField("company")
    private Integer company;
    /**
     * 盐值
     */
    @TableField("salt")
    @JsonIgnore
    private String salt;
    /**
     * 状态(0：禁用，1：启用，2：锁定)
     */
    @TableField("state")
    private Integer state;
    /**
     * 是否显示(0：否，1：是)
     */
    @TableField("is_show")
    private Integer isShow;
    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;
    /**
     * 用户签名
     */
    @TableField("sign")
    private String sign;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", company=" + company +
                ", salt='" + salt + '\'' +
                ", state=" + state +
                ", avatar='" + avatar + '\'' +
                ", sign='" + sign + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
