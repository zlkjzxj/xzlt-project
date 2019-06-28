layui.use(['form', 'layer', 'table'], function () {
    var $ = layui.$, table = layui.table, form = layui.form, layer = layui.layer;
    window.viewObj = {
        tbData: [],
    };
    //数据表格实例化
    var tbWidth = $("#tableRes").width();
    var layTableId = "layTable";
    var tableIns;
    var code = $("#code").val();
    console.log(code);

    function getCodeList(code) {
        $.ajax('/businesscode/listCodeData?code=' + $("#code").val(), {
            success: function (res) {
                var data = res.data;
                tableIns = table.render({
                    elem: '#codeList',
                    id: layTableId,
                    data: data,
                    width: tbWidth,
                    // page: true,
                    page: false,
                    limit: 100,
                    loading: true,
                    even: false, //不开启隔行背景
                    cols: [[
                        {title: '序号', type: 'numbers'},
                        {field: 'codeName', title: '参数名称', align: "center", edit: 'text'},
                        {field: 'code', title: '参数代码', align: 'center'},
                        {field: 'codeValue', title: '参数代码', align: 'center'},
                        {
                            field: 'id', title: '操作', templet: function (d) {
                                return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" lay-id="' + d.codeValue + '"><i class="layui-icon layui-icon-delete"></i>移除</a>';
                            }
                        }
                    ]],
                    done: function (res, curr, count) {
                        viewObj.tbData = res.data;
                    }
                });
            }
        })
    }

    //定义事件集合
    var active = {
        addRow: function () {	//添加一行
            var oldData = table.cache[layTableId];
            console.log(oldData);
            //get the big value
            var values = [];
            for (var i = 0; i < oldData.length; i++) {
                values.push(oldData[i].codeValue);
            }
            var max = Math.max.apply(null, values);
            var newRow = {
                codeName: '',
                name: $("#name").val(),
                code: $("#code").val(),
                codeValue: max + 1,
                codeType: 2
            };
            oldData.push(newRow);
            tableIns.reload({
                data: oldData
            });
        },
        updateRow: function (obj) {
            var oldData = table.cache[layTableId];
            console.log(oldData);
            for (var i = 0, row; i < oldData.length; i++) {
                row = oldData[i];
                if (row.id == obj.id) {
                    $.extend(oldData[i], obj);
                    return;
                }
            }
            tableIns.reload({
                data: oldData
            });
        },
        removeEmptyTableCache: function () {
            var oldData = table.cache[layTableId];
            for (var i = 0, row; i < oldData.length; i++) {
                row = oldData[i];
                if (!row || !row.id) {
                    oldData.splice(i, 1);    //删除一项
                }
                continue;
            }
            tableIns.reload({
                data: oldData
            });
        },
        save: function () {
            var oldData = table.cache[layTableId];
            console.log(oldData);
            for (var i = 0, row; i < oldData.length; i++) {
                row = oldData[i];
                if (!row.codeName) {
                    layer.msg("请填写参数名称！", {icon: 5}); //提示
                    return;
                }
            }

            var str = JSON.stringify(table.cache[layTableId], null, 2);	//使用JSON.stringify() 格式化输出JSON字符串
            //弹出loading
            var index = layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
            $.ajax({
                url: '/businesscode/updateCodes',
                data: str,
                type: "post",
                cache: false,
                dataType: "json",
                contentType: "application/json",
                success: function (res) {
                    if (res.data) {
                        layer.close(index);
                        layer.msg("操作成功！");
                        //刷新父页面
                        parent.location.reload();
                    } else {
                        layer.close(index);
                        layer.msg(data.msg);
                    }
                },
                error: function (e) {
                    layer.close(index);
                    layer.msg(e.msg);
                }
            });
        }
    }

    //激活事件
    var activeByType = function (type, arg) {
        if (arguments.length === 2) {
            active[type] ? active[type].call(this, arg) : '';
        } else {
            active[type] ? active[type].call(this) : '';
        }
    }

    //注册按钮事件
    $('.layui-btn[data-type]').on('click', function () {
        var type = $(this).data('type');
        activeByType(type);
    });

    //监听select下拉选中事件
    form.on('select(type)', function (data) {
        var elem = data.elem; //得到select原始DOM对象
        $(elem).prev("a[lay-event='type']").trigger("click");
    });

    //监听工具条
    table.on('tool(codeList)', function (obj) {
        var data = obj.data, event = obj.event, tr = obj.tr; //获得当前行 tr 的DOM对象;
        console.log(data);
        switch (event) {
            case "type":
                //console.log(data);
                var select = tr.find("select[name='type']");
                if (select) {
                    var selectedVal = select.val();
                    if (!selectedVal) {
                        layer.tips("请选择一个分类", select.next('.layui-form-select'), {tips: [3, '#FF5722']}); //吸附提示
                    }
                    console.log(selectedVal);
                    $.extend(obj.data, {'type': selectedVal});
                    activeByType('updateRow', obj.data);	//更新行记录对象
                }
                break;
            case "state":
                var stateVal = tr.find("input[name='state']").prop('checked') ? 1 : 0;
                $.extend(obj.data, {'state': stateVal})
                activeByType('updateRow', obj.data);	//更新行记录对象
                break;
            case "del":
                layer.confirm('真的删除行么？', function (index) {
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    activeByType('removeEmptyTableCache');
                });
                break;
        }
    });
    var _tools = {
        getCodeList: getCodeList
    }
    window.atools = _tools;
})