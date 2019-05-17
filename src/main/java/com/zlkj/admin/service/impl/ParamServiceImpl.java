package com.zlkj.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zlkj.admin.entity.Param;
import com.zlkj.admin.dao.ParamMapper;
import com.zlkj.admin.service.IParamService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Service
public class ParamServiceImpl extends ServiceImpl<ParamMapper, Param> implements IParamService {
    @Override
    public Boolean saveParam(Param param) {
        Boolean res;
        if (param.getId() == null) {
            res = this.insert(param);
        } else {
            res = this.updateById(param);
        }
        return res;
    }
}