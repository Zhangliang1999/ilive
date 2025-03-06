myapp.factory('agentService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/agent/page",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
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
		list : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/agent/list",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
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
		excelExport : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/agent/excel_export",
				params : {
					enterpriseId : queryCondition.enterpriseId,
					enterpriseName : queryCondition.enterpriseName,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime
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
		info : function(id) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/agent/info",
				params : {
					id : id,
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
		checkFlow : function(revenueFlowIds) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/agent/checkFlow",
				params : {
					revenueFlowIds : revenueFlowIds,
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
				url : baseHomeUrl + "/admin/agent/save",
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
		update : function(bean) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/agent/update",
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
		online : function(id) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/agent/online",
				data : {
					id : id
				},
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
		offline : function(id) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/agent/offline",
				data : {
					id : id
				},
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