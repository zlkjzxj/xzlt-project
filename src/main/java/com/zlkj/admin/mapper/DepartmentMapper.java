package com.zlkj.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zlkj.admin.entity.Department;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> findDepartmentHasNOChildren();
}
