package com.zlkj.admin.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.*;
import com.zlkj.admin.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-10
 */
@RestController
@RequestMapping("/im")
public class ImController extends BaseController {

    @Resource
    private IUserService iUserService;

    @GetMapping("/getInit")
    public ResultInfo<ImInit> getImInit() {
        Session session = SecurityUtils.getSubject().getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        //获取用户信息
        ImMine imMine = new ImMine();
        imMine.setId(userInfo.getId());
        imMine.setUsername(userInfo.getName());
        imMine.setSign(userInfo.getSign());
        imMine.setStatus("online");
        imMine.setAvatar(userInfo.getAvatar());

        List<ImMine> imMineList = new ArrayList<>();
        User u = new User();
        List<User> userList = iUserService.selectList(new EntityWrapper<>(u));
        userList.forEach(user -> {
            ImMine mine = new ImMine(user.getName(), user.getId(), "online", user.getSign(), user.getAvatar());
            imMineList.add(mine);
        });
        //friend
        ImFriend imFriend = new ImFriend();
        imFriend.setGroupname("中林好友");
        imFriend.setId(1);
        imFriend.setList(imMineList);
        List<ImFriend> imFriendList = new ArrayList<>();
        imFriendList.add(imFriend);
        // grpup
        List<ImGroup> groupList = new ArrayList<>();
        groupList.add(new ImGroup("西安研发部", "101", userInfo.getAvatar()));
        groupList.add(new ImGroup("财务部", "102", userInfo.getAvatar()));
        ImInit imInit = new ImInit();
        imInit.setMine(imMine);
        imInit.setFriend(imFriendList);
        imInit.setGroup(groupList);
        return new ResultInfo(imInit);
    }

    @GetMapping("/getMembers")
    public ResultInfo<ImInit> getMembers(String id) {

        List<ImMine> imMineList = new ArrayList<>();
        User u = new User();
        u.setGlbm(6);
        List<User> userList = iUserService.selectList(new EntityWrapper<>(u));
        userList.forEach(user -> {
            ImMine mine = new ImMine(user.getName(), user.getId(),user.getSign(), user.getAvatar());
            imMineList.add(mine);
        });
        return new ResultInfo(imMineList);
    }

}