package com.zlkj.business.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zlkj.admin.dto.AppParam;
import com.zlkj.admin.dto.CodeDto;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserDto;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.User;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.service.IUserService;
import com.zlkj.business.dto.EnterpriseDto;
import com.zlkj.business.dto.ProjectDto;
import com.zlkj.business.entity.Enterprise;
import com.zlkj.business.entity.Project;
import com.zlkj.business.entity.ProjectProgress;
import com.zlkj.business.entity.Tc;
import com.zlkj.business.service.IEnterpriseService;
import com.zlkj.business.service.IProjectProgressService;
import com.zlkj.business.service.IProjectService;
import com.zlkj.business.service.ITcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;


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

    /**
     * 小程序扫码后跳转的页面
     *
     * @param appParam
     * @return
     */
    @RequestMapping("/to_index")
    public String index(AppParam appParam) {
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
//        EntityWrapper<Enterprise> enterpriseWrapper = new EntityWrapper<>();
//        enterpriseWrapper.eq("id", appParam.getProjectId());

        Enterprise enterprise = iEnterpriseService.selectById(appParam.getEnterpriseId());
        List<Project> projectList;
        List<ProjectDto> projectDtoList = new ArrayList<>();
        if (enterprise != null) {
            enterprise.setQrcode(null);
            //用户职位code
            EntityWrapper<Code> userLevelwrapper = new EntityWrapper<>();
            userLevelwrapper.eq("code", "userlevel");
            List<Code> userLevelCodeList = iCodeService.selectList(userLevelwrapper);
//            //公司code
//            EntityWrapper<Code> campanywrapper = new EntityWrapper<>();
//            userLevelwrapper.eq("code", "userlevel");
//            List<Code> campanyCodeList = iCodeService.selectList(campanywrapper);

            EntityWrapper<Project> projectWrapper = new EntityWrapper<>();
            projectWrapper.eq("company", enterprise.getId());
            //查出岗位列表
            projectList = iProjectService.selectList(projectWrapper);
            for (Project project : projectList) {
                Integer managerId = project.getManager();

                String memberStr = project.getMembers();
                String[] members = memberStr.split(",");
                User xmjl = iUserService.selectById(managerId);
                UserDto jl = new UserDto();
                jl.setName(xmjl.getName());
                jl.setSign(xmjl.getSign());
                jl.setAvatar(xmjl.getAvatar());
                for (Code code : userLevelCodeList) {
                    if ("1".equals(code.getCodeValue() + "")) {
                        jl.setZw(code.getCodeName());
                        jl.setZwms(code.getCodeDesc());
                        break;
                    }
                }
                List<UserDto> xmcy = new ArrayList<>();
                for (int i = 0; i < members.length; i++) {
                    String memberstr = members[i].replace("\"", "").replace("}", "").replace("{", "");
                    User cy = iUserService.selectById(memberstr.split(":")[0]);
                    UserDto userDto = new UserDto();
                    for (Code code : userLevelCodeList) {
                        if (memberstr.split(":")[1].equals(code.getCodeValue() + "")) {
                            userDto.setZw(code.getCodeName());
                            userDto.setZwms(code.getCodeDesc());
                            break;
                        }
                    }
                    userDto.setName(cy.getName());
                    userDto.setSign(cy.getSign());
                    userDto.setAvatar(cy.getAvatar());
                    xmcy.add(userDto);
                }
                ProjectDto projectInfo = new ProjectDto();
                projectInfo.setName(project.getName());
                projectInfo.setManager(jl);
                projectInfo.setMembers(xmcy);
                EntityWrapper<ProjectProgress> projectDtoWrapper = new EntityWrapper<>();
                projectDtoWrapper.eq("project_id", project.getId());
                List<ProjectProgress> progressList = iProjectProgressService.selectList(projectDtoWrapper);
                projectInfo.setProgress(progressList);
                projectDtoList.add(projectInfo);
            }

        } else {
            return new ResultInfo<>("-1", "查无此企业");
        }
        //测评code qyrs,clsj,qyxz,zyyt,jyzt,ltdjed,gzqy,gltxjqcd,nrylsl,jxry,yqbzq,zltsl,ldjf,swjf,xytc,qtjfx,starrating

        String[] cpcodes = new String[]{"qyrs", "clsj", "qyxz", "zyyt", "jyzt", "ltdjed", "gzqy", "gltxjqcd", "nrylsl", "jxry",
                "yqbzq", "zltsl", "xytc", "ldjf", "swjf"};

        Map<String, List<CodeDto>> cpCodeMap = new LinkedHashMap<>();
        for (int i = 0; i < cpcodes.length; i++) {
            EntityWrapper<Code> wrapper = new EntityWrapper<>();
            wrapper.eq("code", cpcodes[i]);
            List<Code> codeList = iCodeService.selectList(wrapper);
            List<CodeDto> dtoList = new ArrayList<>();
            for (Code code : codeList) {
                CodeDto dto = new CodeDto();
                dto.setName(code.getName());
                dto.setCodeName(code.getCodeName());
                dto.setCode(code.getCode());
                dto.setCodeValue(code.getCodeValue());
                dto.setCodeDesc(code.getCodeDesc());
                dto.setCodeMark(code.getCodeMark());
                dto.setCodeIcon(code.getCodeIcon());
                if (code.getCpTc() != null) {
                    Tc tc = iTcService.selectById(code.getCpTc());
                    dto.setCpTc(tc);
                }
                dtoList.add(dto);
            }
            cpCodeMap.put(cpcodes[i], dtoList);
        }
        //进度code
        EntityWrapper<Code> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("code", "projectprogress");
        List<Code> progressCodeList = iCodeService.selectList(wrapper1);
        Map<String, Object> map = new HashMap<>();
        map.put("enterprise", enterprise);
        map.put("progressCodes", progressCodeList);
        map.put("projectList", projectDtoList);
        map.put("cpCodes", cpCodeMap);
        ResultInfo resultInfo = new ResultInfo(map);
        return resultInfo;
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
        Enterprise enterprise1 = iEnterpriseService.selectById(enterprise.getId());
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
                return new ResultInfo(dto);
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
//            EntityWrapper<Code> wrapper = new EntityWrapper<>();
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
