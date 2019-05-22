package com.zlkj.business.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer qyrs;
    /**
     * 成立时间
     */
    @TableField("clsj")
    private Integer clsj;
    /**
     * 企业性质
     */
    @TableField("qyxz")
    private Integer qyxz;
    /**
     * 主营业态
     */
    @TableField("zyyt")
    private Integer zyyt;
    /**
     * 经营状态
     */
    @TableField("jyzt")
    private Integer jyzt;
    /**
     * 猎头定金额度
     */
    @TableField("ltdjed")
    private Integer ltdjed;
    /**
     * 工作区域
     */
    @TableField("gzqy")
    private Integer gzqy;
    /**
     * 管理体系健全程度
     */
    @TableField("gltxjqcd")
    private Integer gltxjqcd;
    /**
     * 年人员流失率
     */
    @TableField("nrylsl")
    private Integer nrylsl;
    /**
     * 奖项荣誉
     */
    @TableField("jxry")
    private Integer jxry;
    /**
     * 要求保质期
     */
    @TableField("yqbzq")
    private Integer yqbzq;
    /**
     * 找猎头数量
     */
    @TableField("zltsl")
    private Integer zltsl;
    /**
     * 劳动纠纷
     */
    @TableField("ldjf")
    private Integer ldjf;
    /**
     * 商务纠纷
     */
    @TableField("swjf")
    private Integer swjf;
    /**
     * 选用套餐
     */
    @TableField("xytc")
    private Integer xytc;
    /**
     * 其他加分项（隐藏后台）
     */
    @TableField("qtjfx")
    private Integer qtjfx;
    /**
     * 企业评价总分
     */
    @TableField("pjzf")
    private Integer pjzf;
    /**
     * 企业二维码
     */
    @TableField("qrcode")
    private String qrcode;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

}
