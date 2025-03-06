myapp.controller("MeetingAct", function($scope, $http, $state, queryCondition, meetStatisticService) {
	$scope.refreshTotalData = function() {
		$scope.meetingTimeAreaData = [];
		$scope.meetingEnterpriseTopData = [];
		$scope.meetingLoginTypeData = [];
		$scope.meetingRecordData = [];
		$scope.meetingRedirectData = [];
		$scope.meetingFigureData = [];
		$scope.meetingStartData = [];
		$scope.queryConditionOfTotalData = queryCondition || {};
		meetStatisticService.getTotalData($scope.queryConditionOfTotalData).then(function(response) {
			$scope.totalData = resolveBean(response);
			var totalSourceData=resolveBean(response);
			$scope.meetingTimeAreaData = [ {
				item : '30分钟以内',
				count : totalSourceData.num30
			}, {
				item : '30-1小时',
				count : totalSourceData.num301
			},{
				item : '1-2小时',
				count : totalSourceData.num12
			},{
				item : '2小时以上',
				count : totalSourceData.num2
			} ];
			$scope.meetingEnterpriseTopData = [{
				"field" : totalSourceData.enterpriseName1,
				"value" : totalSourceData.enterpriseNum1
			},{
				"field" : totalSourceData.enterpriseName2,
				"value" : totalSourceData.enterpriseNum2
			},{
				"field" : totalSourceData.enterpriseName3,
				"value" : totalSourceData.enterpriseNum3
			},{
				"field" : totalSourceData.enterpriseName4,
				"value" : totalSourceData.enterpriseNum4
			},{
				"field" : totalSourceData.enterpriseName5,
				"value" : totalSourceData.enterpriseNum5
			},{
				"field" : totalSourceData.enterpriseName6,
				"value" : totalSourceData.enterpriseNum6
			},{
				"field" : totalSourceData.enterpriseName7,
				"value" : totalSourceData.enterpriseNum7
			},{
				"field" : totalSourceData.enterpriseName8,
				"value" : totalSourceData.enterpriseNum8
			},{
				"field" : totalSourceData.enterpriseName9,
				"value" : totalSourceData.enterpriseNum9
			},{
				"field" : totalSourceData.enterpriseName10,
				"value" : totalSourceData.enterpriseNum10
			}
			                                   
			                                   ];
			$scope.meetingLoginTypeData = [{
				item : 'PC网页',
				count : totalSourceData.PcSumNum
			},{
				item : 'APP端',
				count : totalSourceData.APPSumNum
			}
			                               ];
			$scope.meetingRecordData = [{
				item : '录制',
				count : totalSourceData.recordNum
			},{
				item : '不录制',
				count : totalSourceData.unRecordNum
			}];
			$scope.meetingRedirectData = [{
				item : '转播',
				count : totalSourceData.redirectNum
			},{
				item : '不转播',
				count : totalSourceData.unRedirectNum
			}];
			$scope.meetingFigureData = [{
				"field" : '1',
				"value" : totalSourceData.figureNum1
			},{
				"field" : '2',
				"value" : totalSourceData.figureNum2
			},{
				"field" : '3',
				"value" : totalSourceData.figureNum3
			},{
				"field" : '4',
				"value" : totalSourceData.figureNum4
			},{
				"field" : '5',
				"value" : totalSourceData.figureNum5
			},{
				"field" : '6',
				"value" : totalSourceData.figureNum6
			},{
				"field" : '7',
				"value" : totalSourceData.figureNum7
			},{
				"field" : '8',
				"value" : totalSourceData.figureNum8
			},{
				"field" : '9',
				"value" : totalSourceData.figureNum9
			}];
			$scope.meetingStartData = [{
				"field" : '0',
				"value" : totalSourceData.startTime0to1
			},{
				"field" : '1',
				"value" : totalSourceData.startTime1to2
			},{
				"field" : '2',
				"value" : totalSourceData.startTime2to3
			},{
				"field" : '3',
				"value" : totalSourceData.startTime3to4
			},{
				"field" : '4',
				"value" : totalSourceData.startTime4to5
			},{
				"field" : '5',
				"value" : totalSourceData.startTime5to6
			},{
				"field" : '6',
				"value" : totalSourceData.startTime6to7
			},{
				"field" : '7',
				"value" : totalSourceData.startTime7to8
			},{
				"field" : '8',
				"value" : totalSourceData.startTime8to9
			},{
				"field" : '9',
				"value" : totalSourceData.startTime9to10
			},{
				"field" : '10',
				"value" : totalSourceData.startTime10to11
			},{
				"field" : '11',
				"value" : totalSourceData.startTime11to12
			},{
				"field" : '12',
				"value" : totalSourceData.startTime12to13
			},{
				"field" : '13',
				"value" : totalSourceData.startTime13to14
			},{
				"field" : '14',
				"value" : totalSourceData.startTime14to15
			},{
				"field" : '15',
				"value" : totalSourceData.startTime15to16
			},{
				"field" : '16',
				"value" : totalSourceData.startTime16to17
			},{
				"field" : '17',
				"value" : totalSourceData.startTime17to18
			},{
				"field" : '18',
				"value" : totalSourceData.startTime18to19
			},{
				"field" : '19',
				"value" : totalSourceData.startTime19to20
			},{
				"field" : '20',
				"value" : totalSourceData.startTime20to21
			},{
				"field" : '21',
				"value" : totalSourceData.startTime21to22
			},{
				"field" : '22',
				"value" : totalSourceData.startTime22to23
			},{
				"field" : '23',
				"value" : totalSourceData.startTime23to24
			}];
			
		});
	}
	$scope.refreshTotalData();
	
});