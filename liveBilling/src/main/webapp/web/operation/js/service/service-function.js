myapp.factory('functionService', function($http, $q, Upload) {
	return {
		list : function(queryCondition, pageNo) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/admin/function/list",
			}).then(function(response) {
				d.resolve(response.data);
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
	}
});