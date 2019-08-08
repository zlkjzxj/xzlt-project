package com.zlkj.business.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.zlkj.admin.dto.AppParam;
import com.zlkj.admin.dto.CodeDto;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserDto;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.service.IUserService;
import com.zlkj.admin.util.CodeConstant;
import com.zlkj.admin.util.HttpClientUtil;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.dto.ProjectDto;
import com.zlkj.business.entity.*;
import com.zlkj.business.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.zlkj.admin.util.Constant.APPID;
import static com.zlkj.admin.util.Constant.APPSECRET;
import static com.zlkj.admin.util.Constant.WX_OPENID_URL;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
@RequestMapping("/app")
public class AppController {

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
    @Resource
    private IWxUserService iWxUserService;
    @Resource
    private IWxViewProjectLogService iWxViewProjectLogService;

    /**
     * 小程序扫码后跳转的页面
     *
     * @param appParam
     * @return
     */
    @RequestMapping("/to_index")
    public String index(AppParam appParam, Model model) {
        model.addAttribute("enterpriseId", appParam.getEnterpriseId());
        return "app/index";
    }

    /**
     * 手机端登录页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String appLogin() {
        return "app/login";
    }

    /**
     * 根据公司id查出公司和公司的所有项目
     *
     * @param appParam
     * @return
     */
    @RequestMapping("/getProjectInfo")
    @ResponseBody
    public ResultInfo<Map<String, Object>> getProjectInfo(AppParam appParam) {
        //判断项目id是为为空
        if (StringUtils.isEmpty(appParam.getEnterpriseId())) {
            return new ResultInfo<>("-1", "接口参数验证失败");
        }
        QueryWrapper<Enterprise> enterpriseWrapper = new QueryWrapper<>();
        enterpriseWrapper.eq("number", appParam.getEnterpriseId());

        Enterprise enterprise = iEnterpriseService.getOne(enterpriseWrapper);
        List<Project> projectList;
        List<ProjectDto> projectDtoList = new ArrayList<>();
        if (enterprise != null) {
            enterprise.setQrcode(null);

            QueryWrapper<Project> projectWrapper = new QueryWrapper<>();
            projectWrapper.eq("company", enterprise.getId());
            //查出岗位列表
            projectList = iProjectService.list(projectWrapper);
            for (Project project : projectList) {
                Integer managerId = project.getManager();
                System.out.println(JSON.toJSONString(project));
                String memberStr = project.getMembers();
                String[] members = memberStr.split(",");
                QueryWrapper<User> xmjlWrapper = new QueryWrapper<>();
                xmjlWrapper.select("id", "role_id", "name", "user_name", "phone", "company", "sign", "avatar");
                xmjlWrapper.eq("id", managerId);
                User xmjl = iUserService.getOne(xmjlWrapper);
                System.out.println(xmjl);
                UserDto jl = new UserDto();
                jl.setId(xmjl.getId());
                jl.setName(xmjl.getName());
                jl.setSign(xmjl.getSign());
                jl.setAvatar(xmjl.getAvatar());
                jl.setPhone(xmjl.getPhone());
                for (Code code : CodeConstant.USER_LEVEL_MAP.get(enterprise.getEnterpriseId())) {
                    if ("1".equals(code.getCodeValue() + "")) {
                        jl.setZw(code.getCodeName());
                        jl.setZwms(code.getCodeDesc());
                        break;
                    }
                }
                List<UserDto> xmcy = new ArrayList<>();
                for (int i = 0; i < members.length; i++) {
                    String memberstr = members[i].replace("\"", "").replace("}", "").replace("{", "");
                    if (memberStr.indexOf(":") > -1) {
                        QueryWrapper<User> cyWrapper = new QueryWrapper<>();
                        cyWrapper.select("id", "role_id", "name", "user_name", "phone", "company", "sign", "avatar");
                        cyWrapper.eq("id", memberstr.split(":")[0]);
                        User cy = iUserService.getOne(cyWrapper);
                        UserDto userDto = new UserDto();
                        for (Code code : CodeConstant.USER_LEVEL_MAP.get(enterprise.getEnterpriseId())) {
                            if (memberstr.split(":")[1].equals(code.getCodeValue() + "")) {
                                userDto.setZw(code.getCodeName());
                                userDto.setZwms(code.getCodeDesc());
                                break;
                            }
                        }
                        userDto.setId(cy.getId());
                        userDto.setName(cy.getName());
                        userDto.setSign(cy.getSign());
                        userDto.setAvatar(cy.getAvatar());
                        xmcy.add(userDto);
                    }
                }
                ProjectDto projectInfo = new ProjectDto();
                projectInfo.setName(project.getName());
                projectInfo.setManager(jl);
                projectInfo.setMembers(xmcy);
                QueryWrapper<ProjectProgress> projectDtoWrapper = new QueryWrapper<>();
                projectDtoWrapper.eq("project_id", project.getId());
                List<ProjectProgress> progressList = iProjectProgressService.list(projectDtoWrapper);
                projectInfo.setProgress(progressList);
                projectDtoList.add(projectInfo);
            }

        } else {
            return new ResultInfo<>("-1", "查无此企业");
        }


        Map<String, Object> map = new HashMap<>(5);
        map.put("enterprise", enterprise);

        map.put("progressCodes", CodeConstant.PROGRESSCODE_MAP.get(enterprise.getEnterpriseId()));

        map.put("projectList", projectDtoList);

        //根据企业id给测评code添加gzqy
        List<CodeDto> gzqy = CodeConstant.GZQY_MAP.get(enterprise.getEnterpriseId());
        Map<String, List<CodeDto>> cpCodeMap = CodeConstant.cpCodeMap;
        cpCodeMap.put("gzyq", gzqy);
        map.put("cpCodes", cpCodeMap);

        map.put("managerphone", CodeConstant.PHONE_MAP.get(enterprise.getEnterpriseId()));

        ResultInfo resultInfo = new ResultInfo(map);
        return resultInfo;
    }

    /**
     * get wxuser openid
     *
     * @param
     * @return
     */
    @RequestMapping("/getUserAvatar")
    public void getUserAvatar(HttpServletResponse response, Integer id) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("avatar").eq("id", id);
        User user = iUserService.getOne(userQueryWrapper);
        if (user != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] bytes1 = decoder.decodeBuffer(user.getAvatar().split(",")[1]);

                response.getOutputStream().write(bytes1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 根据公司id查出公司和公司的所有项目
     *
     * @param
     * @return
     */
    @RequestMapping("/updateCpValue")
    @ResponseBody
    public ResultInfo<EnterpriseDto> updateCpValue(@RequestBody Enterprise enterprise) {
        Enterprise enterprise1 = iEnterpriseService.getById(enterprise.getId());
        if (enterprise1 != null) {
            if (0 == enterprise1.getAppcp()) {
                enterprise.setAppcp(1);
                enterprise.setQtjfx("0:0");
                //计算分数
                String qyrs = enterprise.getQyrs().split(":")[1];
                String clsj = enterprise.getClsj().split(":")[1];
                String qyxz = enterprise.getQyxz().split(":")[1];
                String zyyt = enterprise.getZyyt().split(":")[1];
                String jyzt = enterprise.getJyzt().split(":")[1];
                String ltdjed = enterprise.getLtdjed().split(":")[1];
                String gzqy = enterprise.getGzqy().split(":")[1];
                String gltxjqcd = enterprise.getGltxjqcd().split(":")[1];
                String nrylsl = enterprise.getNrylsl().split(":")[1];
                String jxry = enterprise.getJxry().split(":")[1];
                String yqbzq = enterprise.getYqbzq().split(":")[1];
                String zltsl = enterprise.getZltsl().split(":")[1];
                String ldjf = enterprise.getLdjf().split(":")[1];
                String swjf = enterprise.getSwjf().split(":")[1];
                String xytc = enterprise.getXytc().split(":")[1];
                int zf = Integer.parseInt(qyrs) + Integer.parseInt(clsj) + Integer.parseInt(qyxz) + Integer.parseInt(zyyt)
                        + Integer.parseInt(jyzt) + Integer.parseInt(ltdjed) + Integer.parseInt(gzqy) + Integer.parseInt(gltxjqcd)
                        + Integer.parseInt(nrylsl) + Integer.parseInt(jxry) + Integer.parseInt(yqbzq) + Integer.parseInt(zltsl)
                        + Integer.parseInt(ldjf) + Integer.parseInt(swjf) + Integer.parseInt(xytc);
                enterprise.setPjzf(zf);
                for (Code code : CodeConstant.starRatingCodeList) {
                    String value = code.getCodeName();
                    Integer start = Integer.parseInt(value.split("~")[0]);
                    Integer end = Integer.parseInt(value.split("~")[1]);
                    if (zf >= start && zf <= end) {
                        enterprise.setGrade(String.valueOf(code.getCodeMark()));
                        break;
                    }
                }
                boolean b = iEnterpriseService.updateById(enterprise);
                EnterpriseDto dto = new EnterpriseDto();
                dto.setName(enterprise.getName());
                dto.setGrade(enterprise.getGrade());
                dto.setQyrs(enterprise.getQyrs());
                dto.setClsj(enterprise.getClsj());
                dto.setQyxz(enterprise.getQyxz());
                dto.setZyyt(enterprise.getZyyt());
                dto.setJyzt(enterprise.getJyzt());
                dto.setLtdjed(enterprise.getLtdjed());
                dto.setGzqy(enterprise.getGzqy());
                dto.setGltxjqcd(enterprise.getGltxjqcd());
                dto.setNrylsl(enterprise.getNrylsl());
                dto.setJxry(enterprise.getJxry());
                dto.setYqbzq(enterprise.getYqbzq());
                dto.setZltsl(enterprise.getZltsl());
                dto.setLdjf(enterprise.getLdjf());
                dto.setSwjf(enterprise.getSwjf());
                dto.setXytc(enterprise.getXytc());
                return new ResultInfo<>("1001", "保存成功", dto);
            } else {
                EnterpriseDto dto = new EnterpriseDto();
                dto.setName(enterprise1.getName());
                dto.setGrade(enterprise1.getGrade());
                dto.setQyrs(enterprise1.getQyrs());
                dto.setClsj(enterprise1.getClsj());
                dto.setQyxz(enterprise1.getQyxz());
                dto.setZyyt(enterprise1.getZyyt());
                dto.setJyzt(enterprise1.getJyzt());
                dto.setLtdjed(enterprise1.getLtdjed());
                dto.setGzqy(enterprise1.getGzqy());
                dto.setGltxjqcd(enterprise1.getGltxjqcd());
                dto.setNrylsl(enterprise1.getNrylsl());
                dto.setJxry(enterprise1.getJxry());
                dto.setYqbzq(enterprise1.getYqbzq());
                dto.setZltsl(enterprise1.getZltsl());
                dto.setLdjf(enterprise1.getLdjf());
                dto.setSwjf(enterprise1.getSwjf());
                dto.setXytc(enterprise1.getXytc());
                return new ResultInfo<>("1001", "此企业已经测评过", dto);
            }
        }
        return new ResultInfo<>("-1", "查无此企业");
    }

    /**
     * 保存微信用户
     *
     * @param
     * @return
     */
    @RequestMapping("/saveWxUser")
    @ResponseBody
    public ResultInfo<Boolean> saveWxUser(WxUser user) {
        if (!StringUtils.isEmpty(user.getOpenid())) {
            WxUser oldUser = iWxUserService.getOne(new QueryWrapper<WxUser>().eq("openid", user.getOpenid()));
            if (oldUser == null) {
                boolean b = iWxUserService.save(user);
                return new ResultInfo<>(b);
            }
            return new ResultInfo<>("1002", "用户已存在");
        }
        return new ResultInfo<>("1001", "用户openid为空");
    }

    /**
     * 保存查看项目看板日志
     *
     * @param
     * @return
     */
    @RequestMapping("/saveWxViewProjectLog")
    @ResponseBody
    public ResultInfo<Boolean> saveWxViewProjectLog(WxViewProjectLog wxViewProjectLog) {
        boolean b = false;
        if (!StringUtils.isEmpty(wxViewProjectLog.getOpenid())) {
            b = iWxViewProjectLogService.save(wxViewProjectLog);
        }
        return new ResultInfo<>(b);
    }

    /**
     * get wxuser openid
     *
     * @param
     * @return
     */
    @RequestMapping("/getWxUserOpenid")
    @ResponseBody
    public ResultInfo<WxLoginResponse> getWxUserOpenid(String code) {
        Map<String, String> map = new HashMap();
        map.put("appid", APPID);
        map.put("secret", APPSECRET);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

        String result = HttpClientUtil.doPost(WX_OPENID_URL, map, "utf-8");
        WxLoginResponse response = (WxLoginResponse) JSON.parseObject(result, WxLoginResponse.class);
        return new ResultInfo<>(response);
    }
    //    @RequestMapping("/getProjectInfo")
//    public ResultInfo<Map<String, Object>> getProjectInfo(AppParam appParam) {
//        String signature = appParam.getSignature();
//        String timestamp = appParam.getTimestamp();
//        //获取签名和时间戳，判断是否为空
//        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp)) {
//            return new ResultInfo<>("-1", "接口参数验证失败");
//        }
//        //判断请求时间，如果超过一分钟，不让请求
//        if (AppUtils.checkTimestamp(timestamp)) {
//            return new ResultInfo<>("-1", "接口请求超时");
//        }
//        //判断二维码是否过期
//        if (AppUtils.checkCreateTime(appParam.getCreateTime())) {
//            return new ResultInfo<>("-1", "二维码已失效");
//        }
//        //判断项目id是为为空
//        if (StringUtils.isEmpty(appParam.getProjectId())) {
//            return new ResultInfo<>("-1", "接口参数验证失败");
//        }
//        if (AppUtils.checkParam(signature, timestamp)) {
//            QueryWrapper<Code> wrapper = new QueryWrapper<>();
//            wrapper.eq("code", "projectprogress");
//            List<Code> codeList = iCodeService.selectList(wrapper);
//            Map<String, Object> map = new HashMap<>();
//            ProjectInfo projectInfo = iProjectService.findProjectbyId(appParam.getProjectId());
//            map.put("codeList", codeList);
//            map.put("projectInfo", projectInfo);
//            return new ResultInfo<>(map);
//        }
//        return new ResultInfo<>("-1", "接口参数验证失败");
//    }
}
