package com.zlkj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlkj.admin.entity.Department;
import com.zlkj.admin.dao.DepartmentMapper;
import com.zlkj.admin.service.IDepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<Integer> getAllChildrenDepartment(int id) {
        Department department = new Department();
        QueryWrapper<Department> wrapper = new QueryWrapper<>(department);
        wrapper.eq("pid", id);
        List<Department> departmentList = departmentMapper.selectList(wrapper);
        List<Integer> ids = new ArrayList<>();
        getChilds(departmentList, ids);
        return ids;
    }

    @Override
    public List<Department> findDepartmentHasNOChildren() {
        return departmentMapper.findDepartmentHasNOChildren();
    }

    public void getChilds(List<Department> list, List<Integer> ids) {
        if (!list.isEmpty()) {
            for (Department d : list) {
                ids.add(d.getId());
                Department department = new Department();
                QueryWrapper<Department> wrapper = new QueryWrapper<>(department);
                wrapper.eq("pid", d.getId());
                List<Department> departmentList = departmentMapper.selectList(wrapper);
                if (!departmentList.isEmpty()) {
                    getChilds(departmentList, ids);
                }
            }

        }
    }
}