myapp.controller("settlementAddAct", function($scope, $rootScope, $state, flowService, settlementService) {
	const platformRate = 0.05;
	$scope.addType = 1;
	$scope.changeType = function(type) {
		$scope.addType = type;
		$scope.flowList = [];
	};
	$scope.queryCondition = {
		enterpriseId : null,
		settlementStatus : 1
	};
	$scope.bean = {
		flowNum : 0,
		settleAmount : 0.00,
		platformAmount : 0.00,
		actualAmount : 0.00,
	};
	$scope.searchList = function() {
		flowService.listForSettlement($scope.queryCondition).then(function(response) {
			$scope.flowList = resolveBean(response);
		}, function(error) {
			$scope.error = error;
		});
	}
	$scope.errorMessage = "";
	$scope.errorFlowIdList = [];
	$scope.searchListByIds = function() {
		$scope.errorMessage = "";
		$scope.errorFlowIdList = [];
		$scope.queryCondition.revenueFlowIds = [];
		const flowIdArray = $scope.flowIds.split("\n");
		for (var i = 0; i < flowIdArray.length; i++) {
			const flowId = flowIdArray[i];
			if (isNaN(flowId)) {
				$scope.errorMessage = "不符合流水ID：";
				$scope.errorFlowIdList.push(flowId + "");
			} else {
				$scope.queryCondition.revenueFlowIds.push(flowId);
			}
		}
		if ($scope.errorFlowIdList.length > 0) {
			return;
		}
		flowService.listForSettlement($scope.queryCondition).then(function(response) {
			if (response.code == 1) {
				$scope.flowList = response.data;
				$scope.errorMessage = "";
				$scope.errorFlowIdList = [];
				for (i = 0; i < $scope.flowList.length; i++) {
					var bean = $scope.flowList[i];
					$scope.bean.flowNum = $scope.bean.flowNum + 1;
					$scope.bean.settleAmount = floatAdd($scope.bean.settleAmount, bean.flowPrice);
					$scope.bean.platformAmount = Math.round(floatMul($scope.bean.settleAmount, platformRate) * 100) / 100;
					$scope.bean.actualAmount = floatSub($scope.bean.settleAmount, $scope.bean.platformAmount);
				}
			} else if (response.code == 501) {
				window.location.href = loginUrl;
			} else if (response.code == 502) {
				$scope.errorMessage = "不符合流水ID：";
				$scope.errorFlowIdList = response.data;
			} else if (response.code == 503) {
				$scope.errorMessage = "不符合流水ID：";
				$scope.errorFlowIdList = response.data;
			} else if (response.code == 504) {
				$scope.errorMessage = "不是同一个企业的流水ID";
				$scope.errorFlowIdList = response.data;
			} else {
				return response.data;
			}
		}, function(error) {
			$scope.error = error;
		});
	}

	$scope.checkAll = function(m) {
		for (i = 0; i < $scope.flowList.length; i++) {
			var bean = $scope.flowList[i];
			if (m === true) {
				bean.isChecked = true;
				$scope.bean.flowNum = $scope.bean.flowNum + 1;
				$scope.bean.settleAmount = floatAdd($scope.bean.settleAmount, bean.flowPrice);
				$scope.bean.platformAmount = Math.round(floatMul($scope.bean.settleAmount, platformRate) * 100) / 100;
				$scope.bean.actualAmount = floatSub($scope.bean.settleAmount, $scope.bean.platformAmount);
			} else {
				bean.isChecked = false;
			}
		}
		if (m === true) {
		} else {
			$scope.bean.flowNum = 0;
			$scope.bean.settleAmount = 0.00;
			$scope.bean.platformAmount = 0.00;
			$scope.bean.actualAmount = 0.00;
		}
	}

	$scope.changeCheck = function(bean) {
		bean.isChecked = !bean.isChecked;
		if (bean.isChecked) {
			$scope.bean.flowNum = $scope.bean.flowNum + 1;
			$scope.bean.settleAmount = floatAdd($scope.bean.settleAmount, bean.flowPrice);
			$scope.bean.platformAmount = Math.round(floatMul($scope.bean.settleAmount, platformRate) * 100) / 100;
			$scope.bean.actualAmount = floatSub($scope.bean.settleAmount, $scope.bean.platformAmount);
		} else {
			$scope.bean.flowNum = $scope.bean.flowNum - 1;
			$scope.bean.settleAmount = floatSub($scope.bean.settleAmount, bean.flowPrice);
			$scope.bean.platformAmount = Math.round(floatMul($scope.bean.settleAmount, platformRate) * 100) / 100;
			$scope.bean.actualAmount = floatSub($scope.bean.settleAmount, $scope.bean.platformAmount);
		}
	}
	
	$scope.saveSettlement = function() {
		$scope.bean.enterpriseId = $scope.queryCondition.enterpriseId;
		$scope.bean.enterpriseName = $scope.queryContent;
		if ($scope.addType == 1) {
			$scope.bean.revenueFlowIds = [];
			for (i = 0; i < $scope.flowList.length; i++) {
				var bean = $scope.flowList[i];
				if (bean.isChecked === true) {
					$scope.bean.revenueFlowIds.push(bean.flowId);
				}
			}
		} else if ($scope.addType == 2) {
			$scope.bean.revenueFlowIds = [];
			for (i = 0; i < $scope.flowList.length; i++) {
				var bean = $scope.flowList[i];
				$scope.bean.revenueFlowIds.push(bean.flowId);
			}
		}
		settlementService.save($scope.bean.enterpriseId, $scope.bean.enterpriseName, $scope.bean.revenueFlowIds).then(
				function(response) {
					if (response.code == 1) {
						alert("结算成功");
						$("#myModal").modal('hide');
						setTimeout(function() {
							$rootScope.returnPrevious();
						}, 500);
					} else if (response.code == 501) {
						window.location.href = loginUrl;
					} else if (response.code == 502) {
						$scope.errorMessage = "不符合流水ID：";
						$scope.errorFlowIdList = response.data;
					} else if (response.code == 503) {
						$scope.errorMessage = "不符合流水ID：";
						$scope.errorFlowIdList = response.data;
					} else if (response.code == 504) {
						$scope.errorMessage = "不是同一个企业的流水ID";
						$scope.errorFlowIdList = response.data;
					} else {
						return response.data;
					}
				}, function(error) {
					$scope.error = error;
				});
	}
});