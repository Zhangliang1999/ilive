myapp.config(function($urlRouterProvider, $stateProvider) {
	$urlRouterProvider.when("", '/total');
	$stateProvider.state("total", {
		url : "/total",
		templateUrl : "total/index.html",
		controller : 'totalAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("user", {
		url : "/user",
		templateUrl : "user/index.html",
		controller : 'userAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("enterprise", {
		url : "/enterprise",
		templateUrl : "enterprise/index.html",
		controller : 'enterpriseAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("enterpriseList", {
		url : "/enterprise/list",
		templateUrl : "enterprise/list.html",
		controller : 'enterpriseListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("enterpriseDetail", {
		url : "/enterprise/detail?enterpriseId",
		templateUrl : "enterprise/detail.html",
		controller : 'enterpriseDetailAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					enterpriseId : $stateParams.enterpriseId,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("live", {
		url : "/live",
		templateUrl : "live/index.html",
		controller : 'liveAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}

		}
	}).state("liveList", {
		url : "/live/list?roomId&liveEventId&liveTitle&startTime&endTime",
		templateUrl : "live/list.html",
		controller : 'liveListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					roomId : $stateParams.roomId,
					liveEventId : $stateParams.liveEventId,
					liveTitle : $stateParams.liveTitle,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("liveListForUserViewLog", {
		url : "/live/listForUserViewLog?roomId&liveEventId&liveTitle&startTime&endTime",
		templateUrl : "live/listForUserViewLog.html",
		controller : 'liveListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					roomId : $stateParams.roomId,
					liveEventId : $stateParams.liveEventId,
					liveTitle : $stateParams.liveTitle,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("liveDetail", {
		url : "/live/detail?liveEventId",
		templateUrl : "live/detail.html",
		controller : 'liveDetailAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					liveEventId : $stateParams.liveEventId,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("liveUserViewLogList", {
		url : "/live/userViewLogList?liveEventId&userId&username&startTime&endTime",
		templateUrl : "live/userViewLogList.html",
		controller : 'liveUserViewLogListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					liveEventId : $stateParams.liveEventId,
					userId : $stateParams.userId,
					username : $stateParams.username,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("videoList", {
		url : "/video/list",
		templateUrl : "video/list.html",
		controller : 'videoListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("videoDetail", {
		url : "/video/detail?videoId",
		templateUrl : "video/detail.html",
		controller : 'videoDetailAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					videoId : $stateParams.videoId,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	}).state("meetTotal", {
		url : "/meet",
		templateUrl : "meet/index.html",
		controller : 'MeetingAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
				};
			}
		}
	});
})
