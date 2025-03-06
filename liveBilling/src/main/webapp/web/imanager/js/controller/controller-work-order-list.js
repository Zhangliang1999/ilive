myapp.controller("workOrderListAct", function($scope, $http, $state, $cookies, queryCondition, workOrderService) {
	$scope.queryCondition = queryCondition;
	$scope.statusBeans = workOrderStatusBeans;
	$scope.workOrderTypeBeans = workOrderTypeBeans;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			workOrderService.page($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

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

	$scope.addPayment = function() {
		$state.go("workOrderAdd", {});
	}
	$scope.editPayment = function(id) {
		$state.go("workOrderEdit", {
			id : id
		});
	}
});