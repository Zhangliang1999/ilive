myapp.controller("settlementListAct", function($scope, $http, $state, $cookies, queryCondition, settlementService) {
	$scope.queryCondition = queryCondition;
	// 发票搜索条件
	$scope.queryInvoiceStatusLabel = "全部";
	$scope.invoiceStatusBeans = invoiceStatusBeans;
	// 状态搜索条件
	$scope.queryStatusLabel = "全部";
	$scope.statusBeans = settlementLogStatusBeans;
	$scope.searchList = function(pageNo, isPage) {
		if (!isPage || (pageNo > 0 && !($scope.page.pageNo == pageNo))) {
			if ($scope.queryCondition.status != null) {
				for (var i = 0; i < $scope.statusBeans.length; i++) {
					var statusBean = $scope.statusBeans[i];
					if ($scope.queryCondition.status == statusBean.value) {
						$scope.queryStatusLabel = statusBean.label;
					}
				}
			} else {
				$scope.queryStatusLabel = "全部";
			}
			if ($scope.queryCondition.invoiceStatus != null) {
				for (var i = 0; i < $scope.invoiceStatusBeans.length; i++) {
					var statusBean = $scope.invoiceStatusBeans[i];
					if ($scope.queryCondition.invoiceStatus == statusBean.value) {
						$scope.queryInvoiceStatusLabel = statusBean.label;
					}
				}
			} else {
				$scope.queryInvoiceStatusLabel = "全部";
			}
			settlementService.page($scope.queryCondition, pageNo).then(function(response) {
				$scope.page = resolveBean(response);
			}, function(error) {
				$scope.error = error;
			});
		}
	}
	$scope.searchList(1, false);

	$scope.changeInvoiceStatus = function(statusBean) {
		if (statusBean) {
			$scope.queryCondition.invoiceStatus = statusBean.value;
		} else {
			$scope.queryCondition.invoiceStatus = null;
		}
		$scope.searchList(1, false);
	}

	$scope.changeStatus = function(statusBean) {
		if (statusBean) {
			$scope.queryCondition.status = statusBean.value;
		} else {
			$scope.queryCondition.status = null;
		}
		$scope.searchList(1, false);
	}

	$scope.excelExport = function() {
		var url = baseHomeUrl + "/ilive/settlement/excel_export?i=i";
		if ($scope.queryCondition.queryContent) {
			url += "&queryContent=" + $scope.queryCondition.queryContent;
		}
		if ($scope.queryCondition.startTime) {
			url += "&startTime=" + $scope.queryCondition.startTime;
		}
		if ($scope.queryCondition.endTime) {
			url += "&endTime=" + $scope.queryCondition.endTime;
		}
		if ($scope.queryCondition.status) {
			url += "&status=" + $scope.queryCondition.status;
		}
		if ($scope.queryCondition.invoiceStatus) {
			url += "&invoiceStatus=" + $scope.queryCondition.invoiceStatus;
		}
		window.location.href = url;
	}

	$scope.checkAll = function(m) {
		for (i = 0; i < $scope.page.list.length; i++) {
			var bean = $scope.page.list[i];
			if (m === true) {
				bean.isChecked = true;
			} else {
				bean.isChecked = false;
			}
		}
	}

	$scope.changePageSize = function(pageSize) {
		if ($scope.page.pageSize != pageSize) {
			var expireDate = new Date();
			expireDate.setDate(expireDate.getDate() + 100 * 365);
			$cookies.put("cookie_page_size", pageSize, {
				'expires' : expireDate.toUTCString(),
				'path' : '/'
			})
			$scope.searchList(1, false);
		}
	}

	$scope.changePageNo = function(pageNo) {
		if ($scope.page.pageNo != pageNo) {
			if (pageNo <= 0) {
				pageNo = 1;
			}
			if (pageNo > $scope.page.totalPage) {
				pageNo = $scope.page.totalPage;
			}
			$scope.searchList(pageNo, false);
		}
	}

	$scope.addSettlementLog = function() {
		$state.go("settlementAdd", {});
	}
	$scope.viewDetail = function(id) {
		$state.go("settlementDetail", {
			id : id
		});
	}
});