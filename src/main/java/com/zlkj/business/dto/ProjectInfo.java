package com.zlkj.business.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zlkj.admin.dto.UserDto;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.User;
import com.zlkj.business.entity.ProjectProgress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
public class ProjectInfo {
    private Integer id;
    private String name;
    private String number;
    private Integer company;
    private Integer manager;
    private String members;
    private List<ProjectProgress> progress;

    private String grade;
    private String contacts;
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lxsj;

    /**
     * 翻译字段
     */
    private String managerName;
    private String membersName;
    /**
     * 分页用的参数
     */
    @JsonIgnore
    private Integer limit1;
    @JsonIgnore
    private Integer limit2;
    @JsonIgnore
    private Integer count;

    /**
     * 排序的条件和顺序
     */
    @JsonIgnore
    private String field;
    @JsonIgnore
    private String order;

}
