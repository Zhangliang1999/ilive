myapp.directive("slimScroll", function($timeout, $window) {
	return {
		restrict : 'A',
		scope : {
			"offset" : "=offset",
		},
		link : function(scope, element) {
			$timeout(function() {
				var w = angular.element($window);
				var winH = w.height() - scope.offset;
				$(element).slimScroll({
					height : winH,
					alwaysVisible : true,
				});
			});
		}
	};
});
