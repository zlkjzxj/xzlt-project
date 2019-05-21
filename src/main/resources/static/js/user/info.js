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
        var form = document.querySelector(".layui-form");
        var formdata = new FormData(form);
        console.log(formdata);
        console.log(formdata.get("name"));
        if ($("#id").val() === "") {
            console.log(data.field);
            $.ajax({
                url: "/user/add",
                data: formdata,
                type: 'POST',
                processData: false,  //必须false才会避开jQuery对 formdata 的默认处理
                contentType: false,  //必须false才会自动加上正确的Content-Type
                success: function (res) {
                    if (res.data) {
                        layer.close(index);
                        layer.msg("添加成功！");
                        //刷新父页面
                        parent.location.reload();
                    } else {
                        layer.msg(data.msg);
                    }
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
    $("#userFaceBtn").on('click', function () {
        $("#avatarFile").trigger('click');
    })

    $("#avatarFile").change(function (event) {
        var files = event.target.files, file;
        if (files && files.length > 0) {
            // 获取目前上传的文件
            file = files[0];// 文件大小校验的动作
            if (file.size > 1024 * 1024 * 2) {
                alert('图片大小不能超过 2MB!');
                return false;
            }
            // 获取 window 的 URL 工具
            var URL = window.URL || window.webkitURL;
            // 通过 file 生成目标 url
            var imgURL = URL.createObjectURL(file);
            //用attr将img的src属性改成获得的url
            $("#userFace").attr("src", imgURL);
            // 使用下面这句可以在内存中释放对此 url 的伺服，跑了之后那个 URL 就无效了
            // URL.revokeObjectURL(imgURL);
        }
    })
})