myapp.directive("autoHeight", function($timeout, $window) {
	return {
		restrict : 'A',
		scope : {
			"offset" : "=offset",
		},
		link : function(scope, element) {
			$timeout(function() {
				var w = angular.element($window);
				scope.getWindowDimensions = function() {
					return {
						'h' : w.height(),
						'w' : w.width()
					};
				};
				scope.$watch(scope.getWindowDimensions, function(newValue, oldValue) {
					scope.windowHeight = newValue.h;
					element.css('height', (newValue.h - scope.offset) + 'px');
				}, true);
				w.bind('resize', function() {
					scope.$apply();
				});
			});
		}
	};
});
