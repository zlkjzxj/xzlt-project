package com.zlkj.admin.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private String phone;

    private String zw;
    private String zwms;

}
