package com.zlkj.admin.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zlkj.admin.annotation.SysLog;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Department;
import com.zlkj.admin.entity.TreeNode;
import com.zlkj.admin.service.IDepartmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Resource
    private IDepartmentService iDepartmentService;

    @RequestMapping("/*")
    public void toHtml() {

    }


    @RequestMapping("/listData")
//    @RequiresPermissions("dept:view")
    public @ResponseBody
    ResultInfo<List<Department>> listData(Department department) {
        EntityWrapper<Department> wrapper = new EntityWrapper<>(department);
        if (department != null && department.getBmmc() != null) {
            wrapper.eq("bmmc", department.getBmmc());
            department.setBmmc(null);
        }
//        List<Department> list = iDepartmentService.selectList(wrapper);
        return new ResultInfo<>(iDepartmentService.selectList(wrapper));
    }

    @SysLog("添加部门操作")
    @RequestMapping("/add")
    @RequiresPermissions("dept:add")
    public @ResponseBody
    ResultInfo<Boolean> add(Department department) {
        return new ResultInfo<>(iDepartmentService.insert(department));
    }

    @SysLog("修改部门操作")
    @RequestMapping("/edit")
    @RequiresPermissions("dept:edit")
    public @ResponseBody
    ResultInfo<Boolean> edit(Department department) {
        return new ResultInfo<>(iDepartmentService.updateById(department));
    }

    @SysLog("删除用户操作")
    @RequestMapping("/delBatch")
    @RequiresPermissions("user:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer[] idArr) {
        boolean b = iDepartmentService.deleteBatchIds(Arrays.asList(idArr));
        return new ResultInfo<>(b);
    }

    @RequestMapping("/listDataTree")
//    @RequiresPermissions("dept:view")
    public @ResponseBody
    ResultInfo<List<TreeNode>> listDataJson(Department department) {
        EntityWrapper<Department> wrapper = new EntityWrapper<>(department);
        Integer pid = 0;
        wrapper.eq("isshow", 1);
        if (department != null && department.getPid() != null) {
            wrapper.ge("pid", department.getPid());
            pid = department.getPid();
            department.setPid(null);
        }
        List<Department> list = iDepartmentService.selectList(wrapper);
        List<TreeNode> nodeList = new ArrayList<>();
        list.forEach(node -> {
            TreeNode<TreeNode> treeNode = new TreeNode<>(node.getId(), node.getBmmc(), node.getPid(), true);
            nodeList.add(treeNode);
        });
        return new ResultInfo<>(makeTree(nodeList, pid));
    }

    @RequestMapping("/listDataTreeWithoutCode")
//    @RequiresPermissions("dept:view")
    public @ResponseBody
    List<TreeNode> listDataJsonWithoutCode(Department department) {
        EntityWrapper<Department> wrapper = new EntityWrapper<>(department);
        Integer pid = 0;
        wrapper.eq("isshow", 1);
        if (department != null && department.getPid() != null) {
            wrapper.ge("pid", department.getPid());
            pid = department.getPid();
            department.setPid(null);
        }
        List<Department> list = iDepartmentService.selectList(wrapper);
        List<TreeNode> nodeList = new ArrayList<>();
        list.forEach(node -> {
            TreeNode<TreeNode> treeNode = new TreeNode<>(node.getId(), node.getBmmc(), node.getPid(), false, false);
            nodeList.add(treeNode);
        });
        return makeTree(nodeList, pid);
    }

    private static List<TreeNode> makeTree(List<TreeNode> TreeNodeList, int pId) {

        //子类
        List<TreeNode> children = TreeNodeList.stream().filter(x -> x.getPid() == pId).collect(Collectors.toList());

        //后辈中的非子类
        List<TreeNode> successor = TreeNodeList.stream().filter(x -> x.getPid() != pId).collect(Collectors.toList());

        children.forEach(x ->
                makeTree(successor, x.getId()).forEach(
                        y -> x.getChildren().add(y)
                )
        );

        return children;
    }

    @RequestMapping("/count")
    public @ResponseBody
    ResultInfo<Integer> count() {
        return new ResultInfo<>(
                iDepartmentService.selectCount(new EntityWrapper<>()));
    }
}