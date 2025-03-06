myapp.controller("flowListAct", function($scope, $http, $state, $cookies, queryCondition, flowService) {
	$scope.queryCondition = queryCondition;
	
	$scope.queryFlowTypeLabel = "全部";
	$scope.flowTypeBeans = flowTypeBeans;
	
	$scope.querySettlementStatusLabel = "全部";
	$scope.settlementStatusBeans = settlementStatusBeans;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			if ($scope.queryCondition.settlementStatus != null) {
				for (var i = 0; i < $scope.settlementStatusBeans.length; i++) {
					var statusBean = $scope.settlementStatusBeans[i];
					if ($scope.queryCondition.settlementStatus == statusBean.value) {
						$scope.querySettlementStatusLabel = statusBean.label;
					}
				}
			} else {
				$scope.querySettlementStatusLabel = "全部";
			}
			if ($scope.queryCondition.flowType != null) {
				for (var i = 0; i < $scope.flowTypeBeans.length; i++) {
					var typeBean = $scope.flowTypeBeans[i];
					if ($scope.queryCondition.flowType == typeBean.value) {
						$scope.queryFlowTypeLabel = typeBean.label;
					}
				}
			} else {
				$scope.queryFlowTypeLabel = "全部";
			}
			flowService.page($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

	$scope.changeSettlementStatus = function(statusBean) {
		if (statusBean) {
			$scope.queryCondition.settlementStatus = statusBean.value;
		} else {
			$scope.queryCondition.settlementStatus = null;
		}
		$scope.searchList(1, false);
	}

	$scope.changeFlowType = function(typeBean) {
		if (typeBean) {
			$scope.queryCondition.flowType = typeBean.value;
		} else {
			$scope.queryCondition.flowType = null;
		}
		$scope.searchList(1, false);
	}

	$scope.changePageSize = function(pageSize) {
		if ($scope.page.pageSize != pageSize) {
			var expireDate = new Date();
			expireDate.setDate(expireDate.getDate() + 100 * 365);
			$cookies.put("cookie_page_size", pageSize, {
				'expires' : expireDate.toUTCString(),
				'path' : '/'
			})
			$scope.searchList(1, false);
		}
	}

	$scope.changePageNo = function(pageNo) {
		if ($scope.page.pageNo != pageNo) {
			if (pageNo <= 0) {
				pageNo = 1;
			}
			if (pageNo > $scope.page.totalPage) {
				pageNo = $scope.page.totalPage;
			}
			$scope.searchList(pageNo, false);
		}
	}

	$scope.addSettlementLog = function() {
		$state.go("settlementAdd", {});
	}
});