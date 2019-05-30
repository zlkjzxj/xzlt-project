package com.zlkj.admin.dto;

import com.zlkj.business.dto.TcDto;
import com.zlkj.business.entity.Tc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 系统code表
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto {
    private String name;
    private String codeName;
    private String code;
    private Integer codeValue;
    private String codeDesc;
    private Integer codeMark;
    private String codeIcon;
    private Tc cpTc;
}
