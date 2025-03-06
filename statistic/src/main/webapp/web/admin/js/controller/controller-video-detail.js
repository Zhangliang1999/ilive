myapp.controller("videoDetailAct", function($scope, $http, $state, queryCondition, videoStatisticService) {
	$scope.queryCondition = queryCondition;
	videoStatisticService.getInfo($scope.queryCondition).then(function(response) {
		$scope.info = resolveBean(response);
	});
	$scope.viewNumDataForLine = [];
	$scope.viewNumFieldForLine = [ "累计观看次数", "并发观看人数", "累计观看人数" ]
	$scope.viewNumForLineQueryCondition = {
		videoId : queryCondition.videoId
	};
	$scope.getViewNumDataForLine = function() {
		videoStatisticService.getViewNumDataForLine($scope.viewNumForLineQueryCondition).then(function(response) {
			var viewNumDataForLine = resolveBean(response);
			var tempData = [];
			for (var i = 0; i < viewNumDataForLine.length; i++) {
				var viewNumData = viewNumDataForLine[i];
				var statisticTime = viewNumData.statisticTime.substring(0, 10);
				//var minute = viewNumData.minute;
				var minute = statisticTime;
				var viewTotalNum = viewNumData.viewTotalNum;
				var userTotalNum = viewNumData.userTotalNum;
				tempData.push({
					"date" : minute,
					"sort" : "观看人数",
					"value" : userTotalNum,
				});
				tempData.push({
					"date" : minute,
					"sort" : "观看次数",
					"value" : viewTotalNum,
				});
			}
			$scope.viewNumDataForLine = tempData;
		});
	}
	$scope.getViewNumDataForLine();

	$scope.viewSourceQueryCondition = {
		videoId : queryCondition.videoId
	};
	$scope.getViewSourceData = function() {
		videoStatisticService.getViewSourceData($scope.queryCondition).then(function(response) {
			var viewSourceData = resolveBean(response);
			$scope.viewSourceData = [ {
				item : '安卓APP',
				count : viewSourceData.androidUserNum
			}, {
				item : '苹果APP',
				count : viewSourceData.iosUserNum
			}, {
				item : 'web端',
				count : viewSourceData.pcUserNum
			}, {
				item : 'H5端',
				count : viewSourceData.wapUserNum
			}, {
				item : '其他',
				count : viewSourceData.otherUserNum
			} ];
		});
	}
	$scope.getViewSourceData();

	$scope.registerSourceQueryCondition = {
		videoId : queryCondition.videoId
	};
	$scope.getRegisterSourceData = function() {
		videoStatisticService.getRegisterSourceData($scope.registerSourceQueryCondition).then(function(response) {
			var registerSourceData = resolveBean(response);
			$scope.registerSourceData = [ {
				item : '新用户',
				count : registerSourceData.newRegisterUserNum
			}, {
				item : '老用户',
				count : registerSourceData.oldRegisterUserNum
			} ];
		});
	}
	$scope.getRegisterSourceData();

	$scope.viewLocationQueryCondition = {
		videoId : queryCondition.videoId
	};
	$scope.getViewLocationData = function() {
		videoStatisticService.getViewLocationData($scope.viewLocationQueryCondition).then(function(response) {
			$scope.viewLocationField = "totalNum";
			$scope.viewLocationData = resolveBean(response);
		});
	}
	$scope.getViewLocationData();
	$scope.excelExportViewLocationData = function() {
		var url = baseHomeUrl + "/admin/video/viewLocationExcelExport?i=i";
		if ($scope.viewLocationQueryCondition.videoId) {
			url += "&videoId=" + $scope.viewLocationQueryCondition.videoId;
		}
		if ($scope.viewLocationQueryCondition.startTime) {
			url += "&startTime=" + $scope.viewLocationQueryCondition.startTime;
		}
		if ($scope.viewLocationQueryCondition.endTime) {
			url += "&endTime=" + $scope.viewLocationQueryCondition.endTime;
		}
		window.location.href = url;
	}
});