myapp.controller("agentEditAct", function($scope, $rootScope, $state, bean, agentService) {
	$scope.bean = bean;

	$scope.updateAgent = function() {
		agentService.update($scope.bean).then(function(response) {
			if (response.code == 1) {
				alert("保存成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});