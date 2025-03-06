myapp.controller("settlementDetailAct", function($scope, $http, $state, bean, settlementService) {
	$scope.bean = bean;

	$scope.updateInvoiceStatus = function(invoiceStatus) {
		settlementService.updateInvoiceStatus($scope.bean.id, invoiceStatus).then(function(response) {
			if (response.code == 1) {
				settlementService.info($scope.bean.id).then(function(response) {
					$scope.bean = resolveBean(response);
				});
			}
		});
	}
	$scope.cancelSettlement = function() {
		if (confirm("确定要作废吗？")) {
			if (confirm("作废以后操作无法恢复！！！")) {
				settlementService.cancel($scope.bean.id).then(function(response) {
					if (response.code == 1) {
						settlementService.info($scope.bean.id).then(function(response) {
							$scope.bean = resolveBean(response);
						});
					}
				});
			}
		}
	}
});