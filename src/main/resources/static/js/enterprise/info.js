layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'rate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        rate = layui.rate
    ;
    var gradeValue = $("#grade").val()

    function initRate() {
        var rates = document.getElementsByClassName("layui-rate")[0].children;
        for (var i = 0; i < rates.length; i++) {
            rates[i].className = 'layui-inline';
        }
    }

    rate.render({
        elem: '#gradeDiv'
        , value: gradeValue
        // , half: true //开启半星
        , text: true
        , setText: function (value) {
            this.span.text(value + "星");
            $("#grade").val(value);
        }
    });
    //rate 重新复制星星就对不齐了
    initRate();

    //页面下拉框赋值------------开始
    var initParam = sessionStorage.getItem("initParam");
    var codes = JSON.parse(initParam)['codeMap'];
    var qyrs = codes['qyrs'], clsj = codes['clsj'], qyxz = codes['qyxz'], zyyt = codes['zyyt'],
        jyzt = codes['jyzt'], ltdjed = codes['ltdjed'], gzqy = codes['gzqy'], gltxjqcd = codes['gltxjqcd'],
        nrylsl = codes['nrylsl'], jxry = codes['jxry'], yqbzq = codes['yqbzq'], zltsl = codes['zltsl'],
        ldjf = codes['ldjf'], swjf = codes['swjf'], xytc = codes['xytc'], qtjfx = codes['qtjfx'];
    //评分标准
    var startRating = codes['starrating'];

    qyrs.forEach(function (e) {
        $("#qyrs").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    clsj.forEach(function (e) {
        $("#clsj").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    qyxz.forEach(function (e) {
        $("#qyxz").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    zyyt.forEach(function (e) {
        $("#zyyt").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    jyzt.forEach(function (e) {
        $("#jyzt").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    ltdjed.forEach(function (e) {
        $("#ltdjed").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    gzqy.forEach(function (e) {
        $("#gzqy").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    gltxjqcd.forEach(function (e) {
        $("#gltxjqcd").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    nrylsl.forEach(function (e) {
        $("#nrylsl").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    jxry.forEach(function (e) {
        $("#jxry").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    yqbzq.forEach(function (e) {
        $("#yqbzq").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    zltsl.forEach(function (e) {
        $("#zltsl").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    ldjf.forEach(function (e) {
        $("#ldjf").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    swjf.forEach(function (e) {
        $("#swjf").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    xytc.forEach(function (e) {
        $("#xytc").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    qtjfx.forEach(function (e) {
        $("#qtjfx").append("<option value='" + e.codeValue + ":" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    form.render('select');//刷新select选择框渲染
    setTimeout(function () {
        console.log($("#grade").val())
        rate.render({
            elem: '#gradeDiv'
            , value: $("#grade").val()
            // , half: true //开启半星
            , text: true
            , setText: function (value) {
                this.span.text(value + "星");
            }
        });
        initRate();
        var addoredit = $("#addoredit").val();
        console.log(addoredit)
        console.log(addoredit == "edit")
        if (addoredit == 'edit') {
            $("#coding").click();
        }
        $("#qyrs1").val() == "" ? "" : $("#qyrs").val($("#qyrs1").val());
        $("#clsj1").val() == "" ? "" : $("#clsj").val($("#clsj1").val());
        $("#qyxz1").val() == "" ? "" : $("#qyxz").val($("#qyxz1").val());
        $("#zyyt1").val() == "" ? "" : $("#zyyt").val($("#zyyt1").val());
        $("#jyzt1").val() == "" ? "" : $("#jyzt").val($("#jyzt1").val());
        $("#ltdjed1").val() == "" ? "" : $("#ltdjed").val($("#ltdjed1").val());
        $("#gzqy1").val() == "" ? "" : $("#gzqy").val($("#gzqy1").val());
        $("#gltxjqcd1").val() == "" ? "" : $("#gltxjqcd").val($("#gltxjqcd1").val());
        $("#nrylsl1").val() == "" ? "" : $("#nrylsl").val($("#nrylsl1").val());
        $("#jxry1").val() == "" ? "" : $("#jxry").val($("#jxry1").val());
        $("#yqbzq1").val() == "" ? "" : $("#yqbzq").val($("#yqbzq1").val());
        $("#zltsl1").val() == "" ? "" : $("#zltsl").val($("#zltsl1").val());
        $("#ldjf1").val() == "" ? "" : $("#ldjf").val($("#ldjf1").val());
        $("#swjf1").val() == "" ? "" : $("#swjf").val($("#swjf1").val());
        $("#xytc1").val() == "" ? "" : $("#xytc").val($("#xytc1").val());
        $("#qtjfx1").val() == "" ? "" : $("#qtjfx").val($("#qtjfx1").val());
        form.render('select');//刷新select选择框渲染
    }, 300)

    $("#rating").on('click', function () {
        var qyrs = $("#qyrs").val().split(":")[1],
            clsj = $("#clsj").val().split(":")[1],
            qyxz = $("#qyxz").val().split(":")[1],
            zyyt = $("#zyyt").val().split(":")[1],
            jyzt = $("#jyzt").val().split(":")[1],
            ltdjed = $("#ltdjed").val().split(":")[1],
            gzqy = $("#gzqy").val().split(":")[1],
            gltxjqcd = $("#gltxjqcd").val().split(":")[1],
            nrylsl = $("#nrylsl").val().split(":")[1],
            jxry = $("#jxry").val().split(":")[1],
            yqbzq = $("#yqbzq").val().split(":")[1],
            zltsl = $("#zltsl").val().split(":")[1],
            ldjf = $("#ldjf").val().split(":")[1],
            swjf = $("#swjf").val().split(":")[1],
            xytc = $("#xytc").val().split(":")[1],
            qtjfx = $("#qtjfx").val().split(":")[1];
        var value = parseInt(qyrs) + parseInt(clsj) + parseInt(qyxz) + parseInt(zyyt) + parseInt(jyzt) + parseInt(ltdjed)
            + parseInt(gzqy) + parseInt(gltxjqcd) + parseInt(nrylsl) + parseInt(jxry) + parseInt(yqbzq) + parseInt(zltsl)
            + parseInt(ldjf) + parseInt(swjf) + parseInt(xytc) + parseInt(qtjfx);
        console.log(value);
        $("#pjzf").val(value);
        for (var i = 0; i < startRating.length; i++) {
            var startValue = startRating[i].codeName.split("~")[0];
            var endValue = startRating[i].codeName.split("~")[1];
            var rateValue = 0;
            if (value >= parseInt(startValue) && value <= parseInt(endValue)) {
                rateValue = startRating[i]['codeMark'];
                rate.render({
                    elem: '#gradeDiv'
                    , value: rateValue
                    // , half: true //开启半星
                    , text: true
                    , setText: function (value) {
                        this.span.text(value + "星");
                        $("#grade").val(value);
                    }
                });
                break;
            }
        }
        return false;
    })

    form.verify({
        // name: [/^[a-zA-Z0-9\u4e00-\u9fa5]{1,40}$/, "项目名只能是长度20内的数字|字母|汉字"],
        //1、编号构成：年度+部门+项目类型+序号+追加
        numberOrEmpty: function (value) {
            if (value != '') {
                if (!/\d+(,\d+)*(.[0-9]{1,2})?/.test(value)) {
                    return '请输入正确的数字';
                }
            }
        }
    })

    form.on("submit(addProject)", function (data) {
        // console.table(data.field);
        var gradeValue = $("#grade").val();
        if (gradeValue == 0) {
            layer.msg("请点击按钮给企业评分");
            return false;
        }
        var canvas = document.getElementsByTagName("canvas")[0];
        if (canvas === undefined) {
            layer.msg("请点击生成企业二维码");
            return false;
        }
        var index = top.layer.msg('数据保存中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var form = document.querySelector(".layui-form");
        var formData = new FormData(form);
        var logoChange = $("#logoChange").val();
        var bs64 = "";
        if (logoChange == 'true') {
            bs64 = canvas.toDataURL('image/jpg');
        }
        formData.append("qrcode", bs64);
        if ($("#id").val() === "") {
            $.ajax({
                url: '/enterprise/add',
                method: 'POST',
                processData: false,  //必须false才会避开jQuery对 formdata 的默认处理
                contentType: false,  //必须false才会自动加上正确的Content-Type
                data: formData,
                success: function (res) {
                    layer.close(index);
                    if (res.data) {
                        layer.msg("企业信息保存成功");
                        parent.location.reload();
                    } else {
                        layer.msg(res.msg);
                    }
                },
                error: function (e) {
                    layer.msg(e.msg);
                }
            })
        } else {
            $.ajax({
                url: '/enterprise/edit',
                method: 'POST',
                processData: false,  //必须false才会避开jQuery对 formdata 的默认处理
                contentType: false,  //必须false才会自动加上正确的Content-Type
                data: formData,
                success: function (res) {
                    if (res.data) {
                        layer.msg("企业信息修改成功");
                        parent.location.reload();
                    } else {
                        layer.msg(res.msg);
                    }
                },
                error: function (e) {
                    layer.msg(e.msg);
                }
            })
        }
        return false;
    })
    $("#logo").on('click', function () {
        $("#logoFile").trigger('click');
    })
    $("#logoFile").change(function (event) {
        var files = event.target.files, file;
        if (files && files.length > 0) {
            // 获取目前上传的文件
            file = files[0];// 文件大小校验的动作
            if (file.size > 1024 * 1024 * 2) {
                alert('图片大小不能超过 2MB!');
                return false;
            }
            // 获取 window 的 URL 工具
            var URL = window.URL || window.webkitURL;
            // 通过 file 生成目标 url
            var imgURL = URL.createObjectURL(file);
            //用attr将img的src属性改成获得的url
            $("#logo").attr("src", imgURL);
            $("#logoChange").val("true");
            setTimeout(function () {
                window.createCode();
            }, 500);
            // 使用下面这句可以在内存中释放对此 url 的伺服，跑了之后那个 URL 就无效了
            // URL.revokeObjectURL(imgURL);
        }
    })
})