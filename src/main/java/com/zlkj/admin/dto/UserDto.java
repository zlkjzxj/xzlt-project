package com.zlkj.admin.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
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
public class UserDto {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer roleId;
    private String name;
    private String userName;
    private Integer company;
    private String avatar;
    private String sign;

    private String zw;
    private String zwms;

}
