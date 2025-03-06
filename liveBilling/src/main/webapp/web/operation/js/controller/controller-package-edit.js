myapp.controller("packageEditAct", function($scope, $rootScope, $state,bean, packageService, productService) {
	$scope.bean = bean;
	$scope.vaildDurationBeans = vaildDurationBeans;
	$scope.packageTypeBeans = packageTypeBeans;

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

	$scope.updatePackage = function() {
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
		packageService.update($scope.bean).then(function(response) {
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
	$scope.onlinePackage = function() {
		packageService.online($scope.bean.id).then(function(response) {
			if (response.code == 1) {
				alert("发售成功");
				$("#myModal").modal('hide');
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
	$scope.offlinePackage = function() {
		packageService.offline($scope.bean.id).then(function(response) {
			if (response.code == 1) {
				alert("停售成功");
				$rootScope.returnPrevious();
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});