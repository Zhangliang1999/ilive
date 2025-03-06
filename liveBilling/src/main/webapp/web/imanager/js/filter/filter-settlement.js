myapp.filter("settlementLogStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < settlementLogStatusBeans.length; i++) {
				if (input == settlementLogStatusBeans[i].value) {
					return settlementLogStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("invoiceStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < invoiceStatusBeans.length; i++) {
				if (input == invoiceStatusBeans[i].value) {
					return invoiceStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});