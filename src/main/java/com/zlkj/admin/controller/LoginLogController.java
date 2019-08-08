package com.zlkj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.LoginLog;
import com.zlkj.admin.service.ILoginLogService;
import com.zlkj.admin.util.FormatUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统登录日志表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-10-01
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    @Resource
    private ILoginLogService iloginLogService;

    @RequestMapping("/*")
    public void toHtml() {

    }

    @RequestMapping("/listData")
    @RequiresPermissions("loginLog:view")
    public @ResponseBody
    ResultInfo<List<LoginLog>> listData(String userName, String loginTime, Integer page, Integer limit) {
        LoginLog loginLog = new LoginLog();
        if (!StringUtils.isEmpty(userName)) {
            loginLog.setUserName(userName);
        }
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>(loginLog);
        wrapper.eq("enterprise_id", this.getUserInfo().getEnterpriseId());
        if (!StringUtils.isEmpty(loginTime)) {
            wrapper.ge("create_time", FormatUtil.parseDate(loginTime.split(" - ")[0] + " 00:00:00", null));
            wrapper.le("create_time", FormatUtil.parseDate(loginTime.split(" - ")[1] + " 23:59:59", null));
        }
        wrapper.orderBy(false, false, new String[]{"create_time"});
        IPage<LoginLog> pageObj = iloginLogService.page(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

}
