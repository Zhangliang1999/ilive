var tempPhoneNum;
function fetchDataByQType(obj) {
	var qtype = $(obj).attr("qtype");
	qtype = parseInt(qtype);
	var isNeed = parseInt($(obj).attr("isNeed"));
	var retVal = {};
	retVal.needMsgValid=0;
	retVal.subValue="";
	var needMsgValid =0;
	switch (qtype) {
		case 7:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 5:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 6:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("textarea").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 8:
			retVal.dataOrder=$(obj).attr("qorder");
			var checkObj = $(obj).find(".mui-radio input[type=radio]:checked");
			// console.log(checkObj.length);
			var dataValue = checkObj.prev().html();
			retVal.dataValue = dataValue;
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 20:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 12:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 10:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 11:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else if(isNeed==1 && $.trim(retVal.dataValue)!=""){
				if(!isEmail(retVal.dataValue)) {
					retVal.retFlag = false;
					retVal.msg = "邮箱格式不正确";
				}else{
					retVal.retFlag = true;
				}
			}else {
				retVal.retFlag = true;
			}
			break;
		case 1:
			retVal.dataOrder=$(obj).attr("qorder");
			var checkObj = $(obj).find(".mui-radio input[type=radio]:checked");
			var dataValue = checkObj.prev().text();
			retVal.dataValue = dataValue;
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if( isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 2:
			retVal.dataOrder=$(obj).attr("qorder");
			var checkObj = $(obj).find(".mui-checkbox input[type=checkbox]:checked");
			var dataValue = "";
			console.log("length:"+checkObj.length);
			if(checkObj.length>0) {
				checkObj.each(function(index,dom){
					dataValue = dataValue + $(dom).prev().text() + ","
				});
			}	
			retVal.dataValue = dataValue;
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			if( isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 27 :
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().find('.titleQ').text();
			retVal.dataModelId=$(obj).attr("modelId");
			needMsgValid = $(obj).attr("needMsgValid");
			tempPhoneNum = retVal.dataValue;
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = $(obj).prev().find('.titleQ').html()+"必须填写";
			}else if(isNeed==1 && $.trim(retVal.dataValue)!=""){
				var phoneNumber = $(obj).find("input").val();
				if(!isPoneAvailable(phoneNumber)) {
					retVal.retFlag = false;
					retVal.msg = "手机号格式不正确";
				}else{
					retVal.retFlag = true;
				}
			}else if(needMsgValid==1) {
				retVal.needMsgValid=1;
				var phoneNumber = $(obj).find("input").val();
				if(!isPoneAvailable(phoneNumber)) {
					retVal.retFlag = false;
					retVal.msg = "手机号格式不正确";
				}else{
					retVal.subValue=$('#input_'+retVal.dataModelId).val();
					retVal.retFlag = true;
					tempPhoneNum = retVal.subValue;
				}
			}else {
				retVal.retFlag = true;
			}
			console.log("手机号"+tempPhoneNum);
			break;
		case 271 : 
			retVal.needMsgValid=needMsgValid
			retVal.dataValue=$(obj).find("input").val();
			if(isNeed==1 && $.trim(retVal.dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = "验证码必须填写";
			}else {
				//验证是否正确
				$.ajax({
					type : "post",//请求方式 get/post
					url : "http://"+tomcatBase+"/ilive/appuser/getIdentityCode.jspx",
					dataType : "jsonp",
					jsonp : "callback",
					cache : false,
					async:false,
					data : {
						phoneNum:tempPhoneNum,
						type:'_form',
						identityCode:retVal.dataValue
					},
					success:function(res){
						console.log(res);
						if(res.code == 0){
							retVal.retFlag = true;
						}else{
							retVal.retFlag = false;
							retVal.msg = res.msg;
						}
					}
				});
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
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 8:
			retVal.dataOrder=$(obj).attr("qorder");
			var checkObj = $(obj).find(".mui-radio input[type=radio]:checked");
			// console.log(checkObj.length);
			var dataValue = checkObj.prev().html();
			retVal.dataValue = dataValue;
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 20:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 12:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 10:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		case 11:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
				if(isNeed==1 && $.trim(dataValue)=="") {
				retVal.retFlag = false;
				retVal.msg = dataTitle+"必须填写";
			}else {
				retVal.retFlag = true;
			}
			break;
		default:
			break;
	}		
	return retVal;
}

function getCode(obj){
	var modelId = $(obj).prev('.mui-card').eq(0).attr("modelId");
	var inputObj = $('#mob_'+modelId);
	var phoneNum = inputObj.val();
	if(phoneNum=="") {
		alert("手机号必须填写");
	}else if(!isPoneAvailable(phoneNum.trim())) {
		alert("手机号格式不正确");
	}else {
		settime("msgBtn_"+modelId);
		$.ajax({
			url : "http://"+tomcatBase+"/ilive/appuser/sendmsg.jspx",
			dataType: "jsonp",
			jsonp: "callback",
			data : {
				phoneNum : phoneNum.trim(),
				type : "_form"
			},
			dataType : "json",
			success : function(data){
				// mui.alert("已通知发送,稍后请查收!");
			},
			error : function(){
				
			}
		});
	}

}


function isPoneAvailable(str) {
	var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
	if (!myreg.test(str)) {
		return false;
	} else {
		return true;
	}
}
function isEmail(str) {
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if (!myreg.test(str)) {
		return false;
	} else {
		return true;
	}
}
var countdown = 120;
function settime(id) {
	var obj = $('#'+id);
	if (countdown == 0) {
		obj.removeAttr("disabled");
		obj.html("获取验证码");
		countdown = 120;
		return;
	} else {
		obj.attr("disabled", true);
		obj.html("剩余"+countdown + "s") ;
		countdown--;
	}
	setTimeout(function() {
		settime(id)
	}, 1000);
}