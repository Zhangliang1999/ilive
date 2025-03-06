myapp.controller("packageAddAct", function($scope, $rootScope, $state, packageService, productService) {
	$scope.vaildDurationBeans = vaildDurationBeans;
	$scope.packageTypeBeans = packageTypeBeans;
	$scope.bean = {
		packageType : 1,
		vaildDurationUnit : 1,
		concurrenceProduct : 0,
		durationProduct : 0,
		shortMessageProduct : 0,
		storageProduct : 0,
		productIds : [],
		productList : []
	}

	$scope.selectFiles = function(files) {
		angular.forEach(files, function(file) {
			$scope.bean.introduceImageFile = file;
			$scope.checkImage();
		});
	}
	$scope.hasImage = false;
	$scope.checkImage = function() {
		try {
			if (typeof ($scope.bean.introduceImageFile) != "undefined") {
				$scope.hasImage = true;
				return;
			}
			if (typeof ($scope.bean.introduceImagePath) != "undefined" && $scope.bean.introduceImagePath != "") {
				$scope.hasImage = true;
				return;
			}
		} catch (e) {
		}
		$scope.hasImage = false;
		return;
	}
	$scope.checkImage();

	$scope.queryCondition = {
		incrementTypes : [],
		productTypes : [],
	};
	$scope.changeProductType = function() {
		$scope.queryCondition.productTypes = [];
		if ($scope.queryProductType1) {
			$scope.queryCondition.productTypes.push(1);
		}
		if ($scope.queryProductType2) {
			$scope.queryCondition.productTypes.push(2);
		}
		if ($scope.queryProductType3) {
			$scope.queryCondition.productTypes.push(3);
		}
		if ($scope.queryProductType4) {
			$scope.queryCondition.productTypes.push(4);
		}
		if ($scope.queryProductType5) {
			$scope.queryCondition.productTypes.push(5);
		}
		$scope.searchProductList();
	}

	$scope.searchProductList = function() {
		productService.list($scope.queryCondition).then(function(response) {
			if (response.code == 1) {
				$scope.productList = response.data;
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
	$scope.searchProductList();

	$scope.refreshProductInfo = function() {
		$scope.bean.productIds = [];
		$scope.bean.concurrenceProduct = 0;
		$scope.bean.durationProduct = 0;
		$scope.bean.shortMessageProduct = 0;
		$scope.bean.storageProduct = 0;
		for (var i = 0; i < $scope.bean.productList.length; i++) {
			var product = $scope.bean.productList[i];
			$scope.bean.productIds.push(product.id);
			if (product.productType == 2) {
				if ($scope.bean.storageProduct != -1) {
					if (product.incrementShowValue >= 0) {
						$scope.bean.storageProduct += product.incrementShowValue;
					} else {
						$scope.bean.storageProduct = -1;
					}
				}
			} else if (product.productType == 3) {
				if ($scope.bean.shortMessageProduct != -1) {
					if (product.incrementShowValue >= 0) {
						$scope.bean.shortMessageProduct += product.incrementShowValue;
					} else {
						$scope.bean.shortMessageProduct = -1;
					}
				}
			} else if (product.productType == 4) {
				if ($scope.bean.durationProduct != -1) {
					if (product.incrementShowValue >= 0) {
						$scope.bean.durationProduct += product.incrementShowValue;
					} else {
						$scope.bean.durationProduct = -1;
					}
				}
			} else if (product.productType == 5) {
				if ($scope.bean.concurrenceProduct != -1) {
					if (product.incrementShowValue >= 0) {
						$scope.bean.concurrenceProduct += product.incrementShowValue;
					} else {
						$scope.bean.concurrenceProduct = -1;
					}
				}
			}else if (product.productType == 6) {
				if ($scope.bean.concurrenceProduct != -1) {
					if (product.incrementShowValue >= 0) {
						$scope.bean.concurrenceProduct += product.incrementShowValue;
					} else {
						$scope.bean.concurrenceProduct = -1;
					}
				}
			}

		}
	}

	$scope.chooseProduct = function() {
		for (var i = 0; i < $scope.productList.length; i++) {
			var product = $scope.productList[i];
			if (product.isChecked) {
				var existed = false;
				for (var j = 0; j < $scope.bean.productList.length; j++) {
					var existedProduct = $scope.bean.productList[j];
					if (existedProduct.id == product.id) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					$scope.bean.productList.push(product);
				}
			}
		}
		$scope.refreshProductInfo();
		$("#myModal").modal('hide');
	}

	$scope.removeProduct = function(product) {
		for (var i = 0; i < $scope.bean.productList.length; i++) {
			var existedProduct = $scope.bean.productList[i];
			if (existedProduct.id == product.id) {
				$scope.bean.productList.splice(i, 1);
			}
		}
		$scope.refreshProductInfo();
	}

	$scope.savePackage = function() {
		if ($scope.bean.packageType == 1) {
			var hasBasicProduct = false;
			for (var i = 0; i < $scope.bean.productList.length; i++) {
				var existedProduct = $scope.bean.productList[i];
				if (existedProduct.productType == 1) {
					if (hasBasicProduct) {
						alert("只能选择一个功能权限资源包");
						return;
					} else {
						hasBasicProduct = true;
					}
				}
			}
			if (!hasBasicProduct) {
				alert("至少选择一个功能权限资源包");
				return;
			}
		} else if ($scope.bean.packageType == 2) {
			for (var i = 0; i < $scope.bean.productList.length; i++) {
				var existedProduct = $scope.bean.productList[i];
				if (existedProduct.packageType != 2) {
					alert("存储叠加套餐只能选择存储空间资源包");
					return;
				}
			}
		} else if ($scope.bean.packageType == 3) {
			for (var i = 0; i < $scope.bean.productList.length; i++) {
				var existedProduct = $scope.bean.productList[i];
				if (existedProduct.packageType != 3) {
					alert("短信叠加套餐只能选择短信资源包");
					return;
				}
			}
		}

		if (typeof ($scope.bean.vaildDurationValue) == "undefined") {
			alert("请填写套餐周期");
			return;
		} else if ($scope.bean.vaildDurationValue <= 0) {
			alert("套餐周期必须大于0");
			return;
		}
		if (typeof ($scope.bean.orginalPrice) == "undefined") {
			alert("请填写套餐原价");
			return;
		} else if ($scope.bean.orginalPrice <= 0) {
			alert("套餐原价必须大于0");
			return;
		}
		if (typeof ($scope.bean.costPrice) == "undefined") {
			alert("请填写成本价");
			return;
		} else if ($scope.bean.costPrice <= 0) {
			alert("成本价必须大于0");
			return;
		}

		$scope.bean.channelTypes = "";
		if ($scope.bean.enableChannelOfOfficialPlatform) {
			$scope.bean.channelTypes += "1,";
		}
		if ($scope.bean.enableChannelOfChannelAgent) {
			$scope.bean.channelTypes += "2,";
		}
		if ($scope.bean.enableChannelOfGroupProductLibrary) {
			$scope.bean.channelTypes += "3,";
		}
		if ($scope.bean.enableChannelOfinside) {
			$scope.bean.channelTypes += "4,";
		}
		if ($scope.bean.channelTypes.length > 0) {
			$scope.bean.channelTypes = $scope.bean.channelTypes.substring(0, $scope.bean.channelTypes.length - 1);
		}
		packageService.save($scope.bean).then(function(response) {
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