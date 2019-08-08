package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zlkj.admin.config.ApplicationRunner;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据表 前端控制器
 * </p>
 *
 * @author sunny
 * @since 2018-12-25
 */
@Controller
@RequestMapping("/businesscode")
public class BusinessCodeController extends BaseController {

    @Resource
    private ICodeService iCodeService;

    @RequestMapping("/*")
    public void toHtml() {

    }

    @RequestMapping("/listData")
    public @ResponseBody
    ResultInfo<Map<String, Object>> listData() {
        UserInfo user = this.getUserInfo();
        //评分标准
        Code code1 = new Code();
        code1.setCodeType(2);
        code1.setEnterpriseId(user.getEnterpriseId());
        List<Code> codeList1 = iCodeService.getCodeType(code1);
        //项目进度流程
        Code code2 = new Code();
        code2.setCodeType(4);
        code2.setEnterpriseId(user.getEnterpriseId());
        List<Code> codeList2 = iCodeService.getCodeType(code2);
        Code code3 = new Code();
        code3.setCodeType(5);
        code3.setEnterpriseId(user.getEnterpriseId());
        List<Code> codeList3 = iCodeService.getCodeType(code3);
        Map<String, List<Code>> codeMap = new HashMap<>(3);
        codeMap.put("grade", codeList1);
        codeMap.put("progress", codeList2);
        codeMap.put("other", codeList3);
        return new ResultInfo(codeMap);
    }

    @RequestMapping("/listCodeData")
    public @ResponseBody
    ResultInfo<Map<String, Object>> listCodeData(Code code) {
        UserInfo user = this.getUserInfo();
        QueryWrapper<Code> codeEntityWrapper = new QueryWrapper<>();
        if (!code.getCodeType().equals(0)) {
            codeEntityWrapper.eq("code_mark", code.getCodeType());
        }
        codeEntityWrapper.eq("code", code.getCode()).eq("enterprise_id", user.getEnterpriseId());
        List<Code> codeList = iCodeService.list(codeEntityWrapper);
        return new ResultInfo(codeList);
    }

    @RequestMapping("/updateCodes")
    public @ResponseBody
    @Transactional
    ResultInfo<Boolean> updateCodes(@RequestBody List<Code> codes) {
        UserInfo user = this.getUserInfo();

        if (!codes.isEmpty()) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("code", codes.get(0).getCode());
            map.put("enterprise_id", user.getEnterpriseId());
            iCodeService.removeByMap(map);
            codes.forEach(newCode -> {
                newCode.setCodeType(codes.get(0).getCodeType());
                newCode.setEnterpriseId(user.getEnterpriseId());
            });
            iCodeService.saveBatch(codes);
            //修改完之后更新系统启动加载的参数
            ApplicationRunner applicationRunner = SpringContextUtil.getBean(ApplicationRunner.class);
//            applicationRunner.initCode();
            return new ResultInfo(true);
        }
        return new ResultInfo<>(false);
    }

    @RequestMapping("/listCodeType")
    public @ResponseBody
    ResultInfo<Map<String, Object>> listCodeType(Code code) {
        UserInfo user = this.getUserInfo();
        QueryWrapper<Code> codeEntityWrapper = new QueryWrapper<>();
        codeEntityWrapper.select("distinct code_mark", "code_desc");
        codeEntityWrapper.eq("code", code.getCode()).eq("enterprise_id", user.getEnterpriseId());
        List<Code> codeList = iCodeService.list(codeEntityWrapper);
        return new ResultInfo(codeList);
    }

//
//    /**
//     * 生成代码
//     */
//    @RequestMapping("/generator")
//    public void generator(String tables, HttpServletResponse response) throws IOException {
//        byte[] data = iCodeService.generatorCode(tables.split(","));
//
//        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"code-generator.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());
//    }

}