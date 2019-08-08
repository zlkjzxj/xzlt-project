package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.IUserService;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.entity.Project;
import com.zlkj.business.service.IEnterpriseService;
import com.zlkj.business.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sunny
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @Resource
    private IEnterpriseService iEnterpriseService;
    @Resource
    private IProjectService iProjectService;
    @Autowired
    private IUserService iUserService;

    @RequestMapping("/*")
    public void toHtml() {
    }

    @RequestMapping("/listDataSelect")
    public @ResponseBody
    ResultInfo<List<Enterprise>> listDataSelect(Integer page, Integer limit) {
        UserInfo userInfo = this.getUserInfo();
        //查处所有人员
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(userInfo);
        userQueryWrapper.eq("enterprise_id", userInfo.getEnterpriseId());
        IPage pageObj = iUserService.page(new Page(page, limit), userQueryWrapper);
        //根据人员查项目
        for (Object u : pageObj.getRecords()) {
            QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
            projectQueryWrapper.eq("manager", ((User) u).getId());
        }
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @RequestMapping("/getUser")
    public @ResponseBody
    ResultInfo<List<UserInfo>> getUser() {
        //查处所有人员
        UserInfo userInfo = this.getUserInfo();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("id", "user_name", "name", "role_id", "enterprise_id", "phone");
        userQueryWrapper.eq("enterprise_id", userInfo.getEnterpriseId());
        List<User> list = iUserService.list(userQueryWrapper);
        List<UserInfo> infoList = new ArrayList<>();
        list.forEach(a -> {
            QueryWrapper<Project> wrapper = new QueryWrapper<>();
            wrapper.eq("manager", a.getId());
            int count = iProjectService.count(wrapper);
            UserInfo info = new UserInfo();
            info.setId(a.getId());
            info.setUserName(a.getUserName());
            info.setName(a.getName());
            info.setRoleId(a.getRoleId());
            info.setPhone(a.getPhone());
            info.setProjectCount(count);
            infoList.add(info);

        });
        return new ResultInfo<>(infoList);
    }

    @RequestMapping("/getProjectByUser")
    public @ResponseBody
    ResultInfo<List<ProjectInfo>> getProjectByUser(Integer manager) {
        QueryWrapper<Project> queryWrapper = new QueryWrapper();
        queryWrapper.eq("manager", manager);
        List<Project> list = iProjectService.list(queryWrapper);
        List<ProjectInfo> infoList = new ArrayList<>();
        for (Project pro : list) {
            User xmjl = iUserService.getById(pro.getManager());
            ProjectInfo info = new ProjectInfo();
            info.setId(pro.getId());
            info.setName(pro.getName());
            info.setNumber(pro.getNumber());
            info.setManagerName(xmjl.getName());
            info.setCompany(pro.getCompany());
            info.setManager(pro.getManager());
            info.setLxsj(pro.getLxsj());
            info.setJssj(pro.getJssj());
            info.setGrade(pro.getGrade());
            info.setContacts(pro.getContacts());
            info.setPhone(pro.getPhone());
            info.setMembers(pro.getMembers());
            String memberStr = pro.getMembers().replace("\"", "").replace("}", "").replace("{", "");
            String[] members = memberStr.split(",");
            String memstr = "";
            for (int i = 0; i < members.length; i++) {
                String memberstr = members[i];
                User cy = iUserService.getById(memberstr.split(":")[0]);
                if (cy != null) {
                    memstr += cy.getName() + ",";
                }
            }
            if (!"".equals(memstr)) {
                info.setMembersName(memstr.substring(0, memstr.length() - 1));
            }
            infoList.add(info);
        }

        return new ResultInfo<>(infoList);
    }

    @RequestMapping("/getProjectInfo")
    public @ResponseBody
    ResultInfo<ProjectInfo> getProjectInfo(String number) {
        ProjectInfo info = iProjectService.findProjectbyId(number);
        return new ResultInfo<>(info);
    }

}