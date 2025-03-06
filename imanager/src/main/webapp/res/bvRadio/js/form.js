//单选框 type=1
var type1Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue",
		"optValue", "helpTxt", "helpPosition" ];
// 多选框 type=2
var type2Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue",
		"optValue", "helpTxt", "helpPosition" ];
// 图片单选框 type=3
var type3Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue",
		"optValue", "optImgUrl", "helpTxt", "helpPosition" ];
// 图片多选框 type=4
var type4Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue",
		"optValue", "optImgUrl", "helpTxt", "helpPosition" ];
// 单行文本 type=5
var type5Array = [ "fieldTitle", "fieldType", "needAnswer", "textSize",
		"defValue", "helpTxt", "helpPosition" ];
// 多行文本 type=6
var type6Array = [ "fieldTitle", "fieldType", "needAnswer", "textSize",
		"areaRows", "areaCols", "defValue", "helpTxt", "helpPosition" ];
// 姓名 type=7
var type7Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 性别 type=8
var type8Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue",
		"optValue", "helpTxt", "helpPosition" ];
// 身份证号 type=9
var type9Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 手机 type=10
var type10Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 邮箱 type=11
var type11Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 地址 type=12
var type12Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 文件上传 type=13
var type13Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 图片上传 type=14
var type14Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition" ];
// 短信验证
var type27Array = [ "fieldTitle", "fieldType", "needAnswer", "helpTxt",
		"helpPosition", "answerVal" ];

// 问题验证
var type37Array = [ "fieldTitle", "fieldType", "needAnswer", "defValue" ];

function submitDiyform(returnFunc) {
	var fieldArray = $("#question_box li.module");
	var fieldCount = 0;
	var diyformName = $("#diyformName").val();
	var diyformId = $("#diyformId").val();
	var resultJson = "{\"liveEventId\":\"" + liveEventId
			+ "\",\"diyformName\":\"" + diyformName + "\",";
	if (diyformId && !isNaN(diyformId)) {
		resultJson += "\"diyformId\":\"" + diyformId + "\",";
	}
	resultJson += "\"fieldData\":[";
	for (i = 0; i < fieldArray.length; i++) {
		var fieldObj = $("#question_box li.module:eq(" + i + ")");
		resultJson += "{";
		var fieldType = fieldObj.find("input[name='fieldType']").val();
		var diymodelId = $("#diymodelId").val();
		if (diymodelId && !isNaN(diymodelId)) {
			resultJson += "\"diymodelId\":\"" + diymodelId + "\",";
		}
		switch (fieldType) {
		case "1":
			for (m = 0; m < type1Array.length; m++) {
				var jsonkey = type1Array[m];
				var jsonValue = "";
				if (jsonkey == 'defValue') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).parent().find(
									"*[name='optValue']").val().replace(
									/(^\s*)|(\s*$)/g, "") != '') {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").val();
							} else {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					} else {
						jsonValue = "";
					}
				} else if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "2":
			for (m = 0; m < type2Array.length; m++) {
				var jsonkey = type2Array[m];
				var jsonValue = "";
				if (jsonkey == 'defValue') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).parent().find(
									"*[name='optValue']").val().replace(
									/(^\s*)|(\s*$)/g, "") != '') {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").val();
							} else {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					} else {
						jsonValue = "";
					}
				} else if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "3":
			for (m = 0; m < type3Array.length; m++) {
				var jsonkey = type3Array[m];
				var jsonValue = "";
				if (jsonkey == 'defValue') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).parent().find(
									"*[name='optValue']").val().replace(
									/(^\s*)|(\s*$)/g, "") != '') {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").val();
							} else {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					} else {
						jsonValue = "";
					}
				} else if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "4":
			for (m = 0; m < type4Array.length; m++) {
				var jsonkey = type4Array[m];
				var jsonValue = "";
				if (jsonkey == 'defValue') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).parent().find(
									"*[name='optValue']").val().replace(
									/(^\s*)|(\s*$)/g, "") != '') {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").val();
							} else {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					} else {
						jsonValue = "";
					}
				} else if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "5":
			for (m = 0; m < type5Array.length; m++) {
				var jsonkey = type5Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "6":
			for (m = 0; m < type6Array.length; m++) {
				var jsonkey = type6Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "7":
			for (m = 0; m < type7Array.length; m++) {
				var jsonkey = type7Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;

		case "8":
			for (m = 0; m < type8Array.length; m++) {
				var jsonkey = type8Array[m];
				var jsonValue = "";
				if (jsonkey == 'defValue') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).parent().find(
									"*[name='optValue']").val().replace(
									/(^\s*)|(\s*$)/g, "") != '') {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").val();
							} else {
								jsonValue += $(jsonObj[j]).parent().find(
										"*[name='optValue']").html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					} else {
						jsonValue = "";
					}
				} else if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "9":
			for (m = 0; m < type9Array.length; m++) {
				var jsonkey = type9Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "10":
			for (m = 0; m < type10Array.length; m++) {
				var jsonkey = type10Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "11":
			for (m = 0; m < type11Array.length; m++) {
				var jsonkey = type11Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "12":
			for (m = 0; m < type12Array.length; m++) {
				var jsonkey = type12Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "13":
			if (jsonkey == 'needAnswer') {
				var jsonObj = $("#question_box li.module:eq(" + i + ")").find(
						"*[name='" + jsonkey + "']:checked");
				if (jsonObj.length) {
					jsonValue += "1";
				} else {
					jsonValue += "0";
				}
			} else {
				for (m = 0; m < type13Array.length; m++) {
					var jsonkey = type13Array[m];
					var jsonValue = "";
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
					resultJson += "\"" + jsonkey + "\":\""
							+ $.trim(jsonValue.replace(/<[^<>]+>/g, ""))
							+ "\",";
				}
			}
			break;
		case "14":
			if (jsonkey == 'needAnswer') {
				var jsonObj = $("#question_box li.module:eq(" + i + ")").find(
						"*[name='" + jsonkey + "']:checked");
				if (jsonObj.length) {
					jsonValue += "1";
				} else {
					jsonValue += "0";
				}
			} else {
				for (m = 0; m < type14Array.length; m++) {
					var jsonkey = type14Array[m];
					var jsonValue = "";
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
					resultJson += "\"" + jsonkey + "\":\""
							+ $.trim(jsonValue.replace(/<[^<>]+>/g, ""))
							+ "\",";
				}
			}
			break;
		case "27":
			for (m = 0; m < type27Array.length; m++) {
				var jsonkey = type27Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $(jsonObj[j]).val();
							} else {
								jsonValue += $(jsonObj[j]).html();
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		case "37":
			for (m = 0; m < type37Array.length; m++) {
				var jsonkey = type37Array[m];
				var jsonValue = "";
				if (jsonkey == 'needAnswer') {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']:checked");
					if (jsonObj.length) {
						jsonValue += "1";
					} else {
						jsonValue += "0";
					}
				} else {
					var jsonObj = $("#question_box li.module:eq(" + i + ")")
							.find("*[name='" + jsonkey + "']");
					if (jsonObj.length) {
						for (j = 0; j < jsonObj.length; j++) {
							if ($(jsonObj[j]).val().replace(/(^\s*)|(\s*$)/g,
									"") != '') {
								jsonValue += $.trim($(jsonObj[j]).val());
							} else {
								jsonValue += $.trim($(jsonObj[j]).html());
							}
							if (j != jsonObj.length - 1) {
								jsonValue += "@;@";
							}
						}
					}
				}
				resultJson += "\"" + jsonkey + "\":\""
						+ $.trim(jsonValue.replace(/<[^<>]+>/g, "")) + "\",";
			}
			break;
		default:
			break;
		}
		resultJson += "\"fieldKey\":\"field" + i + "\",";
		resultJson += "\"order\":\"" + i + "\"";
		resultJson += "}";
		if (i != fieldArray.length - 1) {
			resultJson += ",";
		}
		fieldCount++;
	}
	resultJson += "],\"fieldCount\":\"" + fieldCount + "\"}";
	console.log(resultJson);
	$("#preview").html(resultJson);
	$.post(diyformSubmitUrl, {
		resultJson : resultJson
	}, function(data, status) {
		// returnFunc(data.diyformId);
		if (data.status == "success") {
			alert("表单保存成功");
		}
	});
}

var selectQType = 0;
var selectQLi;
$(document)
		.ready(
				function(e) {
					$('.ul-tool').find('li').click(function(e) {
						var fieldType = $(this).attr('fieldType');
						insertOrigin(fieldType);
					});
					$(document)
							.keydown(
									function(e) {
										var doPrevent;
										if (e.keyCode == 8) {
											var d = e.srcElement || e.target;
											if (d.tagName.toUpperCase() == 'INPUT'
													|| d.tagName.toUpperCase() == 'TEXTAREA'
													|| d.tagName.toUpperCase() == 'SPAN'
													|| d.tagName.toUpperCase() == 'DIV') {
												doPrevent = d.readOnly
														|| d.disabled;
											} else
												doPrevent = true;
										} else
											doPrevent = false;

										if (doPrevent)
											e.preventDefault();
									});
					initBoxTool();
					addrInit();
					$("#question_box")
							.dragsort(
									{
										dragSelectorExclude : "input, textarea, a[href], .T_edit, .T_edit_min, select"
									});
				});

function insertOrigin(fieldType) {
	var $cloneDanXuanHTML = $('#questionTemplate').find(
			'#fieldType_' + fieldType).clone(true);
	$cloneDanXuanHTML.show();
	$('#question_box').append($cloneDanXuanHTML);
	addrInit();
	initCloneBoxTool($cloneDanXuanHTML);
}
function addrInit() {
	$('#question_box').find(".jlbd").each(function(index, dom) {
		var province = $(dom).find(".province")[0];
		var city = $(dom).find(".city")[0];
		var area = $(dom).find(".area")[0];
		addressInit(province, city, area);
	});
}

function initBoxTool() {
	$('#question_box').on('click', '.T_edit', function(e) {
		$(this).attr('contentEditable', 'true');
	});
	$('#question_box').on('click', '.T_edit_min', function(e) {
		$(this).attr('contentEditable', 'true');
	});
	$('#question_box').find('.setup-group').on('click', '.Del', function(e) {
		if ($(this).parents('.module').eq(0).find('.isChecking').length > 0) {
			$('.right_operate').slideUp("slow");
			$('.jt').css("display", "none");
		}
		$(this).parents('.module').eq(0).remove();
	});
	$('#question_box').find('.setup-group').on('click', '.Bub', function(e) {
		var $cloneDanXuanHTML = $(this).parents('.module').eq(0).clone()
		$('#question_box').append($cloneDanXuanHTML);
		addrInit();
		initCloneBoxTool($cloneDanXuanHTML);
	});
	$('#question_box')
			.find('.operationH')
			.on(
					'click',
					'.cq_add',
					function(e) {
						var danxuanOption = '<li style=""><input name="defValue" type="radio"><span class="T_edit_min" for="" name="optValue" contenteditable="true">选项</span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></li>';
						$(this).parents('.module').eq(0).find('.unstyled')
								.append(danxuanOption);
					});
	$('#question_box')
			.find('.operationH')
			.on(
					'click',
					'.duo_add',
					function(e) {
						var danxuanOption = '<li><input name="defValue" type="checkbox"><span class="T_edit_min" name="optValue" contenteditable="true">选项</span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></li>';
						$(this).parents('.module').eq(0).find('.unstyled')
								.append(danxuanOption);
					});
	$('#question_box').on('click', '.DelEdit', function(e) {
		$(this).parents('li').eq(0).remove();
	});
	$(document).on('click', '.dragZone', function(e) {
		var li = $(this).parents('.module').eq(0);
		selectQLi = li;
		selectQType = $(li).attr('fieldType');
		$('#uploadContentDiv').html('');
		$("#imageUpload").dialog("open");
	});
	// $(document).on('click','.Remark',function() {
	// $(this).siblings('input').show().select();
	// });
	// $(document).on('click','.remarkTexts',function() {
	// $(this).hide();
	// });
	$("#imageUpload").dialog({
		autoOpen : false,
		height : 400,
		width : 500,
		modal : true,
		buttons : {
			"确定" : function() {
				saveImageDialog();
				$(this).dialog("close");
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
		}
	});
}

function initCloneBoxTool(cloneQuestion) {
	$(cloneQuestion).on('click', '.T_edit', function(e) {
		$(this).attr('contentEditable', 'true');
	});
	$(cloneQuestion).on('click', '.T_edit_min', function(e) {
		$(this).attr('contentEditable', 'true');
	});
	$(cloneQuestion).find('.setup-group').on('click', '.Del', function(e) {
		if ($(this).parents('.module').eq(0).find('.isChecking').length > 0) {
			$('.right_operate').slideUp("slow");
			$('.jt').css("display", "none");
		}
		$(this).parents('.module').eq(0).remove();
	});
	$(cloneQuestion).find('.setup-group').on('click', '.Bub', function(e) {
		var $cloneDanXuanHTML = $(this).parents('.module').eq(0).clone()
		$('#question_box').append($cloneDanXuanHTML);
		addrInit();
		initCloneBoxTool($cloneDanXuanHTML);
	});
	$(cloneQuestion)
			.find('.operationH')
			.on(
					'click',
					'.cq_add',
					function(e) {
						var danxuanOption = '<li style=""><input name="defValue" type="radio"><span class="T_edit_min" for="" name="optValue" contenteditable="true">选项</span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></li>';
						$(this).parents('.module').eq(0).find('.unstyled')
								.append(danxuanOption);
					});
	$(cloneQuestion)
			.find('.operationH')
			.on(
					'click',
					'.duo_add',
					function(e) {
						var danxuanOption = '<li><input name="defValue" type="checkbox"><span class="T_edit_min" name="optValue" contenteditable="true">选项</span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></li>';
						$(this).parents('.module').eq(0).find('.unstyled')
								.append(danxuanOption);
					});
	$(cloneQuestion).on('click', '.DelEdit', function(e) {
		$(this).parents('li').eq(0).remove();
	});
	$(document).on('click', '.dragZone', function(e) {
		var li = $(this).parents('.module').eq(0);
		selectQLi = li;
		selectQType = $(li).attr('fieldType');
		$('#uploadContentDiv').html('');
		$("#imageUpload").dialog("open");
	});
	// $(document).on('click','.Remark',function() {
	// $(this).siblings('input').show().select();
	// });
	// $(document).on('click','.remarkTexts',function() {
	// $(this).hide();
	// });
	$("#imageUpload").dialog({
		autoOpen : false,
		height : 400,
		width : 500,
		modal : true,
		buttons : {
			"确定" : function() {
				saveImageDialog();
				$(this).dialog("close");
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
		}
	});
}

function saveImageDialog() {
	var htmlret = "";
	$('#uploadContentDiv')
			.find('.cttp')
			.each(
					function(index, dom) {
						var src = $(dom).find('.cpimg').attr('src');
						var title = $(dom).find('.cptitle').find('input').val();
						if (title == '') {
							title = '选项';
						}
						if (selectQType == 3) {
							htmlret += '<li class="oneNot"><div class="questionImgBox" name=""><div  class="QImgCon">'
									+ '<img  src="'
									+ src
									+ '" width="100%" height="100%" border="0"><input type="hidden" name="optImgUrl" value="'
									+ src
									+ '" /></div><input name="defValue" type="radio">'
									+ '<span class="T_edit_min"  name="optValue" contenteditable="true">'
									+ title
									+ '</span><span class="lianJie"></span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></div></li>';
						} else if (selectQType == 4) {
							htmlret += '<li class="oneNot"><div class="questionImgBox" name=""><div  class="QImgCon">'
									+ '<img  src="'
									+ src
									+ '" width="100%" height="100%" border="0"><input type="hidden" name="optImgUrl" value="'
									+ src
									+ '" /></div><input name="defValue" type="checkbox">'
									+ '<span class="T_edit_min"  name="optValue" contenteditable="true">'
									+ title
									+ '</span><span class="lianJie"></span><a name="删除选项" href="javascript:;" class="DelEdit"><i class="menu_edit5_icon"></i></a></div></li>';
						}
					});
	$(selectQLi).find(".unstyled.Imgli").append(htmlret);
}

function checkTrueState() {
	var okfalg1 = true;
	var okfalg2 = true;
	var okfalg3 = true;
	var okfalg4 = true;
	var checkArr = new Array();
	var checkArr2 = new Array();
	var checkArr3 = new Array();
	$('#question_box').find('.T_edit').each(function(index, dom) {
		if (Trim($(dom).html()) == '' || Trim($(dom).html()) == '<br>') {
			checkArr.push(dom);
		}
	});
	$('#question_box').find('.T_edit_min').each(function(index, dom) {
		if (Trim($(dom).html()) == '') {
			checkArr2.push(dom);
		}
	});
	$('#question_box').find(".unstyled.Imgli").each(function(index, dom) {
		if ($(dom).find(".QImgCon").length == 0) {
			checkArr3.push(dom);
		}
	});
	var checkArr4 = new Array();
	$('#question_box').find('.Drag_newAreas').each(
			function(index, dom) {
				var dxlength = $(dom).siblings('.unstyled').eq(0).find(
						"input[type=checkbox]").length;
				var minTicket = $(dom).find('.minTicket').val();
				var maxTicket = $(dom).find('.maxTicket').val();
				var titles = $(dom).siblings('.Drag_area').eq(0).find(
						'.q_title.th4.T_edit').eq(0).text();
				if ("undefined " == typeof minTicket
						|| !isPositiveNum(parseInt(minTicket))
						|| parseInt(minTicket) < 1
						|| parseInt(minTicket) > dxlength) {
					checkArr4.push(titles + "最小选投数不合法");
				} else if ("undefined " == typeof maxTicket
						|| !isPositiveNum(parseInt(maxTicket))
						|| parseInt(maxTicket) < 1
						|| parseInt(maxTicket) > dxlength) {
					checkArr4.push(titles + "最大选投数不合法");
				} else if (parseInt(minTicket) > parseInt(maxTicket)) {
					checkArr4.push(titles + "最大选投数不能小于最小选投数");
				}
			});
	if (checkArr4.length > 0) {
		var str = checkArr4.join(",");
		okfalg1 = false;
		return;
	}
	if (checkArr3.length > 0) {
		alert("请上传图片");
		okfalg2 = false;
		return;
	}
	if (checkArr.length > 0) {
		$(checkArr[0]).focus();
		alert('请填写问题描述');
		okfalg3 = false;
		return;
	}
	if (checkArr2.length > 0) {
		$(checkArr2[0]).focus();
		alert('请填写选项描述');
		okfalg4 = false;
		return;
	}
	if (okfalg1 && okfalg2 && okfalg3 && okfalg4) {
		return true;
	} else {
		return false;
	}
}
