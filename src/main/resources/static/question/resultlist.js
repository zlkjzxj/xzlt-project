layui.use(['form', 'layer', 'table', 'tree', 'laypage'], function () {
    layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    function initTable(userId) {
        console.log(userId)
        var tableIns = table.render({
            elem: '#resultList',
            url: '/question/getTestList?userId=' + userId,
            cellMinWidth: 95,
            page: true,
            height: "full-125",
            limit: 10,
            limits: [10, 20, 30],
            id: "resultListTable",
            cols: [[
                // {type: "radio", fixed: "left", width: 50},
                {field: 'userName', title: '姓名', minWidth: 100, align: "center"},
                {field: 'questionTypeName', title: '测试类型', minWidth: 100, align: "center"},
                {field: 'testJob', title: '推荐工作类型', align: "center"},
            ]]
        });
        return tableIns;
    }


    //监听行双击事件
    table.on('rowDouble(resultList)', function (obj) {
        console.log(obj.data);
        var index = layui.layer.open({
            title: '测试结果详情页',
            type: 2,
            area: ["1000px", "700px"],
            content: "resultinfo.html",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function () {
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    console.log(iframeWin);
                    var initDetail = iframeWin.obj.initDetail
                    initDetail(obj.data.userId, obj.data.questionTypeId);
                }, 500)
            }
        })
    });

    var _tools = {
        initTable: initTable,
    }
    window.atools = _tools;
})
