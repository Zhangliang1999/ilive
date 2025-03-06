function fetchDataByQType(obj) {
	var qtype = $(obj).attr("qtype");
	qtype = parseInt(qtype);
	var retVal = {};
	switch (qtype) {
		case 7:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		case 8:
			retVal.dataOrder=$(obj).attr("qorder");
			var checkObj = $(obj).find(".mui-radio input[type=radio]:checked");
			// console.log(checkObj.length);
			var dataValue = checkObj.prev().html();
			retVal.dataValue = dataValue;
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		case 20:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		case 12:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("button").text();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		case 10:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		case 11:
			retVal.dataOrder=$(obj).attr("qorder");
			retVal.dataValue=$(obj).find("input").val();
			retVal.dataTitle=$(obj).prev().html();
			retVal.dataModelId=$(obj).attr("modelId");
			break;
		default:
			break;
	}		
	return retVal;
}

// 校验规则
function validQType() {
	
	
}