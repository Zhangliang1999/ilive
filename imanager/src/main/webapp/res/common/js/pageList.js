PL = {
	version : '1.0'
};

/**
 * 提交
 */
PL.submit = function(url) {
	if(url){
		try {
			var tableForm = $("#_page_form");
			tableForm.attr("action",url);
			tableForm.onsubmit = null;
			tableForm.submit();
		} catch (e) {
			alert('submit()方法出错');
		}
	}
}
/**
 * 翻页
 * 
 * @param pageNo
 *            页码
 */
PL.gotoPage = function(pageNo) {
	try {
		var tableForm = $("#_page_form");
		$("input[name=pageNo]").val(pageNo);
		tableForm.onsubmit = null;
		tableForm.submit();
	} catch (e) {
		alert('gotoPage(pageNo)方法出错');
	}
}
/**
 * check checkbox.
 * 
 * @param name
 *            checkbox 的name属性
 * @param checked
 *            boolean of checked
 */
PL.checkbox = function(name, checked) {
	$("input[type=checkbox][name=" + name + "]").each(function() {
		$(this).attr("checked", checked);
	});
}
/**
 * 复选框选中的个数
 * 
 * @param name
 *            checkbox 的name属性
 */
PL.checkedCount = function(name) {
	var batchChecks = document.getElementsByName(name);
	var count = 0;
	for (var i = 0; i < batchChecks.length; i++) {
		if (batchChecks[i].checked) {
			count++;
		}
	}
	return count;
}
/**
 * 复选框选中的值
 * 
 * @param name
 *            checkbox 的name属性
 */
PL.getCheckedIds = function(name) {
	var batchChecks = document.getElementsByName(name);
	var ids = new Array();
	for (var i = 0; i < batchChecks.length; i++) {
		if (batchChecks[i].checked) {
			ids.push(batchChecks[i].value);
		}
	}
	return ids;
}

PL.onclickLi = function(obj) {
	var checkObj = $(obj).find("input[type=checkbox]");
	if ($(obj).attr("checkStatus") == "1") {
		checkObj.attr("checked", false);
		$(obj).attr("checkStatus", "0");
	} else {
		checkObj.attr("checked", true);
		$(obj).attr("checkStatus", "1");
	}
}
PL.ondblclickLi = function(obj) {

}
