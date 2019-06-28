package com.zlkj.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.User;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface UserMapper extends BaseMapper<User> {

    UserInfo findUserInfo(String userName);

}
