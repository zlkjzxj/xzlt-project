package com.zlkj.admin.controller;
import com.zlkj.admin.dto.MenuInfo;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.RoleInfo;
import com.zlkj.admin.dto.UserInfo;
import com.zlkj.admin.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author sunny
 * @since 2018-07-16
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{

    @Resource
    private IPermissionService iPermissionService;

    @RequestMapping("/left")
    @ResponseBody
    public ResultInfo<List<MenuInfo>> getMenuInfoList(@RequestParam("code") String code){
        List<MenuInfo> menuInfoList = new ArrayList<>();
        //获取当前用户角色信息
        UserInfo userInfo = this.getUserInfo();
        RoleInfo roleInfo = userInfo.getRoleInfo();
        if (roleInfo == null || StringUtils.isEmpty(roleInfo.getPermissionIds())){
            return new ResultInfo<>(menuInfoList);
        }
        //获取权限菜单信息
        List<MenuInfo>  allMenuInfoList = iPermissionService.getMenuPermissions(code);
        if (allMenuInfoList == null || allMenuInfoList.isEmpty()){
            return new ResultInfo<>(menuInfoList);
        }
        //获得有权限访问菜单
        for (MenuInfo info : allMenuInfoList) {
            if(roleInfo.getPermissionIds().contains(","+info.getOnlyId()+",")){
                if (info.getChildren() != null){
                    List<MenuInfo> subMenuInfoList = new ArrayList<>();
                    for (MenuInfo subInfo : info.getChildren()) {
                        if (roleInfo.getPermissionIds().contains(","+subInfo.getOnlyId()+",")){
                            subMenuInfoList.add(subInfo);
                        }
                    }
                    info.setChildren(subMenuInfoList);
                }
                menuInfoList.add(info);
            }
        }
        return new ResultInfo<>(menuInfoList);
    }

}
