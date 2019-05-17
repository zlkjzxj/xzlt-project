package com.zlkj.business.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zlkj.admin.dto.AppParam;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.util.AppUtils;
import com.zlkj.admin.util.Constant;
import com.zlkj.admin.util.MD5Utils;
import com.zlkj.business.dto.ProjectInfo;
import com.zlkj.business.service.IProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private ICodeService iCodeService;
    @Resource
    private IProjectService iProjectService;

    @RequestMapping("/getProjectInfo")
    public ResultInfo<Map<String, Object>> getProjectInfo(AppParam appParam) {
        String signature = appParam.getSignature();
        String timestamp = appParam.getTimestamp();
        //获取签名和时间戳，判断是否为空
        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp)) {
            return new ResultInfo<>("-1", "接口参数验证失败");
        }
        //判断请求时间，如果超过一分钟，不让请求
        if (AppUtils.checkTimestamp(timestamp)) {
            return new ResultInfo<>("-1", "接口请求超时");
        }
        //判断二维码是否过期
        if (AppUtils.checkCreateTime(appParam.getCreateTime())) {
            return new ResultInfo<>("-1", "二维码已失效");
        }
        //判断项目id是为为空
        if (StringUtils.isEmpty(appParam.getProjectId())) {
            return new ResultInfo<>("-1", "接口参数验证失败");
        }
        if (AppUtils.checkParam(signature, timestamp)) {
            EntityWrapper<Code> wrapper = new EntityWrapper<>();
            wrapper.eq("code", "projectprogress");
            List<Code> codeList = iCodeService.selectList(wrapper);
            Map<String, Object> map = new HashMap<>();
            ProjectInfo projectInfo = iProjectService.findProjectbyId(appParam.getProjectId());
            map.put("codeList", codeList);
            map.put("projectInfo", projectInfo);
            return new ResultInfo<>(map);
        }
        return new ResultInfo<>("-1", "接口参数验证失败");
    }

}
