layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table
    ;
    // 对外提供访问方法
    var _tools = {
        company: '',
        grade: '',
        contacts: '',
        phone: '',
        companyName: '',
        initTable: function (id) {
            var tableIns = table.render({
                elem: '#projectList',
                url: '/project/listData?company=' + id,
                cellMinWidth: 95,
                page: true,
                height: "full-175",//高度最大化减去125
                limit: 10,
                limits: [10, 20, 30],
                id: "projectList",
                // skin:'nob',
                // size:"sm",
                even: true,
                // toolbar:true,
                autoSort: true,
                sortType: 'server',
                title: '宜元中林项目表',//导出Excel的时候会用到
                // initSort: {//默认排序
                //     field: 'number' //排序字段，对应 cols 设定的各字段名
                //     , type: 'asc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
                // },
                cols: [[
                    {type: "radio", fixed: "left"},
                    {field: 'name', title: '项目名称', align: "left", width: 200},
                    {field: 'number', title: '项目编号', align: "left", width: 200},
                    {field: 'lxsj', title: '立项时间', align: 'center', width: 120, sort: true},
                    {field: 'jssj', title: '结束时间', align: 'center', width: 120, sort: true},
                    {field: 'managerName', title: '项目经理', align: 'center', width: 100},
                    {field: 'membersName', title: '项目成员', align: 'center', width: 350},
                    {field: 'grade', title: '评分', align: 'center', width: 100},
                    {field: 'contacts', title: '联系人', align: 'center', width: 100},
                    {field: 'phone', title: '联系电话', align: 'center'},
                    // {
                    //     field: 'qrcode',
                    //     title: '二维码',
                    //     align: 'center',
                    //     width: 120,
                    //     templet: '<div><img src="{{ d.qrcode}}" style="height: 100px;width: 100px;"></div>'
                    // },
                    /*{
                        field: 'qrcode', title: '二维码', align: 'center', templet: function (d) {
                            if (d.qrcode != null) {
                                return '<image src="' + d.qrcode + '" style="width: 100px;height: 100px; text-align: center;" />';
                            }
                        }
                    }*/
                ]],
                done: function (res, curr, count) {
                }
            });
            return tableIns;
        }
    }
    //项目列表

    //监听排序事件
    table.on('sort(projectList)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

        //尽管我们的 table 自带排序功能，但并没有请求服务端。
        //有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，从而实现服务端排序，如：
        table.reload('projectList', {
            initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
            , where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
                field: obj.field //排序字段
                , order: obj.type //排序方式
            }
        });
    });


    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click", function () {
        table.reload("projectList", {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                searchVal: $(".searchVal").val(),

            }
        })
    });

    $(".add_btn").click(function () {
        addObject('add');

    })
    $(".edit_btn").click(function () {
        var checkStatus = table.checkStatus('projectList'),
            data = checkStatus.data;
        if (data.length > 0) {
            addObject('edit', data[0]);
        } else {
            layer.msg("请选择需要修改的项目");
        }
    });

    //批量删除
    $(".delAll_btn").click(function () {
        var checkStatus = table.checkStatus('projectList'),
            data = checkStatus.data;
        console.log(data);
        if (data.length > 0) {
            layer.confirm('确定删除选中的项目？', {icon: 3, title: '提示信息'}, function (index) {
                $.post("/project/del", {
                    id: data[0]['id']
                }, function (data) {
                    layer.close(index);
                    var tableIns = _tools.initTable(_tools.company);
                    tableIns.reload();
                })
            })
        } else {
            layer.msg("请选择需要删除的项目");
        }
    })

    //添加项目
    function addObject(type, data) {
        console.log(data);
        // var h = "750px";
        var title = "添加项目";
        if (type === 'add') {

        } else {
            title = "编辑项目";
        }
        var index = layui.layer.open({
                title: title,
                type: 2,
                area: ["1000px", "600px"],
                // area: 'auto',
                content: "info.html",
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    body.find("#addoredit").val(type);
                    //添加的时候设置
                    body.find("#company").val(_tools.company);
                    body.find("#grade").val(_tools.grade);
                    body.find("#contacts").val(_tools.contacts);
                    body.find("#phone").val(_tools.phone);
                    body.find("#companyName").val(_tools.companyName);
                    if (type === 'edit') {
                        body.find("#id").val(data.id);
                        body.find("#name").val(data.name);
                        body.find("#number").val(data.number);
                        body.find("#lxsj").val(data.lxsj);
                        body.find("#jssj").val(data.jssj);
                        body.find("#managerId").val(data.manager);
                        // body.find("#membersId").val(data.members);
                        body.find("#company").val(data.company);
                        body.find("#grade").val(data.grade);
                        body.find("#contacts").val(data.contacts);
                        body.find("#phone").val(data.phone);
                        form.render('select');
                        form.render();
                        setTimeout(function () {
                            var iframeWin = window[layero.find('iframe')[0]['name']];
                            var progress = iframeWin.atools.progress_value;
                            var user_level = iframeWin.atools.user_level;
                            console.log(data);
                            console.log(data.progress);
                            for (var i = 0; i < data.progress.length; i++) {
                                var key = data.progress[i].progressValue;
                                progress[key] = data.progress[i].progress;
                            }
                            console.log(progress);
                            var members = JSON.parse(data.members);
                            user_level = Object.assign(user_level, members);
                            // for (var i = 0; i < members.length; i++) {
                            //     user_level = Object.assign(user_level, members);
                            // }
                            var initNumber = iframeWin.atools.initNumber
                            var initProgress1 = iframeWin.atools.initProgress1;
                            initNumber(data.number);
                            initProgress1();

                        }, 500)
                    } else {
                        setTimeout(function () {
                            var iframeWin = window[layero.find('iframe')[0]['name']];
                            var initNumber = iframeWin.atools.initNumber;
                            initNumber('');
                        }, 500)
                    }
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                }
            }
        )
        // layui.layer.full(index);
    }

    //列表操作
    table.on('tool(newsList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;

        if (layEvent === 'edit') { //编辑
            addObject(data);
        } else if (layEvent === 'del') { //删除
            layer.confirm('确定删除此文章？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : data.newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            });
        } else if (layEvent === 'look') { //预览
            layer.alert("此功能需要前台展示，实际开发中传入对应的必要参数进行文章内容页面访问")
        }
    })
    window.atools = _tools;
})
