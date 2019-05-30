package com.zlkj.business.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 企业实体类
 * @Author sunny
 * @Date 2019-03-07 14:10
 */
@Data
@NoArgsConstructor
public class EnterpriseDto {
    private String name;
    private String grade;
    private String qyrs;
    private String clsj;
    private String qyxz;
    private String zyyt;
    private String jyzt;
    private String ltdjed;
    private String gzqy;
    private String gltxjqcd;
    private String nrylsl;
    private String jxry;
    private String yqbzq;
    private String zltsl;
    private String ldjf;
    private String swjf;
    private String xytc;
    private String qtjfx;
    private Integer pjzf;

}
