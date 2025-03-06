myapp.filter("packageType", function() {
	return function(input, showStr) {
		if (input) {
			for (var i = 0; i < packageTypeBeans.length; i++) {
				if (input == packageTypeBeans[i].value) {
					return packageTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("vaildDuration", function() {
	return function(input, showStr) {
		if (input) {
			for (var i = 0; i < vaildDurationBeans.length; i++) {
				if (input == vaildDurationBeans[i].value) {
					return vaildDurationBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("packageStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (var i = 0; i < packageStatusBeans.length; i++) {
				if (input == packageStatusBeans[i].value) {
					return packageStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("channelTypes", function() {
	return function(input, showStr) {
		if (input) {
			var returnStr = "";
			for (var i = 0; i < channelTypesBeans.length; i++) {
				var inputs = input.split(",");
				for (var j = 0; j < inputs.length; j++) {
					if (inputs[j] === channelTypesBeans[i].value) {
						returnStr += channelTypesBeans[i].label + ",";
					}
				}
			}
			if (returnStr.length > 0) {
				returnStr = returnStr.substring(0, returnStr.length - 1);
			}
			return returnStr;
		}
		return showStr;
	}
});