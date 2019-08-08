package com.zlkj.admin.dto;


import com.zlkj.admin.entity.User;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class UserInfo extends User implements Serializable {
    private Integer id;
    private RoleInfo roleInfo;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    /**
     * 密码盐.
     *
     * @return
     */
    public String getCredentialsSalt() {
        return this.getUserName() + this.getSalt();
    }
    //重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解

    private Integer projectCount;

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }
}