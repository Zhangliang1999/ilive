myapp.factory('packageService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/package/page",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					packageType : queryCondition.packageType,
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
		info : function(id) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/package/info",
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
			Upload.upload({
				url : baseHomeUrl + "/admin/package/save",
				data : {
					packageName : bean.packageName,
					packageType : bean.packageType,
					vaildDurationUnit : bean.vaildDurationUnit,
					vaildDurationValue : bean.vaildDurationValue,
					currentPrice : bean.currentPrice,
					orginalPrice : bean.orginalPrice,
					costPrice : bean.costPrice,
					introduceContent : bean.introduceContent,
					introduceUrl : bean.introduceUrl,
					channelTypes : bean.channelTypes,
					officialPlatformPrice : bean.officialPlatformPrice,
					channelAgentPrice : bean.channelAgentPrice,
					groupProductLibraryPrice : bean.groupProductLibraryPrice,
					productIds : bean.productIds,
					introduceImageFile : bean.introduceImageFile,
				},
				arrayKey : "[]",
				objectKey : ".i",
			}).then(function(response) {
				d.resolve(response.data)
			}, function(reason) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
		update : function(bean) {
			var d = $q.defer();
			Upload.upload({
				url : baseHomeUrl + "/admin/package/update",
				data : {
					id : bean.id,
					packageName : bean.packageName,
					currentPrice : bean.currentPrice,
					introduceContent : bean.introduceContent,
					introduceUrl : bean.introduceUrl,
					channelTypes : bean.channelTypes,
					officialPlatformPrice : bean.officialPlatformPrice,
					channelAgentPrice : bean.channelAgentPrice,
					groupProductLibraryPrice : bean.groupProductLibraryPrice,
					introduceImageFile : bean.introduceImageFile,
				},
				arrayKey : "[]",
				objectKey : ".i",
			}).then(function(response) {
				d.resolve(response.data)
			}, function(reason) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
		online : function(id) {
			var d = $q.defer();
			Upload.upload({
				url : baseHomeUrl + "/admin/package/online",
				data : {
					id : id,
				},
				arrayKey : "[]",
				objectKey : ".i",
			}).then(function(response) {
				d.resolve(response.data)
			}, function(reason) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
		offline : function(id) {
			var d = $q.defer();
			Upload.upload({
				url : baseHomeUrl + "/admin/package/offline",
				data : {
					id : id,
				},
				arrayKey : "[]",
				objectKey : ".i",
			}).then(function(response) {
				d.resolve(response.data)
			}, function(reason) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
		order : function(id, orderNum) {
			var d = $q.defer();
			Upload.upload({
				url : baseHomeUrl + "/admin/package/order",
				data : {
					id : id,
					orderNum : orderNum,
				},
				arrayKey : "[]",
				objectKey : ".i",
			}).then(function(response) {
				d.resolve(response.data)
			}, function(reason) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},

	}
});