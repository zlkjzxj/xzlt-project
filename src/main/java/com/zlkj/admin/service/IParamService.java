package com.zlkj.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.zlkj.admin.entity.Param;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IParamService extends IService<Param> {
    Boolean saveParam(Param param);
}