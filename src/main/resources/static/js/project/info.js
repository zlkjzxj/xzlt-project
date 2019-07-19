layui.config({
    base: '/static/layui'
}).extend({
    // selectM: '/extend/selectM',
    tableSelect: '/extend/tableSelect'
}).use(['form', 'layer', 'laytpl', 'laydate', 'upload', 'rate', 'slider', 'table', 'tableSelect'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate,
        $ = layui.jquery,
        rate = layui.rate,
        slider = layui.slider,
        laytpl = layui.laytpl
    ;
    var date = new Date();
    var year = date.getFullYear();

    //存放选中成员的级别
    var user_level = {};
    //存放进度
    var progress_value = {};
    //这是初始化项目经理和项目成员的
    var userList = [];
    $.post("/user/listDataSelect", {
        available: 1
    }, function (data) {
        userList = data.data;
        //渲染表格要放到这，不然容易出错！
        initManagerAndMembers(userList)
    });


    // //这是初始化所属公司的
    // $.post("/enterprise/listDataSelect", {
    //     available: 1
    // }, function (data) {
    //     var enterpriseList = data.data;
    //     enterpriseList.forEach(function (e) {
    //         $("#company").append("<option value='" + e.id + "'>" + e.name + "</option>");
    //     });
    //     if ($("#companyId") !== '') {
    //         $("#company").val($("#companyId").val())
    //     }
    //     form.render('select');
    //
    // });

    //这是初始化项目编号的
    function initNumber(number) {
        if (number == '') {
            $.post("/project/getAddSequence?year=" + year, function (data) {
                $("#number").val(data.data);
            });
        } else {
            $("#number").val(number);
        }
    }

    var initParam = sessionStorage.getItem("initParam");
    var codes = JSON.parse(initParam)['codeMap'];
    //人员职位
    var userLevel = codes['userlevel'];
    //项目进度
    var projectprogress = codes['projectprogress'];
    var projectprogressChoice = [];

    var layerOpen = true;
    $("#memberSelect").on('click', function () {
        var elem = $("#memberLabel");
        var t = elem.position().top + "px";
        var l = elem.position().left + elem.outerWidth + "px";
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
                offset: ["350px", "800px"],
                shade: 0,
                success: function (layero, index) {
                    // layero.css("position", "relative").append(layero);
                    // console.log(t);
                    // layero.css("top", t).append(layero);
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

    }

    // var getTpl = progressHtml.innerHTML,
    //     data = { //数据
    //         "title": "Layui常用模块"
    //         , "list": projectprogress
    //     }
    // var progressBoxHtml = "";
    // laytpl(getTpl).render(data, function (html) {
    //     progressBoxHtml = html;
    //     form.render('checkbox');
    // });
    $("#addProgress").on('click', function () {
        // var tableBox = "<form class=\"layui-form\">";
        // var tableBox = "<div class=\" layui-form-item\">"
        // tableBox += "<div class=\"layui-input-block\" >"
        // projectprogress.forEach(function (elem) {
        //     tableBox += '<input  name="memberBox" type="checkbox" value="' + elem.codeValue + '" title="' + elem.codeName + '"/>';
        // })
        // tableBox += "</div>"
        // tableBox += "</div>"
        // tableBox += "</form>"var elem = $("#memberSelect");
        var elem = $("#addProgress");
        console.log(elem)
        console.log(elem.offset())
        var t = elem.offset().top + elem.outerHeight() + "px";
        var l = elem.offset().left + "px";
        var progressChoicValues = [];
        projectprogressChoice.forEach(function (ele) {
            progressChoicValues.push(ele.codeValue);
        });
        var tableBox = '<div  style="left:' + l + ';top:' + t + ';border: 1px solid #d2d2d2;background-color: #fff;">';
        tableBox += "<table  class=\"layui-table\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">";
        projectprogress.forEach(function (elem, index) {
            if (index % 3 == 0) {
                tableBox += "<tr>";
            }
            tableBox += "<td>";
            if (progressChoicValues.indexOf(elem.codeValue) > -1) {
                tableBox += '<input  name="progressBox" type="checkbox" value="' + elem.codeValue + '" style="width: 16px;height: 16px;" checked />' + elem.codeName;
            } else {
                tableBox += '<input  name="progressBox" type="checkbox" value="' + elem.codeValue + '" style="width: 16px;height: 16px;" />' + elem.codeName;
            }
            tableBox += "</td>";
            if (index % 3 == 2) {
                tableBox += "</tr>";
            }
        })
        tableBox += "</table>";
        tableBox += "</div>"
        var index = layer.open({
            type: 1,
            content: tableBox,
            area: ['550px', '400px'],
            fixed: false,
            offset: ["400px", "650px"],
            shade: 0,
            success: function (layero, index) {

            },
            cancel: function (index, layero) {
                layerOpen = true;
            },
            btn: ['选择'],
            yes: function (index, layero) {
                var newChoices = [];
                var checks = layero.find("input[name='progressBox']:checked");
                console.log(checks);
                layero.find("input[name='progressBox']:checked").each(function (d) {
                    var that = $(this).val()
                    projectprogress.forEach(function (elem) {
                        if (elem.codeValue === Number(that) && progressChoicValues.indexOf(Number(that)) < 0) {
                            projectprogressChoice.push(elem);
                            newChoices.push(elem);
                            return false;
                        }
                    })
                });
                console.log(projectprogressChoice)
                console.log(progressChoicValues)
                console.log(newChoices)
                initProgress(newChoices);
                layer.close(index);
            }
        })

    })


    //循环生成项目进度列表
    function initProgress(newChoices) {
        newChoices.forEach(function (e) {
            var id = "silde" + e.codeValue;
            var html = "<div class='layui-form-item'>";
            html += "<div class='layui-inline layui-col-md10'>";
            html += "<label class='layui-form-label'>" + e.codeName + "</label>";
            html += "<div class='layui-input-block' style='padding: 15px 80px 0 0;'>";
            html += "<div id='" + id + "' class='demo-slider' ></div> ";
            html += "</div>";
            html += "</div>";
            html += "<div class='layui-inline layui-col-md1'>";
            html += "<button class='layui-btn layui-btn-sm removeBtn' type='button' value=" + e.codeValue + "><i class='layui-icon'>&#xe640;</i></button>";
            html += "</div>";
            html += "</div>";
            $("#sliders").append(html);
            progress_value[e.codeValue] = 0;
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

    $(document).on('click', '.removeBtn', function () {
        var codeValue = $(this).val();
        console.log(codeValue);
        projectprogressChoice.forEach(function (elem, index) {
            if (elem.codeValue == codeValue) {
                projectprogressChoice.splice(index, 1);
                delete progress_value[codeValue]
            }
        })
        $(this).parent().parent().remove();

    })

    function initProgress1() {
        //给进度赋值
        console.log(progress_value)
        var keys = Object.keys(progress_value);
        projectprogress.forEach(function (elem) {
            if (keys.indexOf(elem.codeValue + "") > -1) {
                projectprogressChoice.push(elem);
            }
        })
        console.log(projectprogressChoice)
        projectprogressChoice.forEach(function (e) {
            var id = "silde" + e.codeValue;
            var html = "<div class='layui-form-item'>";
            html += "<div class='layui-inline layui-col-md10'>";
            html += "<label class='layui-form-label'>" + e.codeName + "</label>";
            html += "<div class='layui-input-block' style='padding: 15px 80px 0 0;'>";
            html += "<div id='" + id + "' class='demo-slider' ></div> ";
            html += "</div>";
            html += "</div>";
            html += "<div class='layui-inline layui-col-md1'>";
            html += "<button class='layui-btn layui-btn-sm removeBtn' type='button' value=" + e.codeValue + "><i class='layui-icon'>&#xe640;</i></button>";
            html += "</div>";
            html += "</div>";
            $("#sliders").append(html);
            // progress_value[e.codeValue] = 0;
            //开启输入框
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

    // initProgress();
    laydate.render({
        elem: '#lxsj',
        theme: 'grid'
        // theme: '#393D49'
        // , calendar: true
    });
    laydate.render({
        elem: '#jssj',
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
            // //rate 重新复制星星就对不齐了
            // var rates = document.getElementsByClassName("layui-rate")[0].children;
            // for (var i = 0; i < rates.length; i++) {
            //     rates[i].className = 'layui-inline';
            // }

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
        var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
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
                    layer.close(index);
                    layer.msg("操作成功！");
                    //刷新父页面
                    parent.location.reload();
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (e) {
                layer.close(index);
                layer.msg(e.msg);
            }
        })
        return false;
    })

    var _tools = {
        progress_value: progress_value,
        user_level: user_level,
        initProgress1: initProgress1,
        initNumber: initNumber,
    }
    window.atools = _tools;
})