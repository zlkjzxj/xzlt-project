package com.zlkj.business.service;

import com.baomidou.mybatisplus.service.IService;
import com.zlkj.business.dto.ProjectCountInfo;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.entity.Project;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IProjectService extends IService<Project> {
    ProjectInfo findProjectbyId(Integer id);

    List<ProjectInfo> findProjectByFuzzySearchVal(ProjectInfo project);

    String getAddSequence(String year);

    Integer getProjectCount(ProjectInfo project);

    List<ProjectCountInfo> getProjectCountByDepartment();

//    Project findProject(String name);

}
