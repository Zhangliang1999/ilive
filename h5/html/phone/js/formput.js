function fetchDataByQType(obj) {
    var qtype = $(obj).attr("qtype");
    qtype = parseInt(qtype);
    var isNeed = parseInt($(obj).attr("isNeed"));
    var retVal = {};
    retVal.needMsgValid = 0;
    retVal.subValue = "";
    retVal.dataOrder = $(obj).attr("qorder");
    retVal.dataTitle = $(obj).data('name');
    retVal.dataModelId = $(obj).attr("modelId");
    switch (qtype) {
        case 1: //单选
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            var checkObj = $(obj).find("input[type=radio]:checked");
            var dataValue = checkObj.prev().html();
            retVal.dataValue = dataValue;
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 2: //多选
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            var checkObj = $(obj).find("input[type=checkbox]:checked");
            var dataValue = "";
            console.log("length:" + checkObj.length);
            if (checkObj.length > 0) {
                checkObj.each(function (index, dom) {
                    dataValue = dataValue + $(dom).prev().text() + ","
                });
            }
            retVal.dataValue = dataValue;
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 5: //单行
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            retVal.dataValue = $(obj).find("input").val();
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 6: //多行
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            retVal.dataValue = $(obj).find("textarea").val();
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 7: //姓名
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            retVal.dataValue = $(obj).find("input").val();
            //retVal.dataTitle = $(obj).prev().find('.titleQ').text();
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 8: //性别
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            var checkObj = $(obj).find("input[type=radio]:checked");
            // console.log(checkObj.length);
            var dataValue = checkObj.prev().html();
            retVal.dataValue = dataValue;
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        // case 20:
        //     retVal.dataOrder = $(obj).attr("qorder");
        //     retVal.dataValue = $(obj).find("button").text();
        //     retVal.dataTitle = $(obj).data('name');
        //     retVal.dataModelId = $(obj).attr("modelId");
        //     if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
        //         retVal.retFlag = false;
        //         retVal.msg = retVal.dataTitle + "必须填写";
        //     } else {
        //         retVal.retFlag = true;
        //     }
        //     break;
        // case 12:
        //     retVal.dataOrder = $(obj).attr("qorder");
        //     retVal.dataValue = $(obj).find("button").text();
        //     retVal.dataTitle = $(obj).data('name');
        //     retVal.dataModelId = $(obj).attr("modelId");
        //     if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
        //         retVal.retFlag = false;
        //         retVal.msg = retVal.dataTitle + "必须填写";
        //     } else {
        //         retVal.retFlag = true;
        //     }
        //     break;
        // case 10:
        //     retVal.dataOrder = $(obj).attr("qorder");
        //     retVal.dataValue = $(obj).find("input").val();
        //     retVal.dataTitle = $(obj).data('name');
        //     retVal.dataModelId = $(obj).attr("modelId");
        //     if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
        //         retVal.retFlag = false;
        //         retVal.msg = retVal.dataTitle + "必须填写";
        //     } else {
        //         retVal.retFlag = true;
        //     }
        //     break;
        case 11: //邮箱
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            retVal.dataValue = $(obj).find("input").val();
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else if (isNeed == 1 && $.trim(retVal.dataValue) != "") {
                if (!isEmail(retVal.dataValue)) {
                    retVal.retFlag = false;
                    retVal.msg = "邮箱格式不正确";
                } else {
                    retVal.retFlag = true;
                }
            } else {
                retVal.retFlag = true;
            }
            break;
        case 27 : //手机验证
            //retVal.dataOrder = $(obj).attr("qorder");
            //retVal.dataTitle = $(obj).data('name');
            //retVal.dataModelId = $(obj).attr("modelId");
            retVal.dataValue = $(obj).find("input").val();
            retVal.needMsgValid = $(obj).attr("needMsgValid");
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = retVal.dataTitle + "必须填写";
            } else if (isNeed == 1 && $.trim(retVal.dataValue) != "") {
                var phoneNumber = $(obj).find("input").val();
                if (!isPoneAvailable(phoneNumber)) {
                    retVal.retFlag = false;
                    retVal.msg = "手机号格式不正确";
                } else {
                    retVal.retFlag = true;
                }
            }   else {
                retVal.retFlag = true;
            }
            break;
        case 271 :
            retVal.dataValue = $(obj).find("input").val();
            if (isNeed == 1 && $.trim(retVal.dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = "验证码不能为空";
            } else {
                retVal.retFlag = true;
            }
            break;
        default:
            break;
    }
    return retVal;
}

// 校验规则
function validQType() {
    var qtype = $(obj).attr("qtype");
    qtype = parseInt(qtype);
    var isNeed = parseInt($(obj).attr("isNeed"));
    var retVal = {};
    switch (qtype) {
        case 7:
            retVal.dataOrder = $(obj).attr("qorder");
            retVal.dataValue = $(obj).find("input").val();
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 8:
            retVal.dataOrder = $(obj).attr("qorder");
            var checkObj = $(obj).find(".radio input[type=radio]:checked");
            // console.log(checkObj.length);
            var dataValue = checkObj.prev().html();
            retVal.dataValue = dataValue;
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 20:
            retVal.dataOrder = $(obj).attr("qorder");
            retVal.dataValue = $(obj).find("button").text();
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 12:
            retVal.dataOrder = $(obj).attr("qorder");
            retVal.dataValue = $(obj).find("button").text();
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 10:
            retVal.dataOrder = $(obj).attr("qorder");
            retVal.dataValue = $(obj).find("input").val();
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        case 11:
            retVal.dataOrder = $(obj).attr("qorder");
            retVal.dataValue = $(obj).find("input").val();
            retVal.dataTitle = $(obj).prev().html();
            retVal.dataModelId = $(obj).attr("modelId");
            if (isNeed == 1 && $.trim(dataValue) == "") {
                retVal.retFlag = false;
                retVal.msg = dataTitle + "必须填写";
            } else {
                retVal.retFlag = true;
            }
            break;
        default:
            break;
    }
    return retVal;
}

function getCode(obj) {
    var modelId = $(obj).prev('.card').eq(0).attr("modelId");
    var inputObj = $('#mob_' + modelId);
    var phoneNum = inputObj.val();
    if (phoneNum == "") {
        layerOpen('手机号必须填写')
    } else if (!isPoneAvailable(phoneNum.trim())) {
        layerOpen('手机号格式不正确')
    } else {
        $.ajax({
            url: "http://" + tomcatBase + "/ilive/appuser/sendmsg.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            data: {
                phoneNum: phoneNum.trim(),
                type: "_form"
            },
            dataType: "json",
            success: function (data) {
                settime("msgBtn_" + modelId);
                // mui.alert("已通知发送,稍后请查收!");
            },
            error: function () {

            }
        });
    }

}