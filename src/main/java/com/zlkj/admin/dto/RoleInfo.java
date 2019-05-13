package com.zlkj.admin.dto;


import com.zlkj.admin.entity.Permission;
import com.zlkj.admin.entity.Role;

import java.io.Serializable;
import java.util.List;

public class RoleInfo extends Role implements Serializable {

    private List<Permission> permissions;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}