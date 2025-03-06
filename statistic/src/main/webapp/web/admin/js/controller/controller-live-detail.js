myapp.controller("liveDetailAct", function($scope, $http, $state, queryCondition, liveStatisticService) {
	$scope.queryCondition = queryCondition;
	liveStatisticService.getInfo($scope.queryCondition).then(function(response) {
		$scope.info = resolveBean(response);
	});

	$scope.viewNumDataForLine = [];
	$scope.viewNumForLineQueryCondition = {
		liveEventId : queryCondition.liveEventId
	};
	$scope.getViewNumDataForLine = function() {
		liveStatisticService.getViewNumDataForLine($scope.viewNumForLineQueryCondition).then(function(response) {
			var viewNumDataForLine = resolveBean(response);
			var tempData = [];
			for (var i = 0; i < viewNumDataForLine.length; i++) {
				var viewNumData = viewNumDataForLine[i];
				var minute = viewNumData.minute;
				var userNum = viewNumData.userNum;
				var viewNum = viewNumData.viewNum;
				var viewTotalNum = viewNumData.viewTotalNum;
				var userTotalNum = viewNumData.userTotalNum;
				tempData.push({
					"date" : minute,
					"sort" : "累计观看次数",
					"value" : viewTotalNum,
				});
				tempData.push({
					"date" : minute,
					"sort" : "并发观看人数",
					"value" : userNum,
				});
				tempData.push({
					"date" : minute,
					"sort" : "累计观看人数",
					"value" : userTotalNum
				});
			}
			$scope.viewNumDataForLine = tempData;
		});
	}
	$scope.getViewNumDataForLine();

	$scope.viewSourceQueryCondition = {
		liveEventId : queryCondition.liveEventId
	};
	$scope.getViewSourceData = function() {
		liveStatisticService.getViewSourceData($scope.viewSourceQueryCondition).then(function(response) {
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
		liveEventId : queryCondition.liveEventId
	};
	$scope.getRegisterSourceData = function() {
		liveStatisticService.getRegisterSourceData($scope.registerSourceQueryCondition).then(function(response) {
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

	$scope.viewTimeForPieQueryCondition = {
		liveEventId : queryCondition.liveEventId
	};
	$scope.getViewTimeDataForPie = function() {
		liveStatisticService.getViewTimeDataForPie($scope.viewTimeForPieQueryCondition).then(function(response) {
			var viewTimeDataForPie = resolveBean(response);
			$scope.viewTimeDataForPie = [ {
				item : '观看5分钟以下',
				count : viewTimeDataForPie.viewDuraionUserNum0To5
			}, {
				item : '观看5-10分钟',
				count : viewTimeDataForPie.viewDuraionUserNum5To10
			}, {
				item : '观看10-30分钟',
				count : viewTimeDataForPie.viewDuraionUserNum10To30
			}, {
				item : '观看30-60分钟',
				count : viewTimeDataForPie.viewDuraionUserNum30To60
			}, {
				item : '观看60分钟以上',
				count : viewTimeDataForPie.viewDuraionUserNum60To
			} ];
		});
	}
	$scope.getViewTimeDataForPie();

	$scope.viewLocationQueryCondition = {
		liveEventId : queryCondition.liveEventId
	};
	$scope.getViewLocationData = function() {
		liveStatisticService.getViewLocationData($scope.viewLocationQueryCondition).then(function(response) {
			$scope.viewLocationField = "totalNum";
			$scope.viewLocationData = resolveBean(response);
		});
	}
	$scope.getViewLocationData();
	$scope.excelExportViewLocationData = function() {
		var url = baseHomeUrl + "/admin/live/viewLocationExcelExport?i=i";
		if ($scope.viewLocationQueryCondition.liveEventId) {
			url += "&liveEventId=" + $scope.viewLocationQueryCondition.liveEventId;
		}
		if ($scope.viewLocationQueryCondition.startTime) {
			url += "&startTime=" + $scope.viewLocationQueryCondition.startTime;
		}
		if ($scope.viewLocationQueryCondition.endTime) {
			url += "&endTime=" + $scope.viewLocationQueryCondition.endTime;
		}
		window.location.href = url;
	}

	$scope.listQueryCondition = {
		liveEventId : queryCondition.liveEventId
	};
	$scope.getList = function() {
		liveStatisticService.getShareList($scope.listQueryCondition).then(function(response) {
			$scope.shareList = resolveBean(response);
		});
	}
	$scope.getList();
});