myapp.factory('productService', function($http, $q, Upload) {
	return {
		page : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/product/page",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					productTypes : queryCondition.productTypes,
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
				url : baseHomeUrl + "/admin/product/list",
				params : {
					queryContent : queryCondition.queryContent,
					startTime : queryCondition.startTime,
					endTime : queryCondition.endTime,
					status : queryCondition.status,
					productTypes : queryCondition.productTypes,
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
				url : baseHomeUrl + "/admin/product/info",
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
				url : baseHomeUrl + "/admin/product/save",
				data : bean,
			}).then(function(response) {
				d.resolve(response.data)
			}, function(response) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
		update : function(bean) {
			var d = $q.defer();
			Upload.upload({
				url : baseHomeUrl + "/admin/product/update",
				data : bean,
			}).then(function(response) {
				d.resolve(response.data)
			}, function(response) {
				d.reject(reason);
			}, function(evt) {
				progress = parseInt(100.0 * evt.loaded / evt.total);
			});
			return d.promise;
		},
	}
});