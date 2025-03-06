function connectWebViewJavascriptBridge(callback) {
	if (window.WebViewJavascriptBridge) {
		callback(WebViewJavascriptBridge)
	} else {
		document.addEventListener('WebViewJavascriptBridgeReady', function() {
			callback(WebViewJavascriptBridge)
		}, false)
	}
}
function iAlert(message, action) {
	connectWebViewJavascriptBridge(function(bridge) {
		bridge.callHandler('ToastMessage', {
			'msg' : encodeURI(message),
			"action" : action
		}, function(response) {
		})
	})
};
function iMessage(action) {
	connectWebViewJavascriptBridge(function(bridge) {
		bridge.callHandler('ToastMessage', {
			"action" : action
		}, function(response) {
		})
	})
};

function mediaPlay() {
	connectWebViewJavascriptBridge(function(bridge) {
		bridge.callHandler('mediaPlay', {
		}, function(response) {
		})
	})
}

function mediaPause() {
	connectWebViewJavascriptBridge(function(bridge) {
		bridge.callHandler('mediaPause', {
		}, function(response) {
		})
	})
}

function jAlert(message) {
	alert(message);
}
