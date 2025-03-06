myapp.factory('totalStatisticService', function($http, $q, Upload) {
	return {
		getTotalData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/total/total",
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
		getMaxData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/total/max",
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
				url : baseHomeUrl + "/admin/total/registerSource",
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