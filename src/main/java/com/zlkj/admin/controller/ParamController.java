package com.zlkj.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.InitCode;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.entity.Param;
import com.zlkj.admin.dao.CodeMapper;
import com.zlkj.admin.dao.ParamMapper;
import com.zlkj.admin.service.IParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-05 14:17
 */
@Controller
@RequestMapping("/param")
public class ParamController {
    @Autowired
    private IParamService paramService;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    protected CodeMapper codeMapper;

    @RequestMapping("/listData")
//    @RequiresPermissions("code:view")
    public @ResponseBody
    ResultInfo<List<Param>> listData(Param param, Integer page, Integer limit) {
        EntityWrapper<Param> wrapper = new EntityWrapper<>(param);
        Page<Param> pageObj = paramService.selectPage(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @SysLog("保存参数操作")
    @RequestMapping("/save")
//    @RequiresPermissions(value = {"role:add", "role:edit"}, logical = Logical.OR)
    public @ResponseBody
    ResultInfo<Boolean> save(Param param) {
        return new ResultInfo<>(paramService.saveParam(param));
    }

    @SysLog("删除参数操作")
    @RequestMapping("/del")
//    @RequiresPermissions("role:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        return new ResultInfo<>(paramService.deleteById(id));
    }

    @RequestMapping("/getInitParam")
    public @ResponseBody
    ResultInfo<InitCode> getInitParam() {
        //加载翻译参数
        Param param = new Param();
        param.setCode("init_code");
        Param param1 = paramMapper.selectOne(param);
        String value = param1.getValue();

        Map<String, List<Code>> codeMap = new HashMap<>(10);
        Map<String, String> paramMap = new HashMap<>(2);
        if (!StringUtils.isEmpty(value)) {
            String[] arrays = value.split(",");
            for (int i = 0; i < arrays.length; i++) {
                EntityWrapper<Code> wrapper = new EntityWrapper<>(new Code());
                wrapper.eq("code", arrays[i]);
                List<Code> codeList = codeMapper.selectList(wrapper);
                codeMap.put(arrays[i], codeList);
            }
        }
        return new ResultInfo<>(new InitCode(codeMap, paramMap));
    }
}
