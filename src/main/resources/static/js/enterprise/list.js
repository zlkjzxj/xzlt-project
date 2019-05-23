layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
        var form = layui.form,
            layer = layui.layer,
            $ = layui.jquery,
            table = layui.table
        ;

        //项目列表
        var tableIns = table.render({
            elem: '#projectList',
            url: '/enterprise/listData',
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
            // title: '宜元中林项目表',//导出Excel的时候会用到
            initSort: {//默认排序
                field: 'number' //排序字段，对应 cols 设定的各字段名
                , type: 'asc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
            },
            cols: [[
                {type: "radio", fixed: "left"},
                {field: 'name', title: '企业名称', align: "left", width: 250},
                {field: 'manager', title: '企业负责人', align: 'center', width: 150},
                {field: 'phone', title: '负责人电话', align: 'center', width: 200},
                {field: 'pjzf', title: '企业评分', align: 'center', width: 150},
                {field: 'grade', title: '企业星级', align: 'center', width: 150},
                {field: 'desc', title: '企业描述', align: 'center'},
                {
                    field: 'qrcode', title: '二维码', align: 'center', templet: function (d) {
                        if (d.qrcode != null) {
                            return '<image src="' + d.qrcode + '" style="width: 100px;height: 100px; text-align: center;" />';
                        }
                    }
                }
            ]],

            done: function (res, curr, count) {
                //监听行双击事件  layui 新版添加，上面是旧版没有这个方法
                table.on('rowDouble(projectList)', function (obj) {
                    $("#projectId").val(obj.data.id);
                    // $(window).one("resize", function () {
                    var index = layui.layer.open({
                        title: "项目详情流程图",
                        type: 2,
                        area: ["1200px", "750px"],
                        content: "chart.html",
                        success: function (layero, index) {
                            setTimeout(function () {
                                layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                                    tips: 3
                                });
                            }, 500)
                        }
                    })
                    layui.layer.full(index);
                });
            }
        });
        //监听排序事件
        /*table.on('sort(projectList)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

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
*/

        //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
        $(".search_btn").on("click", function () {
            table.reload("projectList", {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    fuzzySearchVal: $(".searchVal").val()
                }
            })
        });
        $(".add_btn").click(function () {
            addObjects('add');

        })
        $(".edit_btn").click(function () {
            var checkStatus = table.checkStatus('projectList'),
                data = checkStatus.data;
            if (data.length > 0) {
                addObjects('edit', data[0]);
            } else {
                layer.msg("请选择需要修改的企业");
            }
        });

        //批量删除
        $(".delAll_btn").click(function () {
            var checkStatus = table.checkStatus('projectList'),
                data = checkStatus.data;
            if (data.length > 0) {
                layer.confirm('确定删除选中的企业？', {icon: 3, title: '提示信息'}, function (index) {
                    $.post("/enterprise/del", {
                        id: data[0]['id']
                    }, function (data) {
                        tableIns.reload();
                        layer.close(index);
                    })
                })
            } else {
                layer.msg("请选择需要删除的企业");
            }
        })

        function addObjects(type, data) {
            // var h = "750px";
            var title = "添加企业";
            if (type === 'add') {

            } else {
                title = "编辑企业";
            }
            //修改项目需要判断是否添加人还是修改人
            var index = layui.layer.open({
                    title: title,
                    type: 2,
                    area: ["1200px", "650px"],
                    // area: 'auto',
                    content: "info.html",
                    success: function (layero, index) {
                        var body = layui.layer.getChildFrame('body', index);
                        body.find("#addoredit").val(type);
                        if (type === 'edit') {
                            body.find("#id").val(data.id);
                            body.find("#name").val(data.name);
                            body.find("#number").val(data.number);
                            body.find("#manager").val(data.manager);
                            body.find("#phone").val(data.phone);
                            body.find("#desc").val(data.desc);
                            body.find("#grade").val(data.grade);
                        } else {
                        }
                        setTimeout(function () {
                            layui.layer.tips('点击此处返回项目列表', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3
                            });
                        }, 500)
                    }
                }
            )

        }
    }
)