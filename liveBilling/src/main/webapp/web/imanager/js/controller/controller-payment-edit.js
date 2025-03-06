myapp.controller("paymentEditAct",
		function($scope, $rootScope, $state, bean, paymentService, agentService, operationUserService) {
			$scope.bean = bean;
			$scope.paymentAutoBeans = paymentAutoBeans;
			$scope.paymentWayBeans = paymentWayBeans;
			agentService.list({
				status : 2
			}).then(function(response) {
				if (response.code == 1) {
					$scope.agentList = response.data
					for (var i = 0; i < $scope.agentList.length; i++) {
						if ($scope.agentList[i].id === $scope.bean.agentId) {
							$scope.agentInfo = $scope.agentList[i];
						}
					}
				}
			}, function(error) {
				$scope.error = error;
			});
			operationUserService.list({}).then(function(response) {
				if (response.code == 1) {
					$scope.userList = response.data
					for (var i = 0; i < $scope.userList.length; i++) {
						if ($scope.userList[i].userId === $scope.bean.sellUserId) {
							$scope.userInfo = $scope.userList[i];
						}
					}
				}
			}, function(error) {
				$scope.error = error;
			});

			$scope.$watch("userInfo", function(newValue, oldValue, scope) {
				if ($scope.userInfo) {
					$scope.bean.sellUserId = $scope.userInfo.userId;
				}
			}, true);
			$scope.$watch("agentInfo", function(newValue, oldValue, scope) {
				if ($scope.agentInfo) {
					if ($scope.bean.agentDeductionRate && $scope.bean.agentDeductionRate > 0) {
						$scope.bean.agentId = $scope.agentInfo.id;
						$scope.bean.agentDeductionRate = $scope.agentInfo.deductionRate;
					}
				}
			}, true);

			$scope.updateAgent = function() {
				paymentService.update($scope.bean).then(function(response) {
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
			$scope.processAgent = function() {
				if (confirm("确定要设为处理中吗？")) {
					paymentService.process($scope.bean.id).then(function(response) {
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
			}
			$scope.completeAgent = function() {
				if (confirm("确定要完成订单吗？")) {
					paymentService.complete($scope.bean.id).then(function(response) {
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
			}
			$scope.cancelAgent = function() {
				if (typeof ($scope.bean.cancelReason) == "undefined" || $scope.bean.cancelReason == null
						|| $scope.bean.cancelReason.trim().length == 0) {
					alert("请填写退回原因");
					return;
				}
				paymentService.cancel($scope.bean.id, $scope.bean.cancelReason).then(function(response) {
					if (response.code == 1) {
						alert("保存成功");
						$('#myModal').modal('hide');
						setTimeout(function() {
							$rootScope.returnPrevious();
						}, 500);
					} else {
						alert(response.message);
					}
				}, function(error) {
					$scope.error = error;
				});
			}
		});