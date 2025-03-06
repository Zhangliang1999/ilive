function fileQueueError(file, errorCode, message) {
	try {
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("一次最多只能上传" + swfupload.settings.file_upload_limit + "个文件！", '上传提示', "");
			return;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			message = "文件为0K";
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			message = "文件过大";
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			message = "错误的文件格式";
			break;
		default:
			alert(message);
			break;
		}
	} catch (ex) {
		this.debug(ex);
	}
}

function fileQueued(file) {
	addReadyFileInfo(file.id, file.name, "");
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
}

function uploadError(file, errorCode, message) {
}
function uploadComplete(file) {
	try {
		if (this.getStats().files_queued > 0) {
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}
}

function addReadyFileInfo(fileid, fileName, message) {
	var uploadType = $('#uploadType').val();
	// 用表格显示
	var contentDiv = document.getElementById("contentdiv");
	var midHtml = '<div class="cttp">';
	midHtml += '<img class="cpimg" src=""  id="img' + fileid
			+ '" width="100%" height="60%"/><div class="upImageDel" onclick="delUpImage(this)"><span style="cursor: pointer; color: red;">删除</span></div>';
	midHtml += '<div  class="cptitle"><label>标题</label><input type="text" value=""/></div>';
	midHtml += '<div id="progressbar' + fileid + '"><div class="progress-label">0%</div></div>';
	midHtml += '</div>';
	$(contentDiv).append(midHtml);
	uploadFile();
}