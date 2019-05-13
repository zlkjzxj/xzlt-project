package com.zlkj.admin.web;

import com.zlkj.admin.dto.AppParam;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
public class AppController {

    @RequestMapping("/getProjectInfo")
    public @ResponseBody
    ResultInfo<AppParam<User>> getProjectInfo(AppParam<User> appParam) {

        return new ResultInfo<>(appParam);
    }
}
