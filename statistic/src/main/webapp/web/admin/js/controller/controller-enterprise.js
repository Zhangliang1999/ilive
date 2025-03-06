myapp.controller("enterpriseAct", function($scope, $http, $state, queryCondition, enterpriseStatisticService) {
	$scope.queryCondition = queryCondition;
	enterpriseStatisticService.getLiveTotalData($scope.queryCondition).then(function(response) {
		$scope.totalData = resolveBean(response);
	});
	$scope.beginLiveForColumnQueryCondition = {};
	$scope.getBeginLiveDataForColumn = function() {
		enterpriseStatisticService.getBeginLiveDataForColumn($scope.beginLiveForColumnQueryCondition).then(function(response) {
			var beginLiveDataForColumn = resolveBean(response);
			$scope.beginLiveDataForColumn = [ {
				"field" : "0:00-2:59",
				"value" : beginLiveDataForColumn.beginLiveHour0To3
			}, {
				"field" : "3:00-5:59",
				"value" : beginLiveDataForColumn.beginLiveHour3To6
			}, {
				"field" : "6:00-8:59",
				"value" : beginLiveDataForColumn.beginLiveHour6To9
			}, {
				"field" : "9:00-11:59",
				"value" : beginLiveDataForColumn.beginLiveHour9To12
			}, {
				"field" : "12:00-14:59",
				"value" : beginLiveDataForColumn.beginLiveHour12To15
			}, {
				"field" : "15:00-17:59",
				"value" : beginLiveDataForColumn.beginLiveHour15To18
			}, {
				"field" : "18:00-20:59",
				"value" : beginLiveDataForColumn.beginLiveHour18To21
			}, {
				"field" : "21:00-23:59",
				"value" : beginLiveDataForColumn.beginLiveHour21To24
			} ];
		});
	}
	$scope.getBeginLiveDataForColumn();

	$scope.beginLocationField = "totalNum";
	$scope.beginLocationQueryCondition = {};
	$scope.getBeginLocationData = function() {
		enterpriseStatisticService.getBeginLocationData($scope.beginLocationQueryCondition).then(function(response) {
			$scope.beginLocationData = resolveBean(response);
		});
	}
	$scope.getBeginLocationData();

	$scope.excelExportBeginLocationData = function() {
		var url = baseHomeUrl + "/admin/enterprise/beginLocationExcelExport?i=i";
		if ($scope.beginLocationQueryCondition.enterpriseId) {
			url += "&enterpriseId=" + $scope.beginLocationQueryCondition.enterpriseId;
		}
		if ($scope.beginLocationQueryCondition.startTime) {
			url += "&startTime=" + $scope.beginLocationQueryCondition.startTime;
		}
		if ($scope.beginLocationQueryCondition.endTime) {
			url += "&endTime=" + $scope.beginLocationQueryCondition.endTime;
		}
		window.location.href = url;
	}
});