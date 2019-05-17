package com.zlkj.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zlkj.business.dto.ProjectCountInfo;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.entity.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    /**
     * 根据Id获取项目实体
     *
     * @param number
     * @return
     */
    ProjectInfo findProjectById(String number);

    List<ProjectInfo> findProjectByFuzzySearchVal(ProjectInfo project);

    String getAddSequence(String year);

    Integer getProjectCount(ProjectInfo project);

    List<ProjectCountInfo> getProjectCountByDepartment();
}
