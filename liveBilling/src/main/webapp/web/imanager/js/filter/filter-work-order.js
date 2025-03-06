myapp.filter("workOrderStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < workOrderStatusBeans.length; i++) {
				if (input == workOrderStatusBeans[i].value) {
					return workOrderStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("workOrderType", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < workOrderTypeBeans.length; i++) {
				if (input == workOrderTypeBeans[i].value) {
					return workOrderTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
