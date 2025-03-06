myapp.factory('operationUserService', function($http, $q, Upload) {
	return {
		list : function(queryCondition) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/operationUser/list",
				params : {},
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