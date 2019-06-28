package com.zlkj.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlkj.admin.entity.Code;

import java.util.List;

/**
 * @author Administrator
 */
public interface CodeMapper extends BaseMapper<Code> {
    List<Code> getCodeType(Integer codeType);
}