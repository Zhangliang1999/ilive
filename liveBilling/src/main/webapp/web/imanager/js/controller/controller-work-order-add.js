myapp.controller("workOrderAddAct", function($scope, $rootScope, $state, workOrderService, packageService, agentService,
		operationUserService) {
	$scope.bean = {
		workOrderType : 1
	};
	$scope.workOrderTypeBeans = workOrderTypeBeans;

	$scope.saveAgent = function() {
		if (typeof ($scope.bean.enterpriseId) == "undefined" || $scope.bean.enterpriseId <= 0) {
			alert("请选择企业");
			return;
		}
		if (typeof ($scope.bean.workOrderType) == "undefined" || $scope.bean.workOrderType <= 0) {
			alert("请选择工单类型");
			return;
		}
		workOrderService.save($scope.bean).then(function(response) {
			if (response.code == 1) {
				alert("添加成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});