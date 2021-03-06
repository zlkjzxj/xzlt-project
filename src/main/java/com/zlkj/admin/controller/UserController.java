package com.zlkj.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.IUserService;
import com.zlkj.admin.util.Constant;
import com.zlkj.admin.util.PasswordEncoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private IUserService iUserService;

    @RequestMapping("/*")
    public void toHtml() {
        System.out.println("userController /*");
    }

    @RequestMapping("/unlock")
    @ResponseBody
    public ResultInfo<Boolean> unlock(@RequestParam("password") String password) {
        UserInfo userInfo = this.getUserInfo();
        SimpleHash simpleHash = new SimpleHash("md5", password, userInfo.getCredentialsSalt(), 2);
        if (simpleHash.toString().equals(userInfo.getPassword())) {
            return new ResultInfo<>(true);
        }
        return new ResultInfo<>(false);
    }

    @RequestMapping("/listData")
    @RequiresPermissions("user:view")
    public @ResponseBody
    ResultInfo<List<User>> listData(User user, Integer page, Integer limit) {
        EntityWrapper<User> wrapper = new EntityWrapper<>(user);
        if (user != null && user.getUserName() != null) {
            wrapper.like("user_name", user.getUserName());
            user.setUserName(null);
        }
        if (user != null && user.getName() != null) {
            wrapper.like("name", user.getName());
            user.setName(null);
        }
        if (user != null && user.getCompany() != null) {
            wrapper.eq("company", user.getCompany());
            user.setCompany(null);
        }
        Page<User> pageObj = iUserService.selectPage(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @RequestMapping("/listDataSelect")
//    @RequiresPermissions("user:view")
    public @ResponseBody
    ResultInfo<List<User>> listDataSelect(User user) {
        EntityWrapper<User> wrapper = new EntityWrapper<>(user);
        if (user != null && user.getCompany() != null) {
            wrapper.eq("company", user.getCompany());
            user.setCompany(null);
        }
        wrapper.eq("is_show", 1);
        List<User> userList = iUserService.selectList(wrapper);
        return new ResultInfo<>(userList);
    }

    @SysLog("添加用户操作")
    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public @ResponseBody
    ResultInfo<Boolean> add(@RequestParam("avatarFile") MultipartFile avatarFile, User user) {
        User oldUser = iUserService.findUserInfo(user.getUserName());
        if (oldUser != null) {
            return new ResultInfo<>("此登录名称已经存在！");
        }
        String avatarStr = "";
        if (avatarFile != null) {
            try {
                avatarStr = Base64Utils.encodeToString(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            user.setAvatar(Constant.DEFAULT_AVATAR);
        }
        if (avatarStr != "") {
            user.setAvatar(Constant.BASE64_PIC_HEADER + avatarStr);
        }
        Map<String, String> map = PasswordEncoder.enCodePassWord(user.getUserName(), user.getPassword());
        user.setIsShow(1);
        user.setSalt(map.get(PasswordEncoder.SALT));
        user.setPassword(map.get(PasswordEncoder.PASSWORD));
        boolean b = iUserService.insert(user);
        return new ResultInfo<>(b);
    }

    @SysLog("删除用户操作")
    @RequestMapping("/delBatch")
    @RequiresPermissions("user:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer[] idArr) {
        boolean b = iUserService.deleteBatchIds(Arrays.asList(idArr));
        return new ResultInfo<>(b);
    }

    @SysLog("修改用户操作")
    @RequestMapping("/edit")
    @RequiresPermissions("user:edit")
    public @ResponseBody
    ResultInfo<Boolean> edit(@RequestParam("avatarFile") MultipartFile avatarFile, User user) {
        User us = iUserService.selectById(user.getId());
        us.setName(user.getName());
        us.setUserName(user.getUserName());
        us.setRoleId(user.getRoleId());
        us.setState(user.getState());
        us.setCompany(user.getCompany());
        us.setSign(user.getSign());
        String avatarStr = "";
        if (avatarFile != null) {
            try {
                avatarStr = Base64Utils.encodeToString(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (avatarStr != "") {
            us.setAvatar(Constant.BASE64_PIC_HEADER + avatarStr);
        }
        boolean b = iUserService.updateById(us);
        return new ResultInfo<>(b);
    }

    @SysLog("本人修改用户操作")
    @RequestMapping("/userEdit")
    public @ResponseBody
    ResultInfo<Boolean> userEdit(@RequestParam("avatarFile") MultipartFile avatarFile, User user) {
        UserInfo userInfo = this.getUserInfo();
        User us = iUserService.selectById(userInfo.getId());
        String avatarStr = "";
        if (avatarFile != null) {
            try {
                avatarStr = Base64Utils.encodeToString(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(user.getName())) {
            us.setName(user.getName());
        }
        if (!StringUtils.isEmpty(user.getPassword())) {
            Map<String, String> map = PasswordEncoder.enCodePassWord(us.getUserName(), user.getPassword());
            us.setSalt(map.get(PasswordEncoder.SALT));
            us.setPassword(map.get(PasswordEncoder.PASSWORD));
        }
        if (avatarStr != "") {
            us.setAvatar(Constant.BASE64_PIC_HEADER + avatarStr);
        }
        boolean b = iUserService.updateById(us);
        Session session = SecurityUtils.getSubject().getSession();
        if (b && !"".equals(avatarStr)) {
            session.setAttribute("avatar", Constant.BASE64_PIC_HEADER + avatarStr);
        }
        if (b && !StringUtils.isEmpty(user.getName())) {
            session.setAttribute("userName", userInfo.getName());
        }
        return new ResultInfo<>(b);
    }

    @SysLog("本人修改用户密码")
    @RequestMapping("/userPassEdit")
    public @ResponseBody
    ResultInfo<Boolean> userPassEdit(User user) {
        UserInfo userInfo = this.getUserInfo();
        User us = iUserService.selectById(userInfo.getId());
        if (!StringUtils.isEmpty(user.getPassword())) {
            Map<String, String> map = PasswordEncoder.enCodePassWord(us.getUserName(), user.getPassword());
            us.setSalt(map.get(PasswordEncoder.SALT));
            us.setPassword(map.get(PasswordEncoder.PASSWORD));
        }
        boolean b = iUserService.updateById(us);
        return new ResultInfo<>(b);
    }

    @SysLog("重置密码")
    @RequestMapping("/resetPass")
    public @ResponseBody
    ResultInfo<Boolean> resetPass(User user) {
        User us = iUserService.selectById(user.getId());
        if (us != null) {
            Map<String, String> map = PasswordEncoder.enCodePassWord(us.getUserName(), Constant.DEFAULT_PASS);
            us.setSalt(map.get(PasswordEncoder.SALT));
            us.setPassword(map.get(PasswordEncoder.PASSWORD));
            boolean b = iUserService.updateById(us);
            return new ResultInfo<>(b);
        }
        return new ResultInfo<>("重置密码失败");
    }

    @RequestMapping("/centerDate")
    public @ResponseBody
    ResultInfo<UserInfo> centerDate() {
        UserInfo userInfo = this.getUserInfo();
        BeanUtils.copyProperties(iUserService.selectById(userInfo.getId()), userInfo);
        return new ResultInfo<>(userInfo);
    }


    /**
     * 查询用户数量
     */
    @RequestMapping("/count")
    public @ResponseBody
    ResultInfo<Integer> count() {
        return new ResultInfo<>(iUserService.selectCount(new EntityWrapper<>()));
    }

}