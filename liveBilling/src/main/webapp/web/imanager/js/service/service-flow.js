myapp.factory('flowService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/flow/page",
				params : {
					enterpriseContent : queryCondition.enterpriseContent,
					userContent : queryCondition.userContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					settlementStatus : queryCondition.settlementStatus,
					flowType : queryCondition.flowType,
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
		listForSettlement : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/flow/listForSettlement",
				params : {
					revenueFlowIds : queryCondition.revenueFlowIds,
					enterpriseId : queryCondition.enterpriseId,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					settlementStatus : queryCondition.settlementStatus,
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