myapp.controller("agentAddAct", function($scope, $rootScope, $state, agentService) {
	$scope.bean = {}

	$scope.saveAgent = function() {
		if (typeof ($scope.bean.agentName) == "undefined" || $scope.bean.agentName <= 0) {
			alert("代理商简介不能为空！");
			return;
		}
		if (typeof ($scope.bean.deductionRate) == "undefined" || $scope.bean.deductionRate <= 0) {
			alert("代理商扣率不能为空！");
			return;
		}
		if (typeof ($scope.bean.fullName) == "undefined" || $scope.bean.fullName <= 0) {
			alert("公司全称不能为空！");
			return;
		}
		if (typeof ($scope.bean.organizationCode) == "undefined" || $scope.bean.organizationCode <= 0) {
			alert("公司组织机构代码不能为空！");
			return;
		}
		var codePattern=/[a-zA-Z0-9]{8}-[a-zA-Z0-9]/;
		if (!codePattern.test($scope.bean.organizationCode)) {
			alert('请输入有效的组织机构代码！');
			return;
		}
		if (typeof ($scope.bean.legalRepresentative) == "undefined" || $scope.bean.legalRepresentative <= 0) {
			alert("公司法定代表人不能为空！");
			return;
		}
		if (typeof ($scope.bean.registeredAddress) == "undefined" || $scope.bean.registeredAddress <= 0) {
			alert("公司注册地址不能为空！");
			return;
		}
		if (typeof ($scope.bean.bankName) == "undefined" || $scope.bean.bankName <= 0) {
			alert("银行开户行不能为空！");
			return;
		}
		if (typeof ($scope.bean.bankAccount) == "undefined" || $scope.bean.bankAccount <= 0) {
			alert("银行账户不能为空！");
			return;
		}
		var pattern = /^([1-9]{1})(\d{14}|\d{18})$/;
		if (!pattern.test($scope.bean.bankAccount)) {
			alert('请输入有效的银行卡号！');
			return;
		}
		if (typeof ($scope.bean.contactName) == "undefined" || $scope.bean.contactName <= 0) {
			alert("联系人不能为空！");
			return;
		}
		if (typeof ($scope.bean.contactNumber) == "undefined" || $scope.bean.contactNumber <= 0) {
			alert("联系方式不能为空！");
			return;
		}
		var phoneReg = /(^1[3|4|5|7|8|9]\d{9}$)|(^09\d{8}$)/;
		var mobileReg=/^\d{3,4}-\d{7,8}$/;
		if (!phoneReg.test($scope.bean.contactNumber)&&!mobileReg.test($scope.bean.contactNumber)) {
			alert('请输入有效的联系电话！');
			return;
		}
		agentService.save($scope.bean).then(function(response) {
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