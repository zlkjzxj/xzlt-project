package com.zlkj.business.entity;

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
@TableName("biz_enterprise")
public class Enterprise {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 企业名称
     */
    @TableField("name")
    private String name;
    /**
     * 企业编号
     */
    @TableField("number")
    private String number;
    /**
     * 企业负责人
     */
    @TableField("manager")
    private String manager;
    /**
     * 联系人电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 企业描述
     */
    @TableField("desc")
    private String desc;
    /**
     * 企业评分
     */
    @TableField("grade")
    private String grade;
    /**
     * 企业人数
     */
    @TableField("qyrs")
    private String qyrs;
    /**
     * 成立时间
     */
    @TableField("clsj")
    private String clsj;
    /**
     * 企业性质
     */
    @TableField("qyxz")
    private String qyxz;
    /**
     * 主营业态
     */
    @TableField("zyyt")
    private String zyyt;
    /**
     * 经营状态
     */
    @TableField("jyzt")
    private String jyzt;
    /**
     * 猎头定金额度
     */
    @TableField("ltdjed")
    private String ltdjed;
    /**
     * 工作区域
     */
    @TableField("gzqy")
    private String gzqy;
    /**
     * 管理体系健全程度
     */
    @TableField("gltxjqcd")
    private String gltxjqcd;
    /**
     * 年人员流失率
     */
    @TableField("nrylsl")
    private String nrylsl;
    /**
     * 奖项荣誉
     */
    @TableField("jxry")
    private String jxry;
    /**
     * 要求保质期
     */
    @TableField("yqbzq")
    private String yqbzq;
    /**
     * 找猎头数量
     */
    @TableField("zltsl")
    private String zltsl;
    /**
     * 劳动纠纷
     */
    @TableField("ldjf")
    private String ldjf;
    /**
     * 商务纠纷
     */
    @TableField("swjf")
    private String swjf;
    /**
     * 选用套餐
     */
    @TableField("xytc")
    private String xytc;
    /**
     * 其他加分项（隐藏后台）
     */
    @TableField("qtjfx")
    private String qtjfx;
    /**
     * 企业评价总分
     */
    @TableField("pjzf")
    private Integer pjzf;
    /**
     * 企业logo
     */
    @TableField("logo")
    private String logo;
    /**
     * 企业二维码
     */
    @TableField("qrcode")
    private String qrcode;
    /**
     * 企业二维码
     */
    @TableField("appcp")
    private Integer appcp;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Ignore
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Ignore
    private Date updateTime;

}
