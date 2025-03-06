myapp.controller("productAddAct", function($scope, $rootScope, $state, functionList, productService) {
	$scope.functionList = functionList;
	$scope.productTypeBeans = productTypeBeans;
	$scope.bean = {
		productType : 1,
	}

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

	$scope.saveProduct = function() {
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
		productService.save($scope.bean).then(function(response) {
			if (response.code == 1) {
				alert("添加成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});

	}
});