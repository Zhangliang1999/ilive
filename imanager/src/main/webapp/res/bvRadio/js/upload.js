//上传参数
var uploadmaxfilesize = "1000";
var allowmediatype = "jpg;png;gif;jpeg";
var button_image_url = "${base}/res/bvRadio/img/admin/choose_picture.png";
var upload_url = '../o_uploadFile.do';
var allowmediatypeArray = new Array();
var allowmediatypes = "";
allowmediatypeArray = allowmediatype.split(";");
for (i = 0; i < allowmediatypeArray.length; i++) {
	allowmediatypes += "*." + allowmediatypeArray[i] + ";";
}
allowmediatypes = allowmediatypes.substring(0, allowmediatypes.length - 1);
var flashUrl = "${base}/res/bvRadio/js/swfupload.swf";
var buttonPlaceholderId = "uploadphoto";

function uploadSuccess(file, result) {
	var jsonObj = eval('(' + result + ')');
	$('#img' + file.id).attr('src', jsonObj.fileRootUrl + jsonObj.filePath);
	$('#img' + file.id).attr('status', "finished");
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
	if (percent > 90) {
		percent = 100;
	}
	var progressbar = $("#progressbar" + file.id);
	progressLabel = $(progressbar).find(".progress-label");
	progressbar.progressbar({
		value : false,
		change : function() {
			progressLabel.text(progressbar.progressbar("value") + "%");
		},
		complete : function() {
			progressLabel.text("100%");
		}
	});
	progressbar.progressbar("value", percent);
}

var swfupload = null;

function uploadFile() {
	if (swfupload.getStats().files_queued == 0) {
		alert("请选择要上传的文件！", "上传提示");
		return false;
	}
	if (swfupload.getFile(0) != null) {
		swfupload.startUpload();
	}
}
function initSwf(uploadnumber) {
	$('.swfupload').after($('<div class="uploadphoto" id="uploadphoto"></div>'));
	$('.swfupload').remove();
	var jsessionid = $.cookie("JSESSIONID");
	if (jsessionid) {
		upload_url += ";jsessionid=" + jsessionid;
	}
	swfupload = new SWFUpload({
		upload_url : upload_url,
		post_params : {},
		use_query_string : true,
		file_post_name : "Filedata",
		file_size_limit : uploadmaxfilesize + "MB",
		file_types : allowmediatypes, // 在这里限制文件类型:'*.jpg,*.png,*.gif'
		file_types_description : "所有文件",
		file_upload_limit : uploadnumber,
		file_queue_limit : uploadnumber,

		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : fileDialogComplete,
		file_queued_handler : fileQueued,
		upload_progress_handler : uploadProgress,
		upload_error_handler : uploadError,
		upload_complete_handler : uploadComplete,
		upload_success_handler : uploadSuccess,

		button_image_url : button_image_url,
		button_placeholder_id : buttonPlaceholderId,
		button_width : 195,
		button_height : 42,
		button_text : "",
		button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
		button_cursor : SWFUpload.CURSOR.HAND,
		flash_url : flashUrl,
		debug : false, // 是否显示调试窗口
		photo_size : 100
	});
}
