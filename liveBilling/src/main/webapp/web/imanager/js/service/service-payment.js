myapp.factory('paymentService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/payment/page",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					paymentType : queryCondition.paymentType,
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
				url : baseHomeUrl + "/admin/payment/excel_export",
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
				url : baseHomeUrl + "/admin/payment/info",
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
		save : function(bean) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/payment/save",
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
				url : baseHomeUrl + "/admin/payment/update",
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
		process : function(id) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/payment/process",
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
		complete : function(id) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/payment/complete",
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
		cancel : function(id, cancelReason) {
			var d = $q.defer();
			$http({
				method : "POST",
				url : baseHomeUrl + "/admin/payment/cancel",
				data : {
					id : id,
					cancelReason : cancelReason,
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