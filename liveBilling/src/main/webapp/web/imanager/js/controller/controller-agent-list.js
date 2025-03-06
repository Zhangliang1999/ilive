myapp.controller("agentListAct", function($scope, $http, $state, $cookies, queryCondition, agentService) {
	$scope.queryCondition = queryCondition;
	// 状态搜索条件
	$scope.statusBeans = agentStatusBeans;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			agentService.page($scope.queryCondition, pageNo).then(function(response) {
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

	$scope.addAgent = function() {
		$state.go("agentAdd", {});
	}
	$scope.editAgent = function(id) {
		$state.go("agentEdit", {
			id : id
		});
	}
	$scope.onlineAgent = function(id) {
		if (confirm("确定要生效吗？")) {
			agentService.online(id).then(function(response) {
				$scope.searchList($scope.page.pageNo, false);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.offlineAgent = function(id) {
		if (confirm("确定要失效吗？")) {
			agentService.offline(id).then(function(response) {
				$scope.searchList($scope.page.pageNo, false);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
});