package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.business.entity.WxUser;
import com.zlkj.business.service.IWxUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
@RequestMapping("/wxUser")
public class WxUserController {

    @Resource
    private IWxUserService iWxUserService;


    /**
     * 查询所有用户
     *
     * @return
     */
    @RequestMapping("/getWxUserList")
    @ResponseBody
    public ResultInfo<Map<String, Object>> getWxUserList(WxUser user, Integer page, Integer limit) {
        IPage<WxUser> pageObj = iWxUserService.page(new Page(page, limit));
        return new ResultInfo(pageObj.getRecords(), pageObj.getTotal());
    }
}
