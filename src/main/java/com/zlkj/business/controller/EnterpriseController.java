package com.zlkj.business.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.util.Constant;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.service.IEnterpriseService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author sunny
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/enterprise")
public class EnterpriseController extends BaseController {

    @Resource
    private IEnterpriseService iEnterpriseService;


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
    ResultInfo<List<Enterprise>> listData(Enterprise enterprise, Integer page, Integer limit) {
        EntityWrapper<Enterprise> wrapper = new EntityWrapper<>(enterprise);
        if (enterprise != null && enterprise.getName() != null) {
            wrapper.like("name", enterprise.getName());
            enterprise.setName(null);
        }
        Page<Enterprise> pageObj = iEnterpriseService.selectPage(new Page<>(page, limit));
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @RequestMapping("/listDataSelect")
    public @ResponseBody
    ResultInfo<List<Enterprise>> listDataSelect(Enterprise enterprise) {
        EntityWrapper<Enterprise> wrapper = new EntityWrapper<>(enterprise);
//        if (enterprise != null && enterprise.get != null) {
//            wrapper.eq("company", enterprise.getCompany());
//            user.setCompany(null);
//        }
        List<Enterprise> userList = iEnterpriseService.selectList(wrapper);
        return new ResultInfo<>(userList);
    }

    @RequestMapping("/getObject")
    @RequiresPermissions("enterprise:view")
    public @ResponseBody
    ResultInfo<Enterprise> getObject(Integer id) {
        if (id != null) {
            Enterprise enterprise = iEnterpriseService.selectById(id);
            return new ResultInfo<>(enterprise);
        }
        return new ResultInfo<>("-1", "查无数据");

    }

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
        boolean b = iEnterpriseService.insert(enterprise);
        return new ResultInfo<>(b);
    }

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

    @SysLog("删除企业")
    @RequestMapping("/del")
    @RequiresPermissions("enterprise:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        boolean b = iEnterpriseService.deleteById(id);
        return new ResultInfo<>(b);
    }


}