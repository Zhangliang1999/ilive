myapp.controller("packageIncrementAct", function($scope, $http, $state, $cookies, storagePackageList, shortMessagePackageList,
		packageService, paymentService) {
	$scope.storagePackageList = storagePackageList;
	$scope.shortMessagePackageList = shortMessagePackageList;

	$scope.changeIndex = function(index) {
		$scope.packageIndex = index;
		if (index == 1) {
			$scope.selectedPackage = $scope.storagePackageList[0];
		} else if (index == 2) {
			$scope.selectedPackage = $scope.shortMessagePackageList[0];
		}
	}
	$scope.changeIndex = function(index) {
		$scope.packageIndex = index;
		if (index == 1) {
			$scope.selectedPackage = $scope.storagePackageList[0];
		} else if (index == 2) {
			$scope.selectedPackage = $scope.shortMessagePackageList[0];
		}
		$scope.selectedStoragePackage = $scope.storagePackageList[0];
		$scope.selectedShortMessagePackage = $scope.shortMessagePackageList[0];
	}
	$scope.changeIndex(1);
	$scope.viewPackageDetail = function() {
		if ($scope.selectedPackage && $scope.selectedPackage.introduceUrl.length > 0) {
			window.open($scope.selectedPackage.introduceUrl);
		}
	}
	$scope.agreedClause = false;
	$scope.submitPayment = function() {
		if (typeof ($scope.selectedPackage) == "undefined" || $scope.selectedPackage.id <= 0) {
			alert("请选择一个叠加包");
			return;
		}
		if (typeof ($scope.contactName) == "undefined" || $scope.contactName.length == 0) {
			alert("请填写联系人！");
			return;
		}
		if (typeof ($scope.contactNumber) == "undefined" || $scope.contactNumber.length == 0) {
			alert("请填写联系电话！");
			return;
		}
		var phoneReg = /(^1[3|4|5|7|8]\d{9}$)|(^09\d{8}$)/;
		var mobileReg=/^\d{3,4}-\d{7,8}$/;
		if (!phoneReg.test($scope.contactNumber)&&!mobileReg.test($scope.contactNumber)) {
			alert('请输入有效的联系电话！');
			return;
		}
		if (!$scope.agreedClause) {
			alert("需要同意《天翼直播平台服务条款》才能提交！");
			return;
		}
		var payment = {
			paymentType : 1,
			packageId : $scope.selectedPackage.id,
			contactNumber : $scope.contactNumber,
			contactName : $scope.contactName
		};
		paymentService.save(payment).then(function(response) {
			if (response.code == 1) {
				$("#myModal").modal('hide');
				$("#resultModal").modal('show');
			} else {
				alert(response.message);
			}
		}, function(error) {
			$scope.error = error;
		});
	}
});