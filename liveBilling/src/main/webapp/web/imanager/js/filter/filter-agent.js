myapp.filter("agentStatus", function() {
	return function(input, showStr) {
		if (input) {
			for (i = 0; i < agentStatusBeans.length; i++) {
				if (input == agentStatusBeans[i].value) {
					return agentStatusBeans[i].label;
				}
			}
		}
		return showStr;
	}
});
