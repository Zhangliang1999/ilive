myapp.filter("paymentStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < paymentStatusBeans.length; i++) {
				if (input == paymentStatusBeans[i].value) {
					return paymentStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("paymentType", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < paymentTypeBeans.length; i++) {
				if (input == paymentTypeBeans[i].value) {
					return paymentTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
myapp.filter("channelType", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < channelTypeBeans.length; i++) {
				if (input == channelTypeBeans[i].value) {
					return channelTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
