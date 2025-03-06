myapp.factory('settlementService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/settlement/page",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					invoiceStatus : queryCondition.invoiceStatus,
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
		excelExport : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/settlement/excel_export",
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
				url : baseHomeUrl + "/admin/settlement/info",
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
				url : baseHomeUrl + "/admin/settlement/checkFlow",
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
		save : function(enterpriseId, enterpriseName, revenueFlowIds) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/settlement/save",
				data : {
					enterpriseId : enterpriseId,
					enterpriseName : enterpriseName,
					revenueFlowIds : revenueFlowIds,
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
		cancel : function(id) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/settlement/cancel",
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
		updateInvoiceStatus : function(id, invoiceStatus) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/settlement/update",
				data : {
					id : id,
					invoiceStatus : invoiceStatus
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