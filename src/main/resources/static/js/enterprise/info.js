layui.config({
    base: '/static/layui'
}).use(['form', 'layer', 'rate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        rate = layui.rate
    ;
    var gradeValue = $("#grade").val()
    rate.render({
        elem: '#gradeDiv'
        , value: gradeValue
        , half: true //开启半星
        , text: true
        , setText: function (value) {
            this.span.text(value + "星");
            $("#grade").val(value);
        }
    });
    //rate 重新复制星星就对不齐了
    var rates = document.getElementsByClassName("layui-rate")[0].children;
    for (var i = 0; i < rates.length; i++) {
        rates[i].className = 'layui-inline';
    }
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
        $("#qyrs").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    clsj.forEach(function (e) {
        $("#clsj").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    qyxz.forEach(function (e) {
        $("#qyxz").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    zyyt.forEach(function (e) {
        $("#zyyt").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    jyzt.forEach(function (e) {
        $("#jyzt").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    ltdjed.forEach(function (e) {
        $("#ltdjed").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    gzqy.forEach(function (e) {
        $("#gzqy").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    gltxjqcd.forEach(function (e) {
        $("#gltxjqcd").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    nrylsl.forEach(function (e) {
        $("#nrylsl").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    jxry.forEach(function (e) {
        $("#jxry").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    yqbzq.forEach(function (e) {
        $("#yqbzq").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    zltsl.forEach(function (e) {
        $("#zltsl").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    ldjf.forEach(function (e) {
        $("#ldjf").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    swjf.forEach(function (e) {
        $("#swjf").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    xytc.forEach(function (e) {
        $("#xytc").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });
    qtjfx.forEach(function (e) {
        $("#qtjfx").append("<option value='" + e.codeMark + "'>" + e.codeName + "</option>");
    });

    form.render('select');//刷新select选择框渲染

    $("#rating").on('click', function () {
        var qyrs = $("#qyrs").val(), clsj = $("#clsj").val(), qyxz = $("#qyxz").val(), zyyt = $("#zyyt").val(),
            jyzt = $("#jyzt").val(), ltdjed = $("#ltdjed").val(), gzqy = $("#gzqy").val(),
            gltxjqcd = $("#gltxjqcd").val(),
            nrylsl = $("#nrylsl").val(), jxry = $("#jxry").val(), yqbzq = $("#yqbzq").val(), zltsl = $("#zltsl").val(),
            ldjf = $("#ldjf").val(), swjf = $("#swjf").val(), xytc = $("#xytc").val(), qtjfx = $("#qtjfx").val();
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
                    , half: true //开启半星
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
        name: [/^[a-zA-Z0-9\u4e00-\u9fa5]{1,40}$/, "项目名只能是长度20内的数字|字母|汉字"],
        //1、编号构成：年度+部门+项目类型+序号+追加
        pNumber: [/^(\d{4})+(XS|JC|YW|HL|CW|XZ|RJ|XX)+(JC|DR|ZR|JF|KF|CX|KR|KJ|NB|WB|BH)+([0-9][0-9][2-9])+$/, "项目编号格式有误！"],
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
        var saveImage = canvas.toDataURL('image/png');
        var bs64 = saveImage;
        var field = Object.assign(data.field, {
                'qrcode': bs64,
            })
        ;
        console.log(field)
        if ($("#id").val() === "") {
            $.ajax({
                url: '/enterprise/add',
                method: 'POST',
                data: field,
                dataType: 'json',
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
                data: data.field,
                dataType: 'json',
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

})