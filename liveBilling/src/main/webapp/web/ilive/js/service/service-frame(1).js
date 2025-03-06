myapp.factory('frameService', function($http, $q) {
	return {
		getTop : function(authString) {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/frame/top",
				params : {
					authString : authString
				},
			}).then(function(response) {
				d.resolve(response.data)
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		},
		logout : function() {
			var d = $q.defer();
			$http({
				method : "GET",
				url : baseHomeUrl + "/ilive/logout"
			}).then(function(response) {
				d.resolve(response.data)
			}, function err(reason) {
				d.reject(reason);
			});
			return d.promise;
		}
	}
});