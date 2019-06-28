package com.zlkj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.User;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IUserService extends IService<User> {

    UserInfo findUserInfo(String userName);

}
