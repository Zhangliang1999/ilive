myapp.controller("workOrderEditAct", function($scope, $rootScope, $state, bean, workOrderService) {
	$scope.bean = bean;
	$scope.workOrderTypeBeans = workOrderTypeBeans;

	$scope.updateAgent = function() {
		workOrderService.update($scope.bean).then(function(response) {
			if (response.code == 1) {
				alert("保存成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
	$scope.processAgent = function() {
		if (confirm("确定要设为处理中吗？")) {
			workOrderService.process($scope.bean.id).then(function(response) {
				if (response.code == 1) {
					alert("保存成功");
					$rootScope.returnPrevious();
				} else {
					alert(response.message);
				}
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.completeAgent = function() {
		if (confirm("确定要完成订单吗？")) {
			workOrderService.complete($scope.bean.id).then(function(response) {
				if (response.code == 1) {
					alert("保存成功");
					$rootScope.returnPrevious();
				} else {
					alert(response.message);
				}
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.cancelAgent = function() {
		if (typeof ($scope.bean.cancelReason) == "undefined" || $scope.bean.cancelReason == null
				|| $scope.bean.cancelReason.trim().length == 0) {
			alert("请填写退回原因");
			return;
		}
		workOrderService.cancel($scope.bean.id, $scope.bean.cancelReason).then(function(response) {
			if (response.code == 1) {
				alert("保存成功");
				$('#myModal').modal('hide');
				setTimeout(function() {
					$rootScope.returnPrevious();
				}, 500);
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});