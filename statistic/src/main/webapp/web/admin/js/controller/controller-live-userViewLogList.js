myapp.controller("liveUserViewLogListAct", function($scope, $http, $state, queryCondition, liveStatisticService) {
	$scope.queryCondition = queryCondition;
	console.log($scope.queryCondition);
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			liveStatisticService.getUserViewLogPage($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

	$scope.excelExport = function() {
		var url = baseHomeUrl + "/admin/live/excel_export_view_log?i=i";
		if ($scope.queryCondition.liveEventId) {
			url += "&liveEventId=" + $scope.queryCondition.liveEventId;
		}
		if ($scope.queryCondition.userId) {
			url += "&userId=" + $scope.queryCondition.userId;
		}
		if ($scope.queryCondition.username) {
			url += "&username=" + $scope.queryCondition.username;
		}
		if ($scope.queryCondition.startTime) {
			url += "&startTime=" + $scope.queryCondition.startTime;
		}
		if ($scope.queryCondition.endTime) {
			url += "&endTime=" + $scope.queryCondition.endTime;
		}
		window.location.href = url;
	}

	$scope.checkAll = function(m) {
		for (i = 0; i < $scope.page.list.length; i++) {
			var bean = $scope.page.list[i];
			if (m === true) {
				bean.isChecked = true;
			} else {
				bean.isChecked = false;
			}
		}
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
	$scope.viewDetail = function(liveEventId) {
		$state.go("liveDetail", {
			liveEventId : liveEventId
		});
	}
});