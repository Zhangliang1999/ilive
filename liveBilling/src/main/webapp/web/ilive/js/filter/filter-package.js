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
