package com.zlkj.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统code表
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@TableName("sys_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Code extends Model<Code> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 代码统称
     */
    @TableField("name")
    private String name;
    /**
     * 代码名称
     */
    @TableField("code_name")
    private String codeName;
    /**
     * 代码类型
     */
    @TableField("code")
    private String code;
    /**
     * 代码值
     */
    @TableField("code_value")
    private Integer codeValue;
    /**
     * 是否可用
     */
    private Integer available;
    /**
     * 代码描述
     */
    private String codeDesc;
    /**
     * 分值(给评分标准添加的)
     */
    private Integer codeMark;

    /**
     * 修改时间
     */
    @JsonIgnore
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @JsonIgnore
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
