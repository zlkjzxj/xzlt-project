package com.zlkj.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/icon")
public class IconController {

    @RequestMapping("/*")
    public void toHtml(){

    }

}
