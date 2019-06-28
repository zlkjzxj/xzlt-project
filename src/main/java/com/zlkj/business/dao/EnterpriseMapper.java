package com.zlkj.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {
    List<Enterprise> getListBySearchVar(EnterpriseDto enterprise);
}
