layui.use(['form', 'layer', 'table'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table;
    $.ajax({
        url: '/businesscode/listData',
        success: function (data) {

            console.log(data.data);
            var obj = data.data;
            var progress = obj.progress, grade = obj.grade, other = obj.other;
            var progressHtml = '', gradeHtml = '', otherHtml = '';
            for (var i = 0; i < progress.length; i++) {
                progressHtml += "<div class='layui-col-md3'>";
                progressHtml += '<input type="checkbox" name="code" lay-skin="primary" value="' + progress[i].code + '" title="' + progress[i].name + '" >';
                progressHtml += "</div>";
            }
            $("#progressList").html(progressHtml);

            for (var i = 0; i < grade.length; i++) {
                gradeHtml += "<div class='layui-col-md3'>";
                gradeHtml += '<input type="checkbox" name="code" lay-skin="primary" value="' + grade[i].code + '" title="' + grade[i].name + '" >';
                gradeHtml += "</div>";
            }
            $("#gradeList").html(gradeHtml);

            for (var i = 0; i < other.length; i++) {
                otherHtml += "<div class='layui-col-md3'>";
                otherHtml += '<input type="checkbox" name="code" lay-skin="primary" value="' + other[i].code + '" title="' + other[i].name + '" >';
                otherHtml += "</div>";
            }
            $("#otherList").html(otherHtml);

            form.render('checkbox');
        }
    })

    var code_title = '', code_value = '';
    form.on('checkbox', function (data) {
        $("[type='checkbox']").prop("checked", "");
        $(this).prop("checked", "checked");
        form.render('checkbox');
        code_title = data.elem.title;
        code_value = data.elem.value;
    })
    //数据表列表
    // var tableIns = table.render({
    //     elem: '#tableList',
    //     url: '/businesscode/listData',
    //     cellMinWidth: 95,
    //     page: true,
    //     height: "full-125",
    //     limits: [10, 20, 30],
    //     limit: 10,
    //     id: "tableList",
    //     cols: [[
    //         {type: "radio", fixed: "left", width: 50},
    //         {field: 'name', title: '参数名称', minWidth: 300, align: "center"},
    //         {field: 'code', title: '参数代码', minWidth: 300, align: 'center'},
    //     ]]
    // });


    function addCode() {
        layui.layer.open({
            title: '参数修改',
            type: 2,
            area: ["800px", "600px"],
            content: "info.html",
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                body.find("#code").val(code_value);
                body.find("#name").val(code_title);
                setTimeout(function () {
                    var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    iframeWin.atools.getCodeList(code_value);
                    // layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                    //     tips: 3
                    // });
                }, 500)
            }
        })
    }

    $(".edit_btn").click(function () {
        var obj = $("[type='checkbox']:checked");
        console.log(obj)
        if (obj.length > 0) {
            addCode();
        } else {
            layer.msg("请选择需要修改的参数");
        }
    });

})
