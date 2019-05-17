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
        if ($("#id").val() === "") {
            $.ajax({
                url: '/enterprise/add',
                method: 'POST',
                data: data.field,
                dataType: 'json',
                success: function (res) {
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