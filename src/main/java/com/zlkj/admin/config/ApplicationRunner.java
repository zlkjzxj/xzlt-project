package com.zlkj.admin.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlkj.admin.dao.CodeMapper;
import com.zlkj.admin.dao.ParamMapper;
import com.zlkj.admin.dto.CodeDto;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.Param;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.util.CodeConstant;
import com.zlkj.admin.util.ImageConstant;
import com.zlkj.admin.util.TranslateUtils;
import com.zlkj.business.entity.Tc;
import com.zlkj.business.service.ITcService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description 添加启动类加载翻译的code
 * @Author sunny
 * @Date 2019-04-29 14:48
 */
@Component
public class ApplicationRunner implements CommandLineRunner {
    @Resource
    private ParamMapper paramMapper;
    @Resource
    protected CodeMapper codeMapper;
    @Resource
    private ICodeService iCodeService;
    @Resource
    private ITcService iTcService;

    @Override

    public void run(String... args) {
        this.initCode();


   /* //加载翻译参数
    Param param = new Param();
        param.setCode("url");
    Param param1 = paramMapper.selectOne(param);
    String value = param1.getValue();
    //加载部门简称规则
        param.setCode("project_number_dep");
    Param project_number_dep = paramMapper.selectOne(param);
    String project_number_dep_value = project_number_dep.getValue();
    //加载部门简称类型
        param.setCode("project_number_type");
    Param project_number_type = paramMapper.selectOne(param);
    String project_number_type_value = project_number_type.getValue();

    Map<String, List<Code>> codeMap = new HashMap<>(10);
    Map<String, String> paramMap = new HashMap<>(2);
        paramMap.put("project_number_dep",project_number_dep_value);
        paramMap.put("project_number_type",project_number_type_value);
        if(!StringUtils.isEmpty(value))

    {
        String[] arrays = value.split(",");
        for (int i = 0; i < arrays.length; i++) {
            EntityWrapper<Code> wrapper = new EntityWrapper<>(new Code());
            wrapper.eq("code", arrays[i]);
            List<Code> codeList = codeMapper.selectList(wrapper);
            codeMap.put(arrays[i], codeList);
        }
    }

    //把需要翻译的参数打成js文件
    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        TranslateUtils.putCodeFile(path,CODE_JS_NAME,codeMap,paramMap);*/
    }

    public void initCode() {
//测评code qyrs,clsj,qyxz,zyyt,jyzt,ltdjed,gzqy,gltxjqcd,nrylsl,jxry,yqbzq,zltsl,ldjf,swjf,xytc,qtjfx,starrating
        String[] cpcodes = new String[]{"qyrs", "clsj", "qyxz", "zyyt", "jyzt", "ltdjed", "gzqy", "gltxjqcd", "nrylsl", "jxry",
                "yqbzq", "zltsl", "xytc", "ldjf", "swjf"};

        for (int i = 0; i < cpcodes.length; i++) {
            QueryWrapper<Code> wrapper = new QueryWrapper<>();
            wrapper.eq("code", cpcodes[i]);
            List<Code> codeList = iCodeService.list(wrapper);
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
                    Tc tc = iTcService.getById(code.getCpTc());
                    dto.setCpTc(tc);
                }
                dtoList.add(dto);
            }
            CodeConstant.cpCodeMap.put(cpcodes[i], dtoList);
        }
        //进度code
        QueryWrapper<Code> wrapper11 = new QueryWrapper<>();
        wrapper11.eq("code", "projectprogress");
        List<Code> progressCodeList = iCodeService.list(wrapper11);
        CodeConstant.progressCodeList = progressCodeList;
        // managerPhone
        //进度code
        QueryWrapper<Code> wrapper12 = new QueryWrapper<>();
        wrapper12.eq("code", "managerphone");
        Code phone = iCodeService.getOne(wrapper12);
        CodeConstant.phone = phone;
        //用户职位code
        QueryWrapper<Code> userLevelwrapper = new QueryWrapper<>();
        userLevelwrapper.eq("code", "userlevel");
        List<Code> userLevelCodeList = iCodeService.list(userLevelwrapper);
        CodeConstant.userLevelCodeList = userLevelCodeList;
        //评分
        QueryWrapper<Code> starratingWrapper = new QueryWrapper<>();
        starratingWrapper.eq("code", "starrating");
        List<Code> starRatingList = codeMapper.selectList(starratingWrapper);
        CodeConstant.starRatingCodeList = starRatingList;

        //加载翻译参数

        QueryWrapper<Param> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("code", "url");
        Param param1 = paramMapper.selectOne(wrapper1);
        ImageConstant.URL = param1 == null ? ImageConstant.URL : param1.getValue();

        QueryWrapper<Param> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("code", "image_path");
        Param param2 = paramMapper.selectOne(wrapper2);
        ImageConstant.IMG_PATH = param2 == null ? ImageConstant.IMG_PATH : param2.getValue();

        QueryWrapper<Param> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("code", "user_avatar_path");
        Param param3 = paramMapper.selectOne(wrapper3);
        ImageConstant.USER_AVATAR_PATH = param3 == null ? ImageConstant.USER_AVATAR_PATH : param3.getValue();

        QueryWrapper<Param> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("code", "user_default_avatar");
        Param param4 = paramMapper.selectOne(wrapper4);
        ImageConstant.USER_DEFAULT_AVATAR = param4 == null ? ImageConstant.USER_DEFAULT_AVATAR : param4.getValue();

        QueryWrapper<Param> wrapper5 = new QueryWrapper<>();
        wrapper5.eq("code", "enterprise_avatar_path");
        Param param5 = paramMapper.selectOne(wrapper5);
        ImageConstant.ENTERPRISE_AVATAR_PATH = param5 == null ? ImageConstant.ENTERPRISE_AVATAR_PATH : param5.getValue();

        QueryWrapper<Param> wrapper6 = new QueryWrapper<>();
        wrapper6.eq("code", "enterprise_qrcode_path");
        Param param6 = paramMapper.selectOne(wrapper6);
        ImageConstant.ENTERPRISE_QRCODE_PATH = param6 == null ? ImageConstant.ENTERPRISE_QRCODE_PATH : param6.getValue();
    }
}
