myapp.directive("tokenfield", function($timeout) {
	return {
		restrice : "A",
		link : function(scope, element, attr) {
			return $timeout(function() {
				var engine = new Bloodhound({
					local : [],
					datumTokenizer : function(d) {
						return Bloodhound.tokenizers.whitespace(d.value);
					},
					queryTokenizer : Bloodhound.tokenizers.whitespace
				});
				engine.initialize();
				$(element).tokenfield({
					typeahead : [ null, {
						source : engine.ttAdapter()
					} ]
				});
				scope.$watch(attr["ngModel"], function(newValue, oldValue, ctrl) {
					$(element).tokenfield('setTokens', newValue);
				});
			})

		}

	}
});