package com.zlkj.admin.config;

import com.zlkj.admin.dao.CodeMapper;
import com.zlkj.admin.dao.ParamMapper;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

/**
 * @Description 添加启动类加载翻译的code
 * @Author sunny
 * @Date 2019-04-29 14:48
 */
//@Component
public class ApplicationRunner implements CommandLineRunner {
    @Resource
    private ParamMapper paramMapper;
    @Resource
    protected CodeMapper codeMapper;

    @Override

    public void run(String... args) {
        /*//加载翻译参数
        Param param = new Param();
        param.setCode("init_code");
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
        paramMap.put("project_number_dep", project_number_dep_value);
        paramMap.put("project_number_type", project_number_type_value);
        if (!StringUtils.isEmpty(value)) {
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
        TranslateUtils.putCodeFile(path, CODE_JS_NAME, codeMap, paramMap);*/
    }


}
