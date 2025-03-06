myapp.controller("productEditAct", function($scope, $rootScope, $state, bean, functionList, productService) {
	$scope.bean = bean;
	$scope.functionList = functionList;
	$scope.productTypeBeans = productTypeBeans;

	$scope.checkAll = function(functionBean) {
		for (var j = 0; j < functionBean.childList.length; j++) {
			var childFunctionBean = functionBean.childList[j];
			childFunctionBean.isChecked = functionBean.isChecked
		}
	}

	$scope.isCheckAll = function(functionBean) {
		var isCheckAll = true;
		for (var j = 0; j < functionBean.childList.length; j++) {
			var childFunctionBean = functionBean.childList[j];
			if (!childFunctionBean.isChecked) {
				isCheckAll = false;
				break;
			}
		}
		functionBean.isChecked = isCheckAll;
	}
	if ($scope.bean.productType == 1) {
		var functionCodeArray = $scope.bean.functionCodes.split(",");
		for (var i = 0; i < $scope.functionList.length; i++) {
			var functionBean = $scope.functionList[i];
			for (var j = 0; j < functionBean.childList.length; j++) {
				var childFunctionBean = functionBean.childList[j];
				for (var m = 0; m < functionCodeArray.length; m++) {
					if (childFunctionBean.functionCode == functionCodeArray[m]) {
						childFunctionBean.isChecked = true;
						break;
					}
				}
			}
			$scope.isCheckAll(functionBean);
		}
	}

	$scope.updateProduct = function() {
		var checkedFunctionCodeList = [];
		for (var i = 0; i < $scope.functionList.length; i++) {
			var functionBean = $scope.functionList[i];
			for (var j = 0; j < functionBean.childList.length; j++) {
				var childFunctionBean = functionBean.childList[j];
				if (childFunctionBean.isChecked) {
					checkedFunctionCodeList.push(childFunctionBean.functionCode);
				}
			}
		}
		var functionCodes = checkedFunctionCodeList.join(",");
		$scope.bean.functionCodes = functionCodes;
		$scope.bean.incrementValue = $scope.bean.incrementShowValue;
		productService.update($scope.bean).then(function(response) {
			if (response.code == 1) {
				alert("修改成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});

	}
});