myapp.factory('userStatisticService', function($http, $q, Upload) {
	return {
		getTotalData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/total",
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
		getRegisterSourceData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/registerSource",
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
		getViewSourceData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/viewSource",
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
		getViewTimeDataForLine : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/viewTimeForLine",
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
		getViewTimeDataForColumn : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/viewTimeForColumn",
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
		getViewLocationData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/viewLocation",
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
		getViewTimeDataForPie : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/user/viewTimeForPie",
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
	}
});