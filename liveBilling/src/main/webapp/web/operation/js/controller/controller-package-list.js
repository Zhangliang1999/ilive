myapp.controller("packageListAct", function($scope, $http, $state, $cookies, queryCondition, packageService) {
	$scope.queryCondition = queryCondition;
	// 类型搜索条件
	$scope.packageTypeBeans = packageTypeBeans;
	// 状态搜索条件
	$scope.statusBeans = packageStatusBeans;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			packageService.page($scope.queryCondition, pageNo).then(function(response) {
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

	$scope.addPackage = function() {
		$state.go("packageAdd", {});
	}
	$scope.editPackage = function(id) {
		$state.go("packageEdit", {
			id : id
		});
	}
	$scope.orderPackage = function(bean) {
		$scope.currentBean = bean;
	}
	$scope.updateOrder = function() {
		packageService.order($scope.currentBean.id, $scope.currentBean.orderNum).then(function(response) {
			if (response.code == 1) {
				alert("修改成功");
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});