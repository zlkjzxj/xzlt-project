package com.zlkj.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlkj.business.dto.TestUserMarkDto;
import com.zlkj.business.entity.TestUserMark;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface ITestUserMarkService extends IService<TestUserMark> {
    List<TestUserMarkDto> getUserMarkList(Integer userId);
}
