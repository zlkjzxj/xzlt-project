package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.Department;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.IDepartmentService;
import com.zlkj.admin.service.IUserService;
import com.zlkj.admin.util.Constant;
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
        QueryWrapper<Project> projectEntityWrapper = new QueryWrapper<>();
        projectEntityWrapper.eq("company", project.getCompany());
        if (!StringUtils.isEmpty(project.getSearchVal())) {
            projectEntityWrapper.like("name", project.getSearchVal());
        }
        List<Project> list = iProjectService.list(projectEntityWrapper);
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
            QueryWrapper<ProjectProgress> projectDtoWrapper = new QueryWrapper<>();
            projectDtoWrapper.eq("project_id", pro.getId());
            List<ProjectProgress> progressList = iProjectProgressService.list(projectDtoWrapper);
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
        QueryWrapper<Project> wrapper = new QueryWrapper<>(newProject);
        if (project != null && project.getNumber() != null) {
            wrapper.eq("number", project.getNumber());
            newProject.setNumber(null);
        }
        //判断此项目编号是否存在
        Project oldProject = iProjectService.getOne(wrapper);
        LocalDate today = LocalDate.now();
        if (oldProject == null) {
            UserInfo user = this.getUserInfo();
            project.setLrr(user.getId());
            String progress = project.getProgress().replace("{", "").replace("}", "").replace("\"", "");
            boolean b = iProjectService.save(project);
            String[] progress_s = new String[]{};
            if (!"".equals(progress)) {
                progress_s = progress.split(",");
            }
            for (int i = 0; i < progress_s.length; i++) {
                ProjectProgress projectProgress = new ProjectProgress();
                projectProgress.setProjectId(project.getId());
                String jd = progress_s[i];
                projectProgress.setProgressValue(Integer.parseInt(jd.split(":")[0]));
                projectProgress.setProgress(jd.split(":")[1]);
                //进度不等于0 的给设定时间
                if (!"0".equals(jd.split(":")[1])) {
                    projectProgress.setTime(today.toString());
                }
                iProjectProgressService.save(projectProgress);
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

        Project oldProject = iProjectService.getById(project.getId());

        if (oldProject != null) {
            //获取当前登录用户id
            LocalDate today = LocalDate.now();
            String progress = project.getProgress().replace("{", "").replace("}", "").replace("\"", "");
            boolean b = iProjectService.updateById(project);
            String[] progress_s = new String[]{};
            if (!"".equals(progress)) {
                progress_s = progress.split(",");
            }
            QueryWrapper<ProjectProgress> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("project_id", project.getId());
            List<ProjectProgress> list = iProjectProgressService.list(wrapper1);
            List oldList = new ArrayList();
            List newList = new ArrayList();
            for (ProjectProgress p : list) {
                oldList.add(p.getProgressValue());
            }
            System.out.println(progress_s.length);
            for (int i = 0; i < progress_s.length; i++) {
                newList.add(Integer.parseInt(progress_s[i].split(":")[0]));
            }
            Map map = compareSameAndNot(oldList, newList);
            List<Integer> addList = (List) map.get("add");
            List<Integer> sameList = (List) map.get("same");
            List<Integer> delList = (List) map.get("del");
            //新增的添加
            for (Integer add : addList) {
                ProjectProgress projectProgress = new ProjectProgress();
                projectProgress.setProjectId(project.getId());
                for (String a : progress_s) {
                    if ((a.split(":")[0]).equals(add + "")) {
                        projectProgress.setProgressValue(add);
                        projectProgress.setProgress(a.split(":")[1]);
                        //进度不等于0 的给设定时间
                        if (!"0".equals(a.split(":")[1])) {
                            projectProgress.setTime(today.toString());
                        }
                        iProjectProgressService.save(projectProgress);
                    }

                }
            }
//            //不变的修改
            for (Integer same : sameList) {
                QueryWrapper<ProjectProgress> wrapper = new QueryWrapper<>();
                wrapper.eq("project_id", project.getId());
                wrapper.eq("progress_value", same);
                ProjectProgress progress1 = iProjectProgressService.getOne(wrapper);
                if (progress1 != null) {
                    for (String a : progress_s) {
                        if ((a.split(":")[0]).equals(same + "")) {
                            progress1.setProgress(a.split(":")[1]);
                            String time = progress1.getTime();
                            if (time == null && !"0".equals(a.split(":")[1])) {
                                progress1.setTime(today.toString());
                            }
                        }
                    }

                    iProjectProgressService.updateById(progress1);
                }
            }
            //删除的删除
            for (Integer del : delList) {
                QueryWrapper<ProjectProgress> wrapper = new QueryWrapper<>();
                wrapper.eq("project_id", project.getId());
                wrapper.eq("progress_value", del);
                iProjectProgressService.remove(wrapper);
            }
           /* for (int i = 0; i < progress_s.length; i++) {
                String jd = progress_s[i];
                //进度不等于0 的给查出来修改进度，并判断修改时间
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

            }*/
            return new ResultInfo<>("0", "追加成功", b);
        }

        return new ResultInfo<>("-1", "修改错误！");
    }

    @SysLog("删除项目操作")
    @RequestMapping("/del")
    @RequiresPermissions("project:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        boolean b = iProjectService.removeById(id);
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

    public static void main(String[] args) {
        System.out.println("".length());
//集合一
//        List oldList = new ArrayList();
//        oldList.add("2");
//        oldList.add("3");
//        oldList.add("4");
//        //集合二
//        List newList = new ArrayList();
//        newList.add("1");
//        newList.add("2");
//        newList.add("5");
//
//        Map map = compareSameAndNot(oldList, newList);
//        System.out.println(map.get("add"));
//        System.out.println(map.get("same"));
//        System.out.println(map.get("del"));
    }

    public static Map compareSameAndNot(List oldList, List newList) {
        Collection existsa = new ArrayList(newList);
        Collection notexistsa = new ArrayList(newList);
        Collection existsb = new ArrayList(oldList);
        Collection notexistsb = new ArrayList(oldList);
        existsa.removeAll(oldList);
        notexistsa.removeAll(existsa);
        notexistsb.removeAll(newList);
        Map map = new HashMap();
        map.put("add", existsa);
        map.put("same", notexistsa);
        map.put("del", notexistsb);
        return map;
    }
}