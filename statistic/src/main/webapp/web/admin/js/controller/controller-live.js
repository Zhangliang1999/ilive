myapp.controller("liveAct", function($scope, $http, $state, queryCondition, liveStatisticService) {
	$scope.queryCondition = queryCondition;
	$scope.totalDurationDataForLine = [];
	$scope.totalDurationFieldForLine = "直播时长";
	$scope.totalDurationForLineQueryCondition = {};
	$scope.getTotalDurationDataForLine = function() {
		liveStatisticService.getTotalDurationDataForLine($scope.totalDurationForLineQueryCondition).then(function(response) {
			var totalDurationDataForLine = resolveBean(response);
			var tempData = [];
			for (var i = 0; i < totalDurationDataForLine.length; i++) {
				var totalDurationData = totalDurationDataForLine[i];
				var liveTotalDuration = totalDurationData.liveTotalDuration;
				var statisticTime = totalDurationData.statisticTime;
				tempData.push({
					"date" : statisticTime,
					"直播时长" : liveTotalDuration
				});
			}
			$scope.totalDurationDataForLine = tempData;
		});
	}
	$scope.getTotalDurationDataForLine();

	$scope.singleDurationForPieQueryCondition = {};
	$scope.getSingleDurationDataForPie = function() {
		liveStatisticService.getSingleDurationDataForPie($scope.singleDurationForPieQueryCondition).then(function(response) {
			var singleDurationDataForPie = resolveBean(response);
			$scope.singleDurationDataForPie = [ {
				item : '0-10分钟',
				count : singleDurationDataForPie.liveDuraionUserNum0To10
			}, {
				item : '10-100分钟',
				count : singleDurationDataForPie.liveDuraionUserNum10To100
			}, {
				item : '100-200分钟',
				count : singleDurationDataForPie.liveDuraionUserNum100To200
			}, {
				item : '200-300分钟',
				count : singleDurationDataForPie.liveDuraionUserNum200To300
			}, {
				item : '300分钟以上',
				count : singleDurationDataForPie.liveDuraionUserNum300To
			} ];
		});
	}
	$scope.getSingleDurationDataForPie();

	$scope.eventNumDataForLine = [];
	$scope.eventNumFieldForLine = "直播场数";
	$scope.eventNumForLineQueryCondition = {};
	$scope.getEventNumDataForLine = function() {
		liveStatisticService.getEventNumDataForLine($scope.eventNumForLineQueryCondition).then(function(response) {
			var eventNumDataForLine = resolveBean(response);
			var tempData = [];
			for (var i = 0; i < eventNumDataForLine.length; i++) {
				var eventNumData = eventNumDataForLine[i];
				var liveEventTotalNum = eventNumData.liveEventTotalNum;
				var statisticTime = eventNumData.statisticTime;
				tempData.push({
					"date" : statisticTime,
					"直播场数" : liveEventTotalNum
				});
			}
			$scope.eventNumDataForLine = tempData;
		});
	}
	$scope.getEventNumDataForLine();
});