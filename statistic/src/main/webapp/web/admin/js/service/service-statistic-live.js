myapp.factory('liveStatisticService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/page",
				params : {
					roomId : queryCondition.roomId,
					liveEventId : queryCondition.liveEventId,
					liveTitle : queryCondition.liveTitle,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					enterpriseId: queryCondition.enterpriseId,
					enterpriseName: queryCondition.enterpriseName,
					liveLongTime: queryCondition.liveLongTime,
					pageNo : pageNo
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getInfo : function(queryCondition) {
			console.log("走到了getInfo");
			var d = $q.defer();
			console.log(d);
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/info",
				params : {
					liveEventId : queryCondition.liveEventId,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			console.log("getInfo的返回数据");
			console.log(d.promise);
			return d.promise;
		},
		getTotalDurationDataForLine : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/totalDurationForLine",
				params : {
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getSingleDurationDataForPie : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/singleDurationForPie",
				params : {
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getEventNumDataForLine : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/eventNumForLine",
				params : {
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getViewNumDataForLine : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/viewNumForLine",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getViewSourceData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/viewSource",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getRegisterSourceData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/registerSource",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getViewTimeDataForPie : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/viewTimeForPie",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getViewLocationData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/viewLocation",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getShareList : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/shareList",
				params : {
					liveEventId : queryCondition.liveEventId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		getUserViewLogPage : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/live/userViewLogPage",
				params : {
					liveEventId : queryCondition.liveEventId,
					userId : queryCondition.userId,
					username : queryCondition.username,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					pageNo : pageNo
				},
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
	}
});