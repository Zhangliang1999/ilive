myapp.controller("totalAct", function($scope, $http, $state, queryCondition, totalStatisticService) {
	$scope.refreshTotalData = function() {
		$scope.queryConditionOfTotalData = queryCondition || {};
		totalStatisticService.getTotalData($scope.queryConditionOfTotalData).then(function(response) {
			$scope.totalData = resolveBean(response);
		});
		totalStatisticService.getMaxData($scope.queryConditionOfTotalData).then(function(response) {
			$scope.maxData = resolveBean(response);
		});
	}
	$scope.refreshTotalData();
	$scope.refreshOtherData = function() {
		$scope.queryConditionOfOtherData = queryCondition || {};
		totalStatisticService.getTotalData($scope.queryConditionOfOtherData).then(function(response) {
			$scope.otherData = resolveBean(response);
		});
	}
	$scope.refreshOtherData();

	$scope.refreshRegisterSourceData = function() {
		$scope.enterpriseRegisterData = [];
		$scope.userRegisterData = [];
		$scope.queryConditionOfRegisterSourceData = queryCondition || {};
		totalStatisticService.getRegisterSourceData($scope.queryConditionOfRegisterSourceData).then(function(response) {
			var registerSourceData = resolveBean(response);
			$scope.enterpriseRegisterData = [ {
				item : '新商户',
				count : registerSourceData.newRegisterEnterpriseNum
			}, {
				item : '老商户',
				count : registerSourceData.oldRegisterEnterpriseNum
			} ];
			$scope.userRegisterData = [ {
				item : '新用户',
				count : registerSourceData.newRegisterUserNum
			}, {
				item : '老用户',
				count : registerSourceData.oldRegisterUserNum
			} ];
		});
	}
	$scope.refreshRegisterSourceData();
});