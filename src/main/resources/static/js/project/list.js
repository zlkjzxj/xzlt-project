layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
    $ = layui.jquery
    ;
    $.ajax({
        url: '/enterprise/listDataSelect',
        success: function (data) {
            initEnterpriseList(data.data);
        }
    })

    function clickEnterprise(id) {
        var index = layui.layer.open({
                // title: '岗位列表',
                type: 2,
                area: ["1000px", "800px"],
                // area: 'auto',
                content: "positionList.html",
                success: function (layero, index) {
                    setTimeout(function () {
                        var body = layer.getChildFrame('body', index);
                        var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        iframeWin.atools.initTable(id);
                        layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            }
        )
        layui.layer.full(index);
    }

    function initEnterpriseList(data) {
        var html = "";
        for (var i = 0; i < data.length; i++) {
            if (i % 4 == 0) {
                html += "<div class='layui-row layui-col-space10'>";
            }
            html += "<div class='layui-col-md3' onClick=\"clickEnterprise(" + data[i].id + " );\">";
            html += "<div class=\"layui-card\">";
            html += "<div class=\"layui-card-body\">";
            html += "<div class=\"layui-row\">";
            html += "<div class=\"layui-col-md8\">";
            html += "<h3>" + data[i]['name'] + "</h3>";
            html += "<p>";
            html += "<i class=\"layui-icon layui-icon-friends\"> 负责人：</i>" + data[i]['manager'];
            html += "</p>";
            html += "<p>";
            html += "<i class=\"layui-icon layui-icon-cellphone\"> 电话：</i> " + data[i]['phone'];
            html += "</p>";
            html += "<p>";
            html += "<i class=\"layui-icon layui-icon-rate\"> 评分：</i> " + data[i]['grade'];
            html += "</p>";
            html += "<p>";
            html += "<i class=\"layui-icon layui-icon-tips\"> 企业描述：</i>" + data[i]['desc'];
            html += "</p>";
            html += "</div>";
            html += "<div class=\"layui-col-md4\">";
            html += "<img src='" + data[i]['logo'] + "' style=\"width: 100%;height: 100%;border-radius: 50%\">";
            html += "</div>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
            if (i % 4 == 3) {
                html += "</div>";
            }

        }
        $("#enterpriseList").html(html);
    }

    window.clickEnterprise = clickEnterprise;
})
