package com.zlkj.business.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@Data
public class TcDto {
    private String code;
    private String fwtd;
    private String td;
    private String gwbj;
    private String gzlc;
    private String zgwbj;
    private String zgzlc;
}
