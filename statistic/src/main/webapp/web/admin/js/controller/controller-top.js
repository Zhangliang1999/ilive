myapp.controller("topAct", function($scope, $rootScope, $http, $state, frameService) {
	$scope.logout = function() {
		frameService.logout().then(function(response) {
		}, function(error) {
			$scope.error = error;
		});
		window.location.href = logoutUrl;
	}
});