package com.zlkj.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.entity.Enterprise;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IEnterpriseService extends IService<Enterprise> {
    /**
     * 根据查询条件查询数据
     * @param enterprise
     * @return
     */
    List<Enterprise> selectListBySearchVar(EnterpriseDto enterprise);
}
