package com.zlkj.admin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zlkj.admin.dto.PermissionInfo;
import com.zlkj.admin.entity.Permission;

import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<PermissionInfo> allPermissionInfo();

}
