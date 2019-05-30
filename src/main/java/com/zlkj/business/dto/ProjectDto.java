package com.zlkj.business.dto;

import com.zlkj.admin.dto.UserDto;
import com.zlkj.business.entity.ProjectProgress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
public class ProjectDto {
    private String name;
    private UserDto manager;
    private List<UserDto> members;
    private List<ProjectProgress> progress;

}
