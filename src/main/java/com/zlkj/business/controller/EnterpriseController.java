package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.util.Constant;
import com.zlkj.admin.util.ImageConstant;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.entity.Project;
import com.zlkj.business.service.IEnterpriseService;
import com.zlkj.business.service.IProjectService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static com.zlkj.admin.util.AppUtils.createZImgPath;

/**
 * @author sunny
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/enterprise")
public class EnterpriseController extends BaseController {

    @Resource
    private IEnterpriseService iEnterpriseService;
    @Resource
    private IProjectService iProjectService;

    @RequestMapping("/*")
    public void toHtml() {
    }

    @Override
    protected UserInfo getUserInfo() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    @SysLog("获取企业列表")
    @RequestMapping("/listData")
    @RequiresPermissions("enterprise:view")
    public @ResponseBody
    ResultInfo<List<Enterprise>> listData(EnterpriseDto enterprise, Integer page, Integer limit) {
        QueryWrapper<Enterprise> enterpriseEntityWrapper = new QueryWrapper<>();
        UserInfo user = this.getUserInfo();
        enterpriseEntityWrapper.eq("lrr", user.getId());
        if (!StringUtils.isEmpty(enterprise.getSearchVal())) {
            enterpriseEntityWrapper.like("name", enterprise.getSearchVal())
                    .or().like("manager", enterprise.getSearchVal())
                    .or().like("phone", enterprise.getSearchVal());
        }
        IPage pageObj = iEnterpriseService.page(new Page(page, limit), enterpriseEntityWrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @RequestMapping("/listDataSelect")
    public @ResponseBody
    ResultInfo<List<Enterprise>> listDataSelect(EnterpriseDto enterprise) {
        UserInfo user = this.getUserInfo();
        enterprise.setLrr(user.getId());
        List<Enterprise> list = iEnterpriseService.selectListBySearchVar(enterprise);
        return new ResultInfo<>(list);
    }

    @RequestMapping("/getObject")
    @RequiresPermissions("enterprise:view")
    public @ResponseBody
    ResultInfo<Enterprise> getObject(Integer id) {
        if (id != null) {
            Enterprise enterprise = iEnterpriseService.getById(id);
            return new ResultInfo<>(enterprise);
        }
        return new ResultInfo<>("-1", "查无数据");

    }

    @SysLog("添加企业")
    @RequestMapping("/add")
    @RequiresPermissions("enterprise:edit")
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
        UserInfo user = this.getUserInfo();
        enterprise.setLrr(user.getId());
        enterprise.setAppcp(0);
        boolean b = iEnterpriseService.save(enterprise);
        return new ResultInfo<>(b);
    }
/*
    @SysLog("添加企业")
    @RequestMapping("/add")
    @RequiresPermissions("enterprise:edit")
    public @ResponseBody
    ResultInfo<Boolean> add(@RequestParam("logoFile") MultipartFile logoFile, Enterprise enterprise) {
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
        enterprise.setAppcp(0);
        boolean b = iEnterpriseService.save(enterprise);
        return new ResultInfo<>(b);
    }
*/

    @SysLog("修改企业")
    @RequestMapping("/edit")
    @RequiresPermissions("enterprise:edit")
    @Transactional
    public @ResponseBody
    ResultInfo<Boolean> update(@RequestParam("logoFile") MultipartFile logoFile, Enterprise enterprise) {
        //获取旧的企业信息
        Enterprise enterprise1 = iEnterpriseService.getById(enterprise.getId());
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
                //删除旧的logo
                String oldFilePath = ImageConstant.IMG_PATH + enterprise1.getLogo().replace(ImageConstant.URL, "");
                File oldFile = new File(oldFilePath);
                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.delete();
                }
                //保存二维码
                String qrStr = enterprise.getQrcode();
                if (StringUtils.isNotEmpty(qrStr)) {
                    qrStr = qrStr.split(",")[1];
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes1 = decoder.decodeBuffer(qrStr);
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
                    //删除旧的二维码
                    String oldFilePath1 = ImageConstant.IMG_PATH + enterprise1.getQrcode().replace(ImageConstant.URL, "");
                    File oldFile1 = new File(oldFilePath1);
                    if (oldFile1.exists() && oldFile1.isFile()) {
                        oldFile1.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            enterprise.setLogo(enterprise1.getLogo());
            enterprise.setQrcode(enterprise1.getQrcode());
        }
        //判断企业负责人和联系电话的变更，然后变更项目里面的
        if (StringUtils.isNotEmpty(enterprise.getManager()) && !enterprise1.getManager().equals(enterprise.getManager())) {
            QueryWrapper<Project> projectEntityWrapper = new QueryWrapper<>();
            projectEntityWrapper.eq("company", enterprise1.getId());
            List<Project> list = iProjectService.list(projectEntityWrapper);
            for (Project project : list) {
                project.setContacts(enterprise.getManager());
                iProjectService.saveOrUpdate(project);
            }
        }
        if (StringUtils.isNotEmpty(enterprise.getPhone()) && !enterprise1.getPhone().equals(enterprise.getPhone())) {
            QueryWrapper<Project> projectEntityWrapper = new QueryWrapper<>();
            projectEntityWrapper.eq("company", enterprise1.getId());
            List<Project> list = iProjectService.list(projectEntityWrapper);
            for (Project project : list) {
                project.setPhone(enterprise.getPhone());
                iProjectService.saveOrUpdate(project);
            }
        }
        boolean b = iEnterpriseService.updateById(enterprise);
        return new ResultInfo<>(b);
    }
/*
    @SysLog("修改企业")
    @RequestMapping("/edit")
    @RequiresPermissions("enterprise:edit")
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
*/

    @SysLog("删除企业")
    @RequestMapping("/del")
    @RequiresPermissions("enterprise:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        boolean b = iEnterpriseService.removeById(id);
        return new ResultInfo<>(b);
    }

    public static void main(String[] args) {
        String str = Constant.DEFAULT_AVATAR;
        str = str.replace(Constant.BASE64_PIC_HEADER, "");
        System.out.println(str);
    }
}