layui.config({
    base: '/static/layui'
}).extend({
    // selectM: '/extend/selectM',
    tableSelect: '/extend/tableSelect'
}).use(['form', 'layer', 'layedit', 'laydate', 'upload', 'rate', 'slider', 'table', 'tableSelect'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate,
        $ = layui.jquery,
        rate = layui.rate,
        slider = layui.slider,
        selectM = layui.selectM,
        tableSelect = layui.tableSelect
    ;
    var date = new Date();
    var year = date.getFullYear();

    //存放选中成员的级别
    var user_level = {};
    //存放进度
    var progress_value = {};

    //这是初始化项目编号的
    $.post("/project/getAddSequence?year=" + year, function (data) {
        $("#number").val(data.data);
    });
    //这是初始化所属公司的
    $.post("/enterprise/listDataSelect", {
        available: 1
    }, function (data) {
        var enterpriseList = data.data;
        enterpriseList.forEach(function (e) {
            $("#company").append("<option value='" + e.id + "'>" + e.name + "</option>");
        });
        if ($("#companyId") !== '') {
            $("#company").val($("#companyId").val())
        }
        form.render('select');

    });
    var initParam = sessionStorage.getItem("initParam");
    var codes = JSON.parse(initParam)['codeMap'];
    //人员职位
    var userLevel = codes['userlevel'];
    //项目进度
    var projectprogress = codes['projectprogress'];
    //这是初始化项目经理和项目成员的
    var userList;
    $.post("/user/listDataSelect", {
        available: 1
    }, function (data) {
        userList = data.data;
        //渲染表格要放到这，不然容易出错！
        initManagerAndMembers(userList)
    });
    var layerOpen = true;
    $("#memberSelect").on('click', function () {
        var elem = $("#memberSelect");
        console.log(elem)
        console.log(elem.offset())
        var t = elem.offset().top + elem.outerHeight() + "px";
        var l = elem.offset().left + "px";
        var tableName = "memeberTable";
        var tableBox = '<div  style="left:' + l + ';top:' + t + ';border: 1px solid #d2d2d2;background-color: #fff;">';
        tableBox += '<table id="' + tableName + '" lay-filter="' + tableName + '" class="layui-table" cellpadding="0" cellspacing="0" border="0">';
        tableBox += '<tr>';
        tableBox += '<td width="20px"></td>';
        tableBox += '<td>姓名</td>';
        tableBox += '<td>职位</td>';
        tableBox += '</tr>';
        var userkeys = Object.keys(user_level);
        console.log(userkeys)
        userList.forEach(function (elem, index) {
            tableBox += '<tr>';
            if (userkeys.indexOf(elem.id + "") > -1) {
                tableBox += '<td class ="layui-table-cell"><input  name="memberBox" type="checkbox" style="width: 16px;height: 16px;" checked/></td>';
            } else {
                tableBox += '<td class ="layui-table-cell"><input  name="memberBox" type="checkbox" style="width: 16px;height: 16px;"/></td>';

            }
            tableBox += '<td id="' + elem.id + '">' + elem.name + '</td>';
            tableBox += '<td>';
            userLevel.forEach(function (e) {
                var checkValue = user_level[elem.id];
                if (e.codeValue == checkValue) {
                    tableBox += '<input type="radio" name="' + e.code + elem.id + '" value="' + e.codeValue + '" style="width: 16px;height: 16px; vertical-align: -3px;" checked>' + e.codeName;
                } else {
                    tableBox += '<input type="radio" name="' + e.code + elem.id + '" value="' + e.codeValue + '" style="width: 16px;height: 16px; vertical-align: -3px;">' + e.codeName;
                }
            });
            tableBox += '</td>';
            tableBox += '</tr>';
        })
        tableBox += "</table>"
        tableBox += '</div>';
        if (layerOpen) {
            layerOpen = false;
            var index = layer.open({
                type: 1,
                content: tableBox,
                area: ['500px', '350px'],
                fixed: false,
                offset: ["450px", "1100px"],
                shade: 0,
                success: function (layero, index) {
                    layero.css("position", "relative").append(layero);    //如果该父级原来没有设置相对定位，那么在追加该弹层之前需要设置
                },
                cancel: function (index, layero) {
                    layerOpen = true;
                },
                btn: ['选择'],
                yes: function (index, layero) {
                    var names = [], flag = true;
                    for (var key in user_level) {
                        delete user_level[key]
                    }
                    var checks = layero.find("input[name='memberBox']:checked");
                    if (checks.length > 4) {
                        flag = false;
                        layer.msg("项目成员最多只能选四个");
                        return false;
                    }
                    layero.find("input[name='memberBox']:checked").each(function () {
                        var name = $(this).parent().next().text();
                        var id = $(this).parent().next().attr("id");
                        var radio = $(this).parent().next().next().find("input[type='radio']:checked").val();

                        if (radio == undefined) {
                            flag = false;
                            layer.msg("请选择职位");
                            return false;
                        } else {
                            names.push(name);
                            user_level[id] = radio;

                        }
                    });
                    if (flag) {
                        layerOpen = true;
                        $("#memberSelect").val(names.join(","));
                        layer.close(index);
                    }
                }
            })
        }

    })

    function initManagerAndMembers(users) {
        //渲染项目经理select
        users.forEach(function (e) {
            $("#manager").append("<option value='" + e.id + "'>" + e.name + "</option>");
            // $("#levelSelect").val($("#userLevel").val());//默认选中
        });
        console.log($("#managerId").val())
        if ($("#managerId") !== '') {
            $("#manager").val($("#managerId").val())
        }
        form.render('select');//刷新select选择框渲染
        // var members = $("#membersId").val(),
        //     memberArray = members.split(",");
        // tableSelect.render({
        //     elem: '#memberSelect',	//定义输入框input对象
        //     checkedKey: 'id', //表格的唯一建值，非常重要，影响到选中状态 必填
        //     searchKey: 'keyword',	//搜索输入框的name值 默认keyword
        //     // searchPlaceholder: '关键词搜索',	//搜索输入框的提示文字 默认关键词搜索
        //     table: {	//定义表格参数，与LAYUI的TABLE模块一致，只是无需再定义表格elem
        //         url: '/user/listDataSelect',
        //         cols: [[
        //             {type: "checkbox", fixed: "left"},
        //             {field: 'id', title: 'id', align: "left", width: 100},
        //             {field: 'name', title: '姓名', align: "left", width: 100},
        //             {
        //                 field: 'userName', title: '职位', templet: function (d) {
        //                     return renderMemberTable(d);
        //                 }
        //             },
        //         ]]
        //     },
        //     done: function (elem, data) {
        //         var NEWJSON = []
        //         layui.each(data.data, function (index, item) {
        //             NEWJSON.push(item.name)
        //         })
        //         elem.val(NEWJSON.join(","));
        //         console.log(user_level);
        //         // $("#fuck").attr("ts-selected","42,43,44,45")
        //         // var xx = $(".layui-form-radio");
        //         // console.log(xx.length);
        //         // $(".layui-form-radio").each(function () {
        //         //     var _this = $(this);
        //         //     _this.removeClass("layui-form-radioed");
        //         //
        //         // })
        //     }
        // })
        //
        // userList.forEach(function (user) {
        //     form.on('radio(userlevel' + user.id + ')', function (data) {
        //         user_level[user.id] = data.value;
        //     });
        // })

        // membersTag = selectM({
        //     //元素容器【必填】
        //     elem: '#memberDiv'
        //     //候选数据【必填】
        //     , data: users
        //     // //默认值
        //     , selected: memberArray
        //     //最多选中个数，默认5
        //     , max: 10
        //     //input的name 不设置与选择器相同(去#.)
        //     , name: 'members'
        //     //值的分隔符
        //     , delimiter: ','
        //     //候选项数据的键名
        //     , field: {idName: 'id', titleName: 'name'}
        //
        // });

        var grade = $("#gradeId").val() === "" ? 0 : $("#gradeId").val();
        rate.render({
            elem: '#gradeDiv'
            , value: grade
            // , half: true //开启半星
            , readonly: true
        });
        $("#gradeSpan").text(grade + "星");
        $("#grade").val(grade);
        //rate 重新复制星星就对不齐了
        var rates = document.getElementsByClassName("layui-rate")[0].children;
        for (var i = 0; i < rates.length; i++) {
            rates[i].className = 'layui-inline';
        }
    }


    //循环生成项目进度列表
    function initProgress() {
        projectprogress.forEach(function (e) {
            var id = "silde" + e.codeValue;
            var html = "<div class='layui-form-item'>";
            html += "<label class='layui-form-label'>" + e.codeName + "</label>";
            html += "<div class='layui-input-block' style='padding: 15px 80px 0 20px;'>";
            html += "<div id='" + id + "' class='demo-slider' ></div>";
            html + "</div></div>"
            $("#sliders").append(html);
            progress_value[e.codeValue] = 0;
            // $(".sliders").append("fuck shit:<div  class='demo-slider' id='" + id + "' style='padding: 30px 80px 0 20px;'></div>");
            //开启输入框
            slider.render({
                elem: '#' + id
                , input: true //输入框
                , change: function (value) {
                    progress_value[e.codeValue] = value;
                }
            });
        })
    }

    function initProgress1() {
        //给进度赋值
        console.log(progress_value)
        projectprogress.forEach(function (e) {
            var id = "silde" + e.codeValue;
            var ins = slider.render({
                elem: '#' + id
                , input: true //输入框
                , change: function (value) {
                    progress_value[e.codeValue] = value;
                }
            });
            ins.setValue(progress_value[e.codeValue])
        })
        //给项目成员赋值
        var userkeys = Object.keys(user_level);
        var userNames = [];
        userList.forEach(function (elem) {
            if (userkeys.indexOf(elem.id + "") > -1) {
                userNames.push(elem.name);
            }
        })
        $("#memberSelect").val(userNames.join(","));
    }

    initProgress();
    laydate.render({
        elem: '#lxsj',
        theme: 'grid'
        // theme: '#393D49'
        // , calendar: true
    });
    form.on('select(company)', function (d) {
        $.post("/enterprise/getObject?id=" + d.value, function (data) {
            // $("#number1").val(data.data);
            var obj = data.data;
            if (obj != null) {
                $("#contacts").val(obj.manager);
                $("#phone").val(obj.phone);
                rate.render({
                    elem: '#gradeDiv'
                    , value: obj.grade
                    // , half: true //开启半星
                    , readonly: true
                });
                $("#gradeSpan").text(obj.grade + "星");
                $("#grade").val(obj.grade);
            } else {
                $("#contacts").val("");
                $("#phone").val("");
                rate.render({
                    elem: '#gradeDiv'
                    , value: 0
                    // , half: true //开启半星
                    , readonly: true
                });
                $("#gradeSpan").text(0 + "星");
                $("#grade").val(0);
            }
            //rate 重新复制星星就对不齐了
            var rates = document.getElementsByClassName("layui-rate")[0].children;
            for (var i = 0; i < rates.length; i++) {
                rates[i].className = 'layui-inline';
            }

        });
    });
    form.verify({
        // name: [/^[a-zA-Z0-9\u4e00-\u9fa5]{1,40}$/, "项目名只能是长度20内的数字|字母|汉字"],
        numberOrEmpty: function (value) {
            if (value != '') {
                if (!/\d+(,\d+)*(.[0-9]{1,2})?/.test(value)) {
                    return '请输入正确的数字';
                }
            }
        }
    })
    form.on("submit(addProject)", function (data) {
        console.log(data);
        console.log(user_level);
        console.log(progress_value);
        console.log(user_level.length, progress_value.length)
        //弹出loading
        // var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var field = Object.assign(data.field, {
                'members': JSON.stringify(user_level),
                'progress': JSON.stringify(progress_value)
            })
        ;
        var url = '/project/add';
        if ($("#id").val() !== "") {
            url = '/project/edit';
        }
        $.ajax({
            url: url,
            method: 'POST',
            data: field,
            dataType: 'json',
            success: function (res) {
                if (res.data) {
                    // layer.close(index);
                    layer.msg("修改成功！");
                    //刷新父页面
                    parent.location.reload();
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (e) {
                layer.msg(e.msg);
            }
        })
        return false;
    })

    var renderMemberTable = function (user) {
        var html = "";
        userLevel.forEach(function (e) {
            var checkValue = user_level[user.id];

            if (e.codeValue == checkValue) {
                html += "<input type='radio' name='" + e.code + user.id + "' title='" + e.codeName + "' value='" + e.codeValue + "' lay-filter ='" + e.code + user.id + "' checked> ";
            } else {
                html += "<input type='radio' name='" + e.code + user.id + "' title='" + e.codeName + "' value='" + e.codeValue + "' lay-filter ='" + e.code + user.id + "'> ";
            }
        });
        return html;
    }
    var _tools = {
        progress_value: progress_value,
        user_level: user_level,
        initProgress1: initProgress1
    }
    window.atools = _tools;
})