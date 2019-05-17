layui.config({
    base: '/static/layui/'
}).use(['form', 'layer'], function () {
    var form = layui.form
    layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    //初始化用户级别和所属公司
    var initParam = sessionStorage.getItem("initParam");
    initParam = JSON.parse(initParam);
    var userLevel = initParam.codeMap.userlevel;
    var company = initParam.codeMap.usercompany;

    userLevel.forEach(function (e) {
        $("#levelSelect").append("<option value='" + e.codeValue + "'>" + e.codeName + "</option>");
        $("#levelSelect").val($("#userLevel").val());//默认选中
    });
    company.forEach(function (e) {
        $("#companySelect").append("<option value='" + e.codeValue + "'>" + e.codeName + "</option>");
        $("#companySelect").val($("#company").val());//默认选中
    });
    form.render('select');//刷新select选择框渲染

    //初始化角色下拉框
    $.post("/role/selectListData", {
        available: 1
    }, function (data) {
        var roleList = data.data;
        roleList.forEach(function (e) {
            $("#roleSelect").append("<option value='" + e.id + "'>" + e.roleName + "</option>");
        });
        $("#roleSelect").val($("#roleId").val());//默认选中
        form.render('select');//刷新select选择框渲染
    });

    //添加验证规则
    form.verify({
        newPwd: function (value, item) {
            if (value.length < 6) {
                return "密码长度不能小于6位";
            }
        },
        confirmPwd: function (value, item) {
            if (!new RegExp($("#newPwd").val()).test(value)) {
                return "两次输入密码不一致，请重新输入！";
            }
        }
    })

    form.on("submit(addUser)", function (data) {
        //弹出loading
        var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
        if ($("#id").val() === "") {
            console.log(data.field);
            $.post("/user/add", data.field, function (res) {
                if (res.data) {
                    layer.close(index);
                    layer.msg("添加成功！");
                    // layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                } else {
                    console.log(res);
                    layer.msg(res.msg);
                }
            })
        } else {
            $.post("/user/edit", data.field, function (res) {
                if (res.data) {
                    layer.close(index);
                    layer.msg("修改成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                } else {
                    layer.msg(res.msg);
                }
            })
        }
        return false;
    })

})