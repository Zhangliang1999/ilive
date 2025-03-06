myapp.controller("userAct", function($scope, $http, $state, queryCondition, userStatisticService) {
	$scope.queryCondition = queryCondition;
	userStatisticService.getTotalData($scope.queryCondition).then(function(response) {
		$scope.totalData = resolveBean(response);
	});
	$scope.registerSourceQueryCondition = {};
	$scope.getRegisterSourceData = function() {
		userStatisticService.getRegisterSourceData($scope.registerSourceQueryCondition).then(function(response) {
			var registerSourceData = resolveBean(response);
			$scope.registerSourceData = [ {
				item : '安卓APP',
				count : registerSourceData.androidUserNum
			}, {
				item : '苹果APP',
				count : registerSourceData.iosUserNum
			}, {
				item : 'web端',
				count : registerSourceData.pcUserNum
			}, {
				item : 'H5端',
				count : registerSourceData.wapUserNum
			}, {
				item : '其他',
				count : registerSourceData.otherUserNum
			} ];
		});
	}
	$scope.getRegisterSourceData();
	$scope.viewSourceQueryCondition = {};
	$scope.getViewSourceData = function() {
		userStatisticService.getViewSourceData($scope.viewSourceQueryCondition).then(function(response) {
			var viewSourceData = resolveBean(response);
			$scope.viewSourceData = [ {
				item : '安卓APP',
				count : viewSourceData.androidUserViewNum
			}, {
				item : '苹果APP',
				count : viewSourceData.iosUserViewNum
			}, {
				item : 'web端',
				count : viewSourceData.pcUserViewNum
			}, {
				item : 'H5端',
				count : viewSourceData.wapUserViewNum
			}, {
				item : '其他',
				count : viewSourceData.otherUserViewNum
			} ];
		});
	}
	$scope.getViewSourceData();

	$scope.viewTimeDataForLine = [];
	$scope.viewTimeFieldForLine = [ "用户平均观看直播时长" ];
	$scope.viewTimeForLineQueryCondition = {};
	$scope.getViewTimeDataForLine = function() {
		userStatisticService.getViewTimeDataForLine($scope.viewTimeForLineQueryCondition).then(function(response) {
			var viewTimeDataForLine = resolveBean(response);
			var tempData = [];
			for (var i = 0; i < viewTimeDataForLine.length; i++) {
				var viewTimeData = viewTimeDataForLine[i];
				var totalDuration = viewTimeData.viewTotalDuration;
				var averageDuration;
				if (viewTimeData.viewTotalNum > 0) {
					averageDuration = viewTimeData.viewTotalDuration / viewTimeData.viewTotalNum;
				} else {
					averageDuration = 0;
				}
				tempData.push({
					"date" : viewTimeData.statisticTime,
					"用户平均观看直播时长" : averageDuration
				});
			}
			$scope.viewTimeDataForLine = tempData;

		});
	}
	$scope.getViewTimeDataForLine();

	$scope.viewTimeForColumnQueryCondition = {};
	$scope.getViewTimeDataForColumn = function() {
		userStatisticService.getViewTimeDataForColumn($scope.viewTimeForColumnQueryCondition).then(function(response) {
			var viewTimeDataForColumn = resolveBean(response);
			$scope.viewTimeDataForColumn = [ {
				"field" : "0:00-2:59",
				"value" : viewTimeDataForColumn.viewTimeHour0To3
			}, {
				"field" : "3:00-5:59",
				"value" : viewTimeDataForColumn.viewTimeHour3To6
			}, {
				"field" : "6:00-8:59",
				"value" : viewTimeDataForColumn.viewTimeHour6To9
			}, {
				"field" : "9:00-11:59",
				"value" : viewTimeDataForColumn.viewTimeHour9To12
			}, {
				"field" : "12:00-14:59",
				"value" : viewTimeDataForColumn.viewTimeHour12To15
			}, {
				"field" : "15:00-17:59",
				"value" : viewTimeDataForColumn.viewTimeHour15To18
			}, {
				"field" : "18:00-20:59",
				"value" : viewTimeDataForColumn.viewTimeHour18To21
			}, {
				"field" : "21:00-23:59",
				"value" : viewTimeDataForColumn.viewTimeHour21To24
			} ];

		});
	}
	$scope.getViewTimeDataForColumn();

	$scope.viewLocationField = "totalNum";
	$scope.viewLocationQueryCondition = {};
	$scope.getViewLocationData = function() {
		userStatisticService.getViewLocationData($scope.viewLocationQueryCondition).then(function(response) {
			$scope.viewLocationData = resolveBean(response);
		});
	}
	$scope.getViewLocationData();
	$scope.excelExportViewLocationData = function() {
		var url = baseHomeUrl + "/admin/user/viewLocationExcelExport?i=i";
		if ($scope.viewLocationQueryCondition.startTime) {
			url += "&startTime=" + $scope.viewLocationQueryCondition.startTime;
		}
		if ($scope.viewLocationQueryCondition.endTime) {
			url += "&endTime=" + $scope.viewLocationQueryCondition.endTime;
		}
		window.location.href = url;
	}
	
	$scope.viewTimeForPieQueryCondition = {};
	$scope.getViewTimeDataForPie = function() {
		userStatisticService.getViewTimeDataForPie($scope.viewTimeForPieQueryCondition).then(function(response) {
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
});