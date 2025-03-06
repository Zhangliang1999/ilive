myapp.factory('enterpriseService', function($http, $q, Upload) {
	return {
		info : function() {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/enterprise/info",
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
		productInfo : function() {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/enterprise/productInfo",
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
		productList : function() {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/enterprise/productList",
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