myapp.controller("liveListAct", function($scope, $http, $state, $cookies, queryCondition, liveStatisticService) {
	$scope.queryCondition = queryCondition;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			liveStatisticService.page($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

	$scope.excelExport = function() {
		var url = baseHomeUrl + "/admin/live/excel_export?i=i";
		if ($scope.queryCondition.liveEventId) {
			url += "&liveEventId=" + $scope.queryCondition.liveEventId;
		}
		if ($scope.queryCondition.liveTitle) {
			url += "&liveTitle=" + $scope.queryCondition.liveTitle;
		}
		if ($scope.queryCondition.startTime) {
			url += "&startTime=" + $scope.queryCondition.startTime;
		}
		if ($scope.queryCondition.endTime) {
			url += "&endTime=" + $scope.queryCondition.endTime;
		}
		if ($scope.queryCondition.roomId) {
			url += "&roomId=" + $scope.queryCondition.roomId;
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
	$scope.viewUserViewLog = function(liveEventId) {
		$state.go("liveUserViewLogList", {
			liveEventId : liveEventId
		});
	}
});