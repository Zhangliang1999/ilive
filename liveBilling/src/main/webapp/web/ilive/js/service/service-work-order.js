myapp.factory('workOrderService', function($http, $q, Upload) {
	return {
		list : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/workOrder/list",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					wordOrderType : queryCondition.wordOrderType,
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
		save : function(bean) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/ilive/workOrder/save",
				data : bean,
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				transformRequest : postTransformRequest
			}).then(function(response) {
				d.resolve(response.data);
			}, function arr(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
	}
});