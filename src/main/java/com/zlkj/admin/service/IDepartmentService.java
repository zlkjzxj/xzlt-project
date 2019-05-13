package com.zlkj.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.zlkj.admin.entity.Department;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IDepartmentService extends IService<Department> {

    List<Integer> getAllChildrenDepartment(int id);

    List<Department> findDepartmentHasNOChildren();
}
