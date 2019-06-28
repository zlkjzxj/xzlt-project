package com.zlkj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Role;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.IRoleService;
import com.zlkj.admin.service.IUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private IRoleService iRoleService;

    @Resource
    private IUserService iUserService;

    @RequestMapping("/*")
    public void toHtml() {
        System.out.println("roleController /*");
    }

    @RequestMapping("/selectListData")
    @ResponseBody
    public ResultInfo<List<Role>> selectListData(Role role) {
        List<Role> list = iRoleService.list(new QueryWrapper<>(role));
        return new ResultInfo<>(list);
    }

    @RequestMapping("/listData")
    @RequiresPermissions("role:view")
    public @ResponseBody
    ResultInfo<List<Role>> listData(Role role, Integer page, Integer limit) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>(role);
        if (role != null && role.getRoleCode() != null) {
            wrapper.like("role_code", role.getRoleCode());
            role.setRoleCode(null);
        }
        if (role != null && role.getRoleName() != null) {
            wrapper.like("role_name", role.getRoleName());
            role.setRoleName(null);
        }
        IPage<Role> pageObj = iRoleService.page(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @SysLog("保存角色操作")
    @RequestMapping("/save")
    @RequiresPermissions(value = {"role:add", "role:edit"}, logical = Logical.OR)
    public @ResponseBody
    ResultInfo<Boolean> save(Role role) {
        return new ResultInfo<>(iRoleService.saveRole(role));
    }

    @SysLog("删除角色操作")
    @RequestMapping("/delBatch")
    @RequiresPermissions("role:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer[] idArr) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("role_id", idArr);
        List<User> userList = iUserService.list(wrapper);
        if (userList != null && userList.size() > 0) {
            return new ResultInfo<>("用户拥有角色不能删除！");
        }
        return new ResultInfo<>(iRoleService.removeByIds(Arrays.asList(idArr)));
    }

}