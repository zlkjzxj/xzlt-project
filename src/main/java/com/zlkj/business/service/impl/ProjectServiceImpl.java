package com.zlkj.business.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zlkj.business.dto.ProjectCountInfo;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.entity.Project;
import com.zlkj.business.dao.ProjectMapper;
import com.zlkj.business.service.IProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public ProjectInfo findProjectbyId(String number) {
        return projectMapper.findProjectById(number);
    }

    @Override
    public List<ProjectInfo> findProjectByFuzzySearchVal(ProjectInfo project) {
        return projectMapper.findProjectByFuzzySearchVal(project);
    }

    @Override
    public String getAddSequence(String year) {
        return projectMapper.getAddSequence(year);
    }

    @Override
    public Integer getProjectCount(ProjectInfo project) {
        return projectMapper.getProjectCount(project);
    }

    @Override
    public List<ProjectCountInfo> getProjectCountByDepartment() {
        return projectMapper.getProjectCountByDepartment();
    }

}