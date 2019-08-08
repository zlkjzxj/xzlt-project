package com.zlkj.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Data
@TableName("sys_role")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 企业代号
	 */
	@TableField("enterprise_id")
	private String enterpriseId;

    /**
     * 权限ID列表
     */
    @TableField("permission_ids")
	private String permissionIds;
    /**
     * 是否可用
     */
	private Integer available;
    /**
     * 角色名称
     */
    @TableField("role_name")
	private String roleName;
    /**
     * 角色编号
     */
    @TableField("role_code")
	private String roleCode;
    /**
     * 修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
}
