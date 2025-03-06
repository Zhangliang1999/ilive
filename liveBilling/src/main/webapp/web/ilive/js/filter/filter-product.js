myapp.filter("productType", function() {
	return function(input, showStr) {
		if (input) {
			for (var i = 0; i < productTypeBeans.length; i++) {
				if (input == productTypeBeans[i].value) {
					return productTypeBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
