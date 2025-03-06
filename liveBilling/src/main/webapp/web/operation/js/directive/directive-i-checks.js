myapp.directive("iCheck", function($timeout, $parse) {
	return {
		restrict : 'AC',
		require : 'ngModel',
		link : function($scope, element, $attrs, ngModel) {
			// 延迟的时间(非必填),如果不填,表示等线程空下来以后就执行.比如当页面被渲染完成后.
			return $timeout(function() {
				var value;
				value = $attrs['value'];
				$scope.$watch($attrs['ngModel'], function(newValue) {
					$(element).iCheck('update');
				})
				return $(element).iCheck({
					checkboxClass : 'icheckbox_square-green',
					radioClass : 'iradio_square-green',
				}).on('ifChanged', function(event) {

					if ($(element).attr('type') === 'checkbox' && $attrs['ngModel']) {
						$scope.$apply(function() {
							return ngModel.$setViewValue(event.target.checked);
						});
					}
					if ($(element).attr('type') === 'radio' && $attrs['ngModel']) {
						return $scope.$apply(function() {
							return ngModel.$setViewValue(value);
						});
					}
				});
			});
		}
	};
});
