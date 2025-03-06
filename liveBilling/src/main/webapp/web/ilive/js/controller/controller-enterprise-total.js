myapp.controller("enterpriseTotalAct", function($scope, $http, $state, $cookies, enterprise, enterpriseProductInfo,
		workOrderService) {
	$scope.enterprise = enterprise;
	$scope.enterpriseProductInfo = enterpriseProductInfo;

	var totalStorage = enterpriseProductInfo.storageProduct;
	var remainStorage = enterpriseProductInfo.storageProduct - enterpriseProductInfo.usedStorageProduct;
	if (remainStorage < 0) {
		remainStorage = 0;
	}
	var usedStorage = totalStorage - remainStorage;
	$scope.storageChartData = [ {
		"item" : "已用",
		"count" : usedStorage
	}, {
		"item" : "剩余",
		"count" : remainStorage
	} ];
	if (usedStorage / totalStorage > 0.6) {
		$scope.storageChartColors = [ "#FDA500", "#D2D7DB" ];
	} else {
		$scope.storageChartColors = [ "#48B20A", "#D2D7DB" ];
	}
	if (usedStorage / totalStorage > 0.8) {
		$scope.storageChartColors = [ "#FD5D00", "#D2D7DB" ];
	}
	if (usedStorage / totalStorage >= 1) {
		$scope.storageChartColors = [ "#FE4400", "#D2D7DB" ];
	}
	$scope.storageChartLabel = (usedStorage / 1024.0 / 1024.0).toFixed(2) + "G";
	var totalShortMessage = enterpriseProductInfo.shortMessageProduct;
	var remainShortMessage = enterpriseProductInfo.shortMessageProduct - enterpriseProductInfo.usedShortMessageProduct;
	if (remainShortMessage < 0) {
		remainShortMessage = 0;
	}
	var usedShortMessage = totalShortMessage - remainShortMessage;
	$scope.shortMessageChartData = [ {
		"item" : "已用",
		"count" : usedShortMessage
	}, {
		"item" : "剩余",
		"count" : remainShortMessage
	} ];
	if (usedShortMessage / totalShortMessage >= 0.6) {
		$scope.shortMessageChartColors = [ "#FDA500", "#D2D7DB" ];
	} else {
		$scope.shortMessageChartColors = [ "#48B20A", "#D2D7DB" ];
	}
	if (usedShortMessage / totalShortMessage >= 0.8) {
		$scope.shortMessageChartColors = [ "#FD5D00", "#D2D7DB" ];
	}
	if (usedShortMessage / totalShortMessage >= 1) {
		$scope.shortMessageChartColors = [ "#FE4400", "#D2D7DB" ];
	}
	$scope.shortMessageChartLabel = usedShortMessage + "条";
	
	var totalDurationProduct = enterpriseProductInfo.durationProduct;
	var remainDurationProduct = enterpriseProductInfo.durationProduct - enterpriseProductInfo.usedDurationProduct;
	if (remainDurationProduct < 0) {
		remainDurationProduct = 0;
	}
	var usedDurationProduct = totalDurationProduct - remainDurationProduct;
	$scope.durationProductChartData = [ {
		"item" : "已用",
		"count" : usedDurationProduct
	}, {
		"item" : "剩余",
		"count" : remainDurationProduct
	} ];
	if ((usedDurationProduct/3600.0).toFixed(2) == -1) {
		$scope.durationProductChartColors = [ "#48B20A", "#D2D7DB" ];
	}
	if (usedDurationProduct / totalDurationProduct >= 0.6) {
		$scope.durationProductChartColors = [ "#FDA500", "#D2D7DB" ];
	} else {
		$scope.durationProductChartColors = [ "#48B20A", "#D2D7DB" ];
	}
	
	if (usedDurationProduct / totalDurationProduct >= 0.8) {
		$scope.durationProductChartColors = [ "#FD5D00", "#D2D7DB" ];
	}
	if (usedDurationProduct / totalDurationProduct >= 1) {
		$scope.durationProductChartColors = [ "#FE4400", "#D2D7DB" ];
	}
	if((usedDurationProduct/3600.0).toFixed(2) == -1){
		$scope.durationProductChartLabel="无限小时";
		$scope.durationProductChartColors = [ "#48B20A", "#D2D7DB" ];
	}else{
		$scope.durationProductChartLabel = (usedDurationProduct/3600.0).toFixed(2) + "小时";
	}
	$scope.agreedClause = false;
	$scope.applyRevenueAccount = function() {
		if (!$scope.agreedClause) {
			alert("需要同意《天翼直播收益账户管理办法》才能提交！");
			return;
		}
		if (typeof ($scope.contactName) == "undefined" || $scope.contactName.trim().length == 0) {
			alert("请填写联系人");
			return;
		}
		if (typeof ($scope.contactPhoneNumber) == "undefined" || $scope.contactPhoneNumber.trim().length == 0) {
			alert("请填写联系方式");
			return;
		}
		var workOrder = {
			workOrderType : 1,
			contactName : $scope.contactName,
			contactPhoneNumber : $scope.contactPhoneNumber
		};
		workOrderService.save(workOrder).then(function(response) {
			if (response.code == 1) {
				alert("提交成功");
				$("#revenueModal").modal('hide');
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
	$scope.applyRedPackageAccount = function() {
		if (!$scope.agreedClause) {
			alert("需要同意《天翼直播收益账户管理办法》才能提交！");
			return;
		}
		if (typeof ($scope.contactName) == "undefined" || $scope.contactName.trim().length == 0) {
			alert("请填写联系人");
			return;
		}
		if (typeof ($scope.contactPhoneNumber) == "undefined" || $scope.contactPhoneNumber.trim().length == 0) {
			alert("请填写联系方式");
			return;
		}
		var workOrder = {
			workOrderType : 2,
			contactName : $scope.contactName,
			contactPhoneNumber : $scope.contactPhoneNumber
		};
		workOrderService.save(workOrder).then(function(response) {
			if (response.code == 1) {
				alert("提交成功");
				$("#redPackageModal").modal('hide');
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});