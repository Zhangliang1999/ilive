myapp.filter("settlementStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < settlementStatusBeans.length; i++) {
				if (input == settlementStatusBeans[i].value) {
					return settlementStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("flowType", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < flowTypeBeans.length; i++) {
				if (input == flowTypeBeans[i].value) {
					return flowTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});