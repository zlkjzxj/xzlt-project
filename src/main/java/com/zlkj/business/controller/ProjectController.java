package com.zlkj.business.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserDto;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.Department;
import com.zlkj.admin.entity.Permission;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.IDepartmentService;
import com.zlkj.admin.service.IUserService;
import com.zlkj.admin.util.Constant;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.business.dto.ProjectCountInfo;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.entity.Project;
import com.zlkj.business.entity.ProjectProgress;
import com.zlkj.business.service.IProjectProgressService;
import com.zlkj.business.service.IProjectService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @author sunny
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Resource
    private IProjectService iProjectService;
    @Resource
    private IDepartmentService iDepartmentService;
    @Resource
    private IProjectProgressService iProjectProgressService;
    @Resource
    private IUserService iUserService;

    @RequestMapping("/*")
    public void toHtml() {
    }

    @Override
    protected UserInfo getUserInfo() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    @RequestMapping("/listData")
//    @RequiresPermissions("project:view")
    public @ResponseBody
    ResultInfo<List<ProjectInfo>> listData(ProjectInfo project, Integer page, Integer limit) {
//        if (page != null && limit != null) {
//            project.setLimit1(limit * (page - 1));
//            project.setLimit2(limit);
//        }
//        List<ProjectInfo> projectInfoList = iProjectService.findProjectByFuzzySearchVal(project);
//        int count;
//        count = iProjectService.selectCount(null);
//        return new ResultInfo<>(projectInfoList, count);
        EntityWrapper<Project> projectEntityWrapper = new EntityWrapper<>();
        projectEntityWrapper.eq("company", project.getCompany());
        List<Project> list = iProjectService.selectList(projectEntityWrapper);
        List<ProjectInfo> infoList = new ArrayList<>();
        for (Project pro : list) {
            User xmjl = iUserService.selectById(pro.getManager());
            ProjectInfo info = new ProjectInfo();
            info.setId(pro.getId());
            info.setName(pro.getName());
            info.setManagerName(xmjl.getName());
            info.setCompany(pro.getCompany());
            info.setManager(pro.getManager());
            info.setLxsj(pro.getLxsj());
            info.setGrade(pro.getGrade());
            info.setContacts(pro.getContacts());
            info.setPhone(pro.getPhone());
            info.setMembers(pro.getMembers());
            String memberStr = pro.getMembers().replace("\"", "").replace("}", "").replace("{", "");
            String[] members = memberStr.split(",");
            String memstr = "";
            for (int i = 0; i < members.length; i++) {
                String memberstr = members[i];
                User cy = iUserService.selectById(memberstr.split(":")[0]);
                if (cy != null) {
                    memstr += cy.getName() + ",";
                }
            }
            if (!"".equals(memstr)) {
                info.setMembersName(memstr.substring(0, memstr.length() - 1));
            }
            EntityWrapper<ProjectProgress> projectDtoWrapper = new EntityWrapper<>();
            projectDtoWrapper.eq("project_id", pro.getId());
            List<ProjectProgress> progressList = iProjectProgressService.selectList(projectDtoWrapper);
            info.setProgress(progressList);
            infoList.add(info);
        }
        return new ResultInfo<>(infoList);
    }

//    @RequestMapping("/getObject")
////    @RequiresPermissions("project:view")
//    public @ResponseBody
//    ResultInfo<ProjectInfo> getObject(Integer id) {
//        if (id != null) {
//            ProjectInfo project = iProjectService.findProjectbyId(id);
//            return new ResultInfo<>(project);
//        }
//        return new ResultInfo<>("-1", "查无数据");
//
//    }


    @SysLog("添加项目")
    @RequestMapping("/add")
    @RequiresPermissions("project:add")
    @Transactional
    public @ResponseBody
    ResultInfo<Boolean> add(Project project) {
        Project newProject = new Project();
        EntityWrapper<Project> wrapper = new EntityWrapper<>(newProject);
        if (project != null && project.getNumber() != null) {
            wrapper.eq("number", project.getNumber());
            newProject.setNumber(null);
        }
        //判断此项目编号是否存在
        Project oldProject = iProjectService.selectOne(wrapper);
        LocalDate today = LocalDate.now();
        if (oldProject == null) {
            UserInfo user = this.getUserInfo();
            project.setLrr(user.getId());
            String progress = project.getProgress().replace("{", "").replace("}", "").replace("\"", "");
            boolean b = iProjectService.insert(project);
            String[] progress_s = progress.split(",");
            for (int i = 0; i < progress_s.length; i++) {
                ProjectProgress projectProgress = new ProjectProgress();
                projectProgress.setProjectId(project.getId());
                String jd = progress_s[i];
                projectProgress.setProgressValue(jd.split(":")[0]);
                projectProgress.setProgress(jd.split(":")[1]);
                if (!"0".equals(jd.split(":")[1])) {
                    projectProgress.setTime(today.toString());
                }
                iProjectProgressService.insert(projectProgress);
            }
            return new ResultInfo<>("0", "添加成功", b);
        }
        return new ResultInfo<>("-1", "重复添加");
    }

    @SysLog("修改项目")
    @RequestMapping("/edit")
    @RequiresPermissions("project:edit")
    @Transactional
    public @ResponseBody
    ResultInfo<Boolean> update(Project project) {

        Project oldProject = iProjectService.selectById(project.getId());

        //判断修改人是否一致
        if (oldProject != null) {
            //获取当前登录用户id
            LocalDate today = LocalDate.now();
            String progress = project.getProgress().replace("{", "").replace("}", "").replace("\"", "");
            boolean b = iProjectService.updateById(project);
            String[] progress_s = progress.split(",");
            for (int i = 0; i < progress_s.length; i++) {
                String jd = progress_s[i];
                if (!"0".equals(jd.split(":")[1])) {
                    EntityWrapper<ProjectProgress> wrapper = new EntityWrapper<>();
                    wrapper.eq("project_id", project.getId());
                    wrapper.eq("progress_value", jd.split(":")[0]);
                    ProjectProgress progress1 = iProjectProgressService.selectOne(wrapper);
                    if (progress1 != null) {
                        if (!progress1.getProgress().equals(jd.split(":")[1])) {
                            progress1.setProgress(jd.split(":")[1]);
                        }
                        String time = progress1.getTime();
                        if (time == null) {
                            progress1.setTime(today.toString());
                        }
                        iProjectProgressService.updateById(progress1);
                    }

                }

            }
            return new ResultInfo<>("0", "追加成功", b);
        }

        return new ResultInfo<>("-1", "修改错误！");
    }

    @SysLog("删除项目操作")
    @RequestMapping("/del")
    @RequiresPermissions("project:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        boolean b = iProjectService.deleteById(id);
        return new ResultInfo<>(b);
    }

    /**
     * 前台图表统计
     *
     * @return
     */
    @RequestMapping("/getProjectCountByDepartment")
    public @ResponseBody
    ResultInfo<Map<String, List>> getProjectCountByDepartment() {
        List<Department> departmentList = iDepartmentService.findDepartmentHasNOChildren();
        List<ProjectCountInfo> projectCountInfoList = iProjectService.getProjectCountByDepartment();
        Map<String, List> map = new HashMap<>(2);
        map.put("departments", departmentList);
        map.put("projects", projectCountInfoList);
        return new ResultInfo<>(map);
    }

    @SysLog("获取项目编号")
    @RequestMapping("/getAddSequence")
    @RequiresPermissions("project:add")
    public @ResponseBody
    ResultInfo<String> getAddSequence(String year) {
        String sequence = iProjectService.getAddSequence(year);
        Integer s;
        if (sequence == null) {
            //初始值
            s = 001;
        } else {
            s = Integer.parseInt(sequence) + 1;
        }

        String s1 = Constant.PROJECT_NUMBER_PREFIX + year + String.format("%03d", s);
        return new ResultInfo<>("1", "查询序列成功", s1);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}