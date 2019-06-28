package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.service.IUserService;
import com.zlkj.admin.util.Constant;
import com.zlkj.admin.util.ImageConstant;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.entity.Project;
import com.zlkj.business.entity.ProjectProgress;
import com.zlkj.business.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zlkj.admin.util.AppUtils.createZImgPath;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
@RequestMapping("/phone")
public class PhoneController {

    @Resource
    private ICodeService iCodeService;
    @Resource
    private IEnterpriseService iEnterpriseService;
    @Resource
    private IProjectService iProjectService;
    @Resource
    private IUserService iUserService;
    @Resource
    private ITcService iTcService;
    @Resource
    private IProjectProgressService iProjectProgressService;

    @RequestMapping("/*")
    public void toHtml() {
        System.out.println("跳转页面了");
    }

    /**
     * 手机端登录页面
     *
     * @return
     */
    @RequestMapping({"/", "/login"})
    public String toLogin() {
        return "phone/login";
    }

    /**
     * 手机端主页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "phone/list";
    }

    //    /**
//     * 手机端主页
//     *
//     * @param user
//     * @return
//     */
//    @RequestMapping("/index")
//    @ResponseBody
//    public ResultInfo<User> login(User user) {
//        //根据用户名获取用户，然后根据密码加盐，判断是否和查询的密码一样
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.select("user_name", "name", "salt", "password").eq("user_name", user.getUserName());
//        User oldUser = iUserService.getOne(wrapper);
//        if (oldUser != null) {
//            return new ResultInfo<>("-1", "查无用户");
//        }
//        SimpleHash simpleHash = new SimpleHash("md5", user.getPassword(), oldUser.getSalt(), 2);
//        if (simpleHash.toString().equals(oldUser.getPassword())) {
//            return new ResultInfo<>("0", "登录成功");
//        }
//        return new ResultInfo<>("-2", "密码错误");
//    }
    @RequestMapping("/listData")
    public @ResponseBody
    ResultInfo<List<Enterprise>> listData(EnterpriseDto enterprise, Integer page, Integer limit) {
        QueryWrapper<Enterprise> enterpriseEntityWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(enterprise.getSearchVal())) {
            enterpriseEntityWrapper.like("name", enterprise.getSearchVal()).or().like("manager", enterprise.getSearchVal());
        }
        IPage pageObj = iEnterpriseService.page(new Page(page, limit), enterpriseEntityWrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @RequestMapping("/enterpriseAdd")
    public @ResponseBody
    ResultInfo<Boolean> add(@RequestParam("logoFile") MultipartFile logoFile, Enterprise enterprise) {
        if (StringUtils.isNotBlank(logoFile.getOriginalFilename())) {
            byte[] bytes;
            try {
                bytes = logoFile.getBytes();
                // 查看图片文件夹如果不存在就创建
                File file = new File(ImageConstant.IMG_PATH + ImageConstant.ENTERPRISE_AVATAR_PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String imgName = createZImgPath();
                String imgPath = ImageConstant.IMG_PATH + ImageConstant.ENTERPRISE_AVATAR_PATH + imgName;
                File imgFile = new File(imgPath);
                FileOutputStream outStream = new FileOutputStream(imgFile);
                outStream.write(bytes);
                outStream.close();
                enterprise.setLogo(ImageConstant.URL + ImageConstant.ENTERPRISE_AVATAR_PATH + imgName);
                //保存二维码
                String qrStr = enterprise.getQrcode();

                if (StringUtils.isNotEmpty(qrStr)) {
                    //前端转码的时候把前缀加上了
                    qrStr = qrStr.split(",")[1];
                    byte[] bytes1 = Base64.getDecoder().decode(qrStr);
                    File dir = new File(ImageConstant.IMG_PATH + ImageConstant.ENTERPRISE_QRCODE_PATH);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    String imgName1 = createZImgPath();
                    String imgPath1 = ImageConstant.IMG_PATH + ImageConstant.ENTERPRISE_QRCODE_PATH + imgName1;
                    File imgFile1 = new File(imgPath1);
                    FileOutputStream outStream1 = new FileOutputStream(imgFile1);
                    outStream1.write(bytes1);
                    outStream1.close();
                    enterprise.setQrcode(ImageConstant.URL + ImageConstant.ENTERPRISE_QRCODE_PATH + imgName1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            enterprise.setLogo(ImageConstant.URL + ImageConstant.USER_DEFAULT_AVATAR);
        }
        enterprise.setAppcp(0);
        boolean b = iEnterpriseService.save(enterprise);
        return new ResultInfo<>(b);
    }

    @RequestMapping("/enterpriseEdit")
    public @ResponseBody
    ResultInfo<Boolean> update(@RequestParam("logoFile") MultipartFile logoFile, Enterprise enterprise) {
        String logoStr = "";
        if (logoFile != null) {
            try {
                logoStr = Base64Utils.encodeToString(logoFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logoStr != "") {
            enterprise.setLogo(Constant.BASE64_PIC_HEADER + logoStr);
        }
        boolean b = iEnterpriseService.updateById(enterprise);
        return new ResultInfo<>(b);
    }

    @SysLog("删除企业")
    @RequestMapping("/del")
    @RequiresPermissions("enterprise:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        boolean b = iEnterpriseService.removeById(id);
        return new ResultInfo<>(b);
    }

    @RequestMapping("/getProgressAndLevel")
    public @ResponseBody
    ResultInfo<Map> getProgressAndLevel() {
        //进度code
        QueryWrapper<Code> wrapper1 = new QueryWrapper<>();
        //用户职位code
        QueryWrapper<Code> userLevelwrapper = new QueryWrapper<>();
        userLevelwrapper.eq("code", "userlevel");
        List<Code> userLevelCodeList = iCodeService.list(userLevelwrapper);
        wrapper1.eq("code", "projectprogress");
        List<Code> progressCodeList = iCodeService.list(wrapper1);
        Map<String, List<Code>> map = new HashMap<>(2);
        map.put("userlevel", userLevelCodeList);
        map.put("projectprogress", progressCodeList);
        return new ResultInfo<>(map);
    }

    @RequestMapping("/listDataSelect")
    public @ResponseBody
    ResultInfo<List<User>> listDataSelect(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>(user);
        wrapper.select("id", "name", "user_name");
        if (user != null && user.getCompany() != null) {
            wrapper.eq("company", user.getCompany());
            user.setCompany(null);
        }
        wrapper.eq("is_show", 1);
        List<User> userList = iUserService.list(wrapper);
        return new ResultInfo<>(userList);
    }

    @RequestMapping("/getAddSequence")
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

    @RequestMapping("/addProject")
    @Transactional
    public @ResponseBody
    ResultInfo<Boolean> addProject(Project project) {
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
            UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
            if (user != null) {
                project.setLrr(user.getId());
            } else {
                project.setLrr(1);
            }
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

}
