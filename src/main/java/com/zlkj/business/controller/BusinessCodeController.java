package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlkj.admin.controller.BaseController;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.service.ICodeService;
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
        //评分标准
        List<Code> codeList1 = iCodeService.getCodeType(2);
        //项目进度流程
        List<Code> codeList2 = iCodeService.getCodeType(4);
        //其他参数
        List<Code> codeList3 = iCodeService.getCodeType(5);
        Map<String, List<Code>> codeMap = new HashMap<>(3);
        codeMap.put("grade", codeList1);
        codeMap.put("progress", codeList2);
        codeMap.put("other", codeList3);
        return new ResultInfo(codeMap);
    }

    @RequestMapping("/listCodeData")
    public @ResponseBody
    ResultInfo<Map<String, Object>> listCodeData(Code code) {
        QueryWrapper<Code> codeEntityWrapper = new QueryWrapper<>();
        codeEntityWrapper.eq("code", code.getCode());
        List<Code> codeList = iCodeService.list(codeEntityWrapper);
        return new ResultInfo(codeList);
    }

    @RequestMapping("/updateCodes")
    public @ResponseBody
    @Transactional
    ResultInfo<Boolean> updateCodes(@RequestBody List<Code> codes) {
        if (!codes.isEmpty()) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("code", codes.get(0).getCode());
            iCodeService.removeByMap(map);
            iCodeService.saveBatch(codes);
            return new ResultInfo(true);
        }
        return new ResultInfo<>(false);
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