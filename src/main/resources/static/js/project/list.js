layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
    $ = layui.jquery
    ;

    $(".enterpriseTag").on('click', function () {
        var index = layui.layer.open({
                // title: '岗位列表',
                type: 2,
                area: ["1000px", "800px"],
                // area: 'auto',
                content: "positionList.html",
                success: function (layero, index) {
                    // var body = layer.getChildFrame('body', index);
                    // var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    // console.log(iframeWin);
                    // console.log(iframeWin.atools);
                    // console.log(iframeWin.atools.a);
                    // iframeWin.atools.initTable();
                    setTimeout(function () {
                        var body = layer.getChildFrame('body', index);
                        var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        console.log(iframeWin);
                        console.log(iframeWin.atools);
                        console.log(iframeWin.atools.a);
                        iframeWin.atools.initTable();
                        layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            }
        )
        layui.layer.full(index);
    })
})
