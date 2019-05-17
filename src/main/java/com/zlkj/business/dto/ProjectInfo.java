package com.zlkj.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lxsj;
    private Integer manager;
    private String members;
    private Integer company;
    private String grade;
    private String contacts;
    private String phone;
    private String progress;
    private String qrcode;
    private Integer lrr;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

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
