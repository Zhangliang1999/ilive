myapp.factory('enterpriseStatisticService', function($http, $q, Upload) {
	return {
		getLiveTotalData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/enterprise/liveTotal",
				params : {
					enterpriseId:queryCondition.enterpriseId
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
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/enterprise/page",
				params : {
					enterpriseId : queryCondition.enterpriseId,
					enterpriseName : queryCondition.enterpriseName,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					bindPhone: queryCondition.bindPhone,
					certStatus: queryCondition.certStatus,
					stamp: queryCondition.stamp,
					entype: queryCondition.entype,
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
				url : baseHomeUrl + "/admin/enterprise/excel_export",
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
		getBeginLiveDataForColumn : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/enterprise/beginLiveForColumn",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
		getBeginLocationData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/enterprise/beginLocation",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
		getTotalData : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/enterprise/total",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/max",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/viewSource",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/viewTimeForLine",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/viewTimeForColumn",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/viewLocation",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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
				url : baseHomeUrl + "/admin/enterprise/viewTimeForPie",
				params : {
					enterpriseId : queryCondition.enterpriseId,
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