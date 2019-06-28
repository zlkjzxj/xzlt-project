package com.zlkj.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlkj.business.dao.TestUserMarkMapper;
import com.zlkj.business.dto.TestUserMarkDto;
import com.zlkj.business.entity.TestUserMark;
import com.zlkj.business.service.ITestUserMarkService;
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
public class TestUserMarkServiceImpl extends ServiceImpl<TestUserMarkMapper, TestUserMark> implements ITestUserMarkService {

    @Resource
    private TestUserMarkMapper testUserMarkMapper;

    @Override
    public List<TestUserMarkDto> getUserMarkList(Integer userId) {
        return testUserMarkMapper.getUserMarkList(userId);
    }
}