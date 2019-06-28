package com.zlkj.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlkj.business.dao.EnterpriseMapper;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.service.IEnterpriseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {
    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Override
    public List<Enterprise> selectListBySearchVar(EnterpriseDto enterprise) {
        return enterpriseMapper.getListBySearchVar(enterprise);
    }
}