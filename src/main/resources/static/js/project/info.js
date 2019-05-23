layui.config({
    base: '/static/layui'
}).extend({
    selectM: '/extend/selectM',
    tableSelect: '/extend/tableSelect'
}).use(['form', 'layer', 'layedit', 'laydate', 'upload', 'rate', 'slider', 'selectM', 'tableSelect'], function () {
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
    var user_level = [];
    //存放进度
    var progress = {};

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
            // $("#levelSelect").val($("#userLevel").val());//默认选中
        });
        if ($("#companyId") !== '') {
            console.log($("#companyId").val());
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
    console.log(projectprogress)
    console.log(userLevel)
    //这是初始化项目经理和项目成员的
    var userList;
    $.post("/user/listDataSelect", {
        available: 1
    }, function (data) {
        userList = data.data;
        //渲染表格要放到这，不然容易出错！
        initManagerAndMembers(userList)
    });
    var membersTag;

    function initManagerAndMembers(users) {
        //渲染项目经理select
        users.forEach(function (e) {
            $("#manager").append("<option value='" + e.id + "'>" + e.name + "</option>");
            // $("#levelSelect").val($("#userLevel").val());//默认选中
        });
        if ($("#managerId") !== '') {
            $("#manager").val($("#managerId").val())
        }
        form.render('select');//刷新select选择框渲染
        // var members = $("#membersId").val(),
        //     memberArray = members.split(",");
        tableSelect.render({
            elem: '#fuck',	//定义输入框input对象
            checkedKey: 'id', //表格的唯一建值，非常重要，影响到选中状态 必填
            searchKey: 'keyword',	//搜索输入框的name值 默认keyword
            // searchPlaceholder: '关键词搜索',	//搜索输入框的提示文字 默认关键词搜索
            table: {	//定义表格参数，与LAYUI的TABLE模块一致，只是无需再定义表格elem
                url: '/user/listDataSelect',
                cols: [[
                    {type: "checkbox", fixed: "left"},
                    {field: 'id', title: 'id', align: "left", width: 100},
                    {field: 'name', title: '姓名', align: "left", width: 100},
                    {
                        field: '职位', title: '项目编号', templet: function (d) {
                            return renderMemberTable(d);
                        }
                    },
                ]]
            },
            done: function (elem, data) {
                var NEWJSON = []
                layui.each(data.data, function (index, item) {
                    NEWJSON.push(item.name)
                })
                elem.val(NEWJSON.join(","))
            }
        })
        userList.forEach(function (user) {
            form.on('radio(userlevel' + user.id + ')', function (data) {
                user_level.push({id: user.id, value: data.value});
            });
        })
        var renderMemberTable = function (user) {
            // var html = "<select  id='"+user.id+"' lay-filter='company' lay-verify='required'>";
            // userLevel.forEach(function (e) {
            //     html += "<option value='" + e.codeValue + "'>" + e.codeName + "</option>";
            // });
            // html += "</select>";
            var html = "";
            userLevel.forEach(function (e) {
                html += "<input type='radio' name='" + e.code + user.id + "' title='" + e.codeName + "' value='" + e.codeValue + "' lay-filter ='" + e.code + user.id + "'> ";
            });

            return html;
        }
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
            , half: true //开启半星
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
    projectprogress.forEach(function (e) {
        var id = "silde" + e.codeValue;
        var html = "<div class='layui-form-item'>";
        html += "<label class='layui-form-label'>" + e.codeName + "</label>";
        html += "<div class='layui-input-block' style='padding: 15px 80px 0 20px;'>";
        html += "<div id='" + id + "' class='demo-slider' ></div>";
        html + "</div></div>"
        $("#sliders").append(html);
        progress[e.codeValue] = 0;
        // $(".sliders").append("fuck shit:<div  class='demo-slider' id='" + id + "' style='padding: 30px 80px 0 20px;'></div>");
        //开启输入框
        slider.render({
            elem: '#' + id
            , input: true //输入框
            , change: function (value) {
                progress[e.codeValue] = value;
            }
        });
    })

    laydate.render({
        elem: '#lxsj',
        theme: 'grid'
        // theme: '#393D49'
        // , calendar: true
    });
    // var sliderChange = function (value, values) {
    //     slider.render({
    //         elem: '#slide'
    //         , step: 20 //步长
    //         , showstep: true //开启间隔点
    //         , value: value
    //         , tipvalues: values
    //         , change: function (value) {
    //             sliderChange(value, values);
    //             $("#progress").val(value)
    //         }
    //     });
    // }
    // //这是初始化进度的
    // var initParam = sessionStorage.getItem("initParam");
    // initParam = JSON.parse(initParam);
    // var projectProgress = initParam.codeMap.projectprogress;
    // var col = 100 / (projectProgress.length - 1);
    // // {0: '未开始', 20: '简历筛选', 40: '初试', 60: '复试', 80: '发送offer', 100: "面试完成"}
    // var values = {};
    // values[0] = projectProgress[0]['codeName'];
    // for (var i = 1; i < projectProgress.length; i++) {
    //     values[(col * i)] = projectProgress[i]['codeName'];
    // }
    // sliderChange(0, values);
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
                    , half: true //开启半星
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
                    , half: true //开启半星
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
        name: [/^[a-zA-Z0-9\u4e00-\u9fa5]{1,40}$/, "项目名只能是长度20内的数字|字母|汉字"],
        numberOrEmpty: function (value) {
            if (value != '') {
                if (!/\d+(,\d+)*(.[0-9]{1,2})?/.test(value)) {
                    return '请输入正确的数字';
                }
            }
        }
    })
    form.on("submit(addProject)", function (data) {
        // 获取 二维码canvas 对象
        console.log(data);
        console.log(user_level);
        console.log(progress);
        //弹出loading
        // var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
        // var field = Object.assign(data.field, {
        //         'members': members.join(",")
        //     })
        // ;
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
})