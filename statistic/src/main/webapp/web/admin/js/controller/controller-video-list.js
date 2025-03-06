myapp.controller("videoListAct", function($scope, $http, $state, $cookies, queryCondition, videoStatisticService) {
	$scope.queryCondition = queryCondition;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			videoStatisticService.page($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

	$scope.excelExport = function() {
		var url = baseHomeUrl + "/admin/video/excel_export?i=i";
		if ($scope.queryCondition.videoId) {
			url += "&videoId=" + $scope.queryCondition.videoId;
		}
		if ($scope.queryCondition.videoTitle) {
			url += "&videoTitle=" + $scope.queryCondition.videoTitle;
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
	$scope.viewDetail = function(videoId) {
		$state.go("videoDetail", {
			videoId : videoId
		});
	}
});