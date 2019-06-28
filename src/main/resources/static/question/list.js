layui.use(['form', 'layer', 'table', 'tree', 'laypage'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laypage = layui.laypage,
        table = layui.table;


    var roleList;
    $.post("/role/selectListData", {
        available: 1
    }, function (data) {
        roleList = data.data;
        roleList.forEach(function (e) {
            $("#roleSelect").append("<option value='" + e.id + "'>" + e.roleName + "</option>");
        });
        form.render('select');//刷新select选择框渲染
    });

    //用户列表
    var tableIns = table.render({
        elem: '#testUserList',
        url: '/question/getTestUserList',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limit: 10,
        limits: [10, 20, 30],
        id: "userListTable",
        cols: [[
            // {type: "radio", fixed: "left", width: 50},
            {field: 'userName', title: '姓名', minWidth: 100, align: "center"},
            {field: 'gender', title: '性别', minWidth: 100, align: "center"},
            {field: 'phone', title: '手机号码', minWidth: 150, align: "center"},
            {field: 'age', title: '年龄', minWidth: 150, align: "center"},
            {field: 'school', title: '学校', minWidth: 150, align: "center"},
            {field: 'idNumber', title: '学校', minWidth: 150, align: "center"},
            {field: 'qualification', title: '学历', minWidth: 150, align: "center"},
            {field: 'station', title: '职位', minWidth: 150, align: "center"}
        ]],
        done: function (res, curr, count) {
            // 隐藏列
            // $(".layui-table-box").find("[data-field='glbm']").css("display", "none");
            /* laypage.render({
                 elem: '#userList',
                 count: count,
                 limit: 5,
                 limits: [5, 10, 15]
             })*/
        }
    });
    //监听行双击事件
    table.on('rowDouble(testUserList)', function (obj) {
        console.log(obj);
        var index = layui.layer.open({
            title: '测试结果列表',
            type: 2,
            area: ["1000px", "800px"],
            content: "testresultlist.html",
            success: function (layero, index) {
                // var body = layui.layer.getChildFrame('body', index);
                setTimeout(function () {
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    console.log(iframeWin);
                    var initTable = iframeWin.atools.initTable
                    initTable(obj.data.id);
                    layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        })
        layui.layer.full(index);
    });

    //搜索
    $(".search_btn").on("click", function () {
        table.reload("userListTable", {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                userName: $(".userName").val(),
                name: $(".name").val(),
                state: $(".state").val(),
                roleId: $(".roleId").val(),
                glbm: $("#parentGlbm").val()
            }
        })
    });

})
