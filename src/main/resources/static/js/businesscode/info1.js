layui.use(['form', 'layer', 'table'], function () {
    var form = layui.form
    layer = layui.layer,
        table = layui.table,
        $ = layui.jquery;

    var tableData;
    //数据表列表
    // function getCodeList(code) {
    var tableIns = table.render({
        elem: '#codeList',
        url: '/businesscode/listCodeData?code=' + 'usercompany',
        cellMinWidth: 95,
        toolbar: '#toolbar',
        // page: true,
        // height: "full-125",
        limits: [10, 20, 30],
        limit: 10,
        cols: [[
            // {type: "radio", fixed: "left", width: 50},
            {field: 'codeName', title: '参数名称', align: "center", edit: 'text'},
            {field: 'code', title: '参数代码', align: 'center'},
            {field: 'codeValue', title: '参数代码', align: 'center'},
            {fixed: 'right', width: 178, align: 'center', toolbar: '#tableBar'},
        ]],
        done: function (res, curr, count) {
            tableData = res.data;
        }
    });
    // }

    //监听单元格编辑
    table.on('edit(codeList)', function (obj) {
        console.log(obj)
        var value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        layer.msg('[ID: ' + data.id + '] ' + field + ' 字段更改为：' + value);
    });
    //监听工具条
    table.on('tool(demo)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            layer.msg('ID：' + data.id + ' 的查看操作');
        } else if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                obj.del();
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            layer.alert('编辑行：<br>' + JSON.stringify(data))
        }
    });
    //头工具栏事件
    $("#addOne").on('click', function () {
        var tr = " <tr data-index=\"2\">" +
            " <td data-field='codeName' data-key=\"1-0-0\" data-edit='text' align=\"center\"> <div class='layui-table-cell laytable-cell-1-0-0 '> </div></td>" +
            " <td data-field='code' data-key=\"1-0-1\" align=\"center\"> <div class='layui-table-cell laytable-cell-1-0-1 '>usercompany </div></td>" +
            " <td data-field='codeValue' data-key=\"1-0-2\" align=\"center\"> <div class='layui-table-cell laytable-cell-1-0-2 '>33 </div></td>" +
            " <td data-field='4' data-key=\"1-0-3\" align=\"center\"> <div class='layui-table-cell laytable-cell-1-0-3 '><a class=\"layui-btn layui-btn-xs\" lay-event=\"edit\">编辑</a><a class=\"layui-btn layui-btn-danger layui-btn-xs\" lay-event=\"del\">删除</a> </div></td>" +
            " </tr>";
        $(".layui-table-body .layui-table tbody").append(tr);
    });

    // //头工具栏事件
    // table.on('toolbar(codeList)', function (obj) {
    //     if (obj.event == 'addOne') {
    //         var tableData = table.cache.codeList;
    //         var datas = [];
    //         if (tableData != null) {
    //             for (var i = 0; i < tableData.length; i++) {
    //                 datas.push(tableData[i]);
    //             }
    //         }
    //         datas.push(tableData[0]);
    //
    //         /*datas.push({
    //             id: null,
    //             name: datas[0].name,
    //             code: datas[0].code,
    //             codeDesc: "",
    //             codeIcon: null,
    //             codeMark: null,
    //             codeName: datas[0].codeName,
    //             codeValue: null,
    //             cpTc: null,
    //             cpTc: null,
    //             createTime: null,
    //             updateTime: null
    //         })*/
    //         console.log(datas)
    //         table.reload("codeList", {
    //             data: datas
    //         })
    //         return false
    //     }
    // });


    form.on("submit(addParam)", function (data) {
        //根据table 重新取下数据

        //弹出loading
        // var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
        // $.post("/param/save", data.field, function (res) {
        //     if (res.data) {
        //         layer.close(index);
        //         layer.msg("保存成功！");
        //         layer.closeAll("iframe");
        //         //刷新父页面
        //         parent.location.reload();
        //     } else {
        //         layer.msg(data.msg);
        //     }
        // });
        return false;
    })
    var _tools = {
        // getCodeList: getCodeList
    }
    window.atools = _tools;
})