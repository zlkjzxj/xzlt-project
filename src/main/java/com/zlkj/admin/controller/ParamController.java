package com.zlkj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.internal.rngom.parse.host.Base;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.InitCode;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
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
import javax.validation.Valid;
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
public class ParamController extends BaseController {
    @Autowired()
    private IParamService paramService;
    @Resource()
    private ParamMapper paramMapper;
    @Resource
    protected CodeMapper codeMapper;

    @RequestMapping("/listData")
//    @RequiresPermissions("code:view")
    public @ResponseBody
    ResultInfo<List<Param>> listData(Param param, Integer page, Integer limit) {
        QueryWrapper<Param> wrapper = new QueryWrapper<>(param);
        IPage<Param> pageObj = paramService.page(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @SysLog("保存参数操作")
    @RequestMapping("/save")
//    @RequiresPermissions(value = {"role:add", "role:edit"}, logical = Logical.OR)
    public @ResponseBody
    ResultInfo<Boolean> save(@Valid Param param) {
        return new ResultInfo<>(paramService.saveParam(param));
    }

    @SysLog("删除参数操作")
    @RequestMapping("/del")
//    @RequiresPermissions("role:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer id) {
        return new ResultInfo<>(paramService.removeById(id));
    }

    @RequestMapping("/getInitParam")
    public @ResponseBody
    ResultInfo<InitCode> getInitParam() {
        //加载翻译参数 通用code
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", "init_code");
        Param param = paramMapper.selectOne(queryWrapper);
        String value = param.getValue();
        //加载翻译参数 不一样的code
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("code", "init_code1");
        Param param1 = paramMapper.selectOne(queryWrapper1);
        String value1 = param1.getValue();

        UserInfo user = this.getUserInfo();
        Map<String, List<Code>> codeMap = new HashMap<>(10);
        Map<String, String> paramMap = new HashMap<>(2);
        if (!StringUtils.isEmpty(value)) {
            String[] arrays = value.split(",");
            for (int i = 0; i < arrays.length; i++) {
                QueryWrapper<Code> wrapper = new QueryWrapper<>();
                wrapper.eq("code", arrays[i]);
                wrapper.eq("available", 1);
                List<Code> codeList = codeMapper.selectList(wrapper);
                codeMap.put(arrays[i], codeList);
            }
        }
        if (!StringUtils.isEmpty(value1)) {
            String[] arrays = value1.split(",");
            for (int i = 0; i < arrays.length; i++) {
                QueryWrapper<Code> wrapper = new QueryWrapper<>();
                wrapper.and(wq -> wq.eq("enterprise_id", user.getEnterpriseId()));
                wrapper.eq("code", arrays[i]);
                wrapper.eq("available", 1);
                wrapper.orderByAsc("code_value");
                List<Code> codeList = codeMapper.selectList(wrapper);
                if (codeList.isEmpty()) {
                    QueryWrapper<Code> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("code", arrays[i]);
                    wrapper1.eq("available", 1);
                    wrapper1.eq("enterprise_id", "").or().eq("enterprise_id", null);
                    codeList = codeMapper.selectList(wrapper1);
                }
                codeMap.put(arrays[i], codeList);
            }
        }
        return new ResultInfo<>(new InitCode(codeMap, paramMap));
    }
}
