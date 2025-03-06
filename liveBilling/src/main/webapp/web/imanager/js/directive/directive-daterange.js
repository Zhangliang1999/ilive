myapp.directive("daterange", function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element) {
			$timeout(function() {
				element.datepicker({
					keyboardNavigation : false,
					forceParse : false,
					autoclose : true,
					format : "yyyy-mm-dd"
				});
			});
		}
	};
});
