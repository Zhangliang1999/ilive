myapp.config(function($urlRouterProvider, $stateProvider) {
	$urlRouterProvider.when("", '/enterprise/total');
	$stateProvider.state("settlementList", {
		url : "/settlement/list?queryContent&startTime&endTime&status&invoiceStatus",
		templateUrl : "settlement/list.html",
		controller : 'settlementListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					queryContent : $stateParams.queryContent,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					invoiceStatus : $stateParams.invoiceStatus,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("settlementAdd", {
		url : "/settlement/add",
		templateUrl : "settlement/add.html",
		controller : 'settlementAddAct',
		resolve : {}
	}).state("settlementDetail", {
		url : "/settlement/detail?id",
		templateUrl : "settlement/detail.html",
		controller : 'settlementDetailAct',
		resolve : {
			bean : function($stateParams, settlementService) {
				return settlementService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("flowList", {
		url : "/flow/list?queryContent&startTime&endTime&status&invoiceStatus",
		templateUrl : "flow/list.html",
		controller : 'flowListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					enterpriseContent : $stateParams.enterpriseContent,
					userContent : $stateParams.userContent,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					settlementStatus : $stateParams.settlementStatus,
					flowType : $stateParams.flowType,
					pageNo : $stateParams.pageNo
				};
			},
			enterpriseBilling : function(enterpriseService) {
				return enterpriseService.info().then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("enterpriseTotal", {
		url : "/enterprise/total",
		templateUrl : "enterprise/total.html",
		controller : 'enterpriseTotalAct',
		resolve : {
			enterprise : function(enterpriseService) {
				return enterpriseService.info().then(function(response) {
					return resolveBean(response);
				});
			},
			enterpriseProductInfo : function(enterpriseService) {
				return enterpriseService.productInfo().then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("enterpriseProduct", {
		url : "/enterprise/product",
		templateUrl : "enterprise/productList.html",
		controller : 'enterpriseProductAct',
		resolve : {
			enterpriseAndProductList : function(productService) {
				return productService.list().then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("paymentList", {
		url : "/payment/list",
		templateUrl : "payment/list.html",
		controller : 'paymentListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					paymentType : $stateParams.paymentType,
					status : $stateParams.status,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("paymentDetail", {
		url : "/payment/detail?id",
		templateUrl : "payment/detail.html",
		controller : 'paymentDetailAct',
		resolve : {
			bean : function(paymentService, $stateParams) {
				return paymentService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("packageBasic", {
		url : "/package/basic",
		templateUrl : "package/basic.html",
		controller : 'packageBasicAct',
		resolve : {
			enterpriseProductInfo : function(enterpriseService) {
				return enterpriseService.productInfo().then(function(response) {
					return resolveBean(response);
				});
			},
			packageList : function(packageService) {
				return packageService.list({
					status : 2,
					channelType : 1,
					packageTypes : [ 1 ]
				}).then(function(response) {
					return resolveBean(response);
				});
			},
		}
	}).state("packageIncrement", {
		url : "/package/increment",
		templateUrl : "package/increment.html",
		controller : 'packageIncrementAct',
		resolve : {
			storagePackageList : function(packageService) {
				return packageService.list({
					status : 2,
					channelType : 1,
					packageTypes : [ 2 ]
				}).then(function(response) {
					return resolveBean(response);
				});
			},
			shortMessagePackageList : function(packageService) {
				return packageService.list({
					status : 2,
					channelType : 1,
					packageTypes : [ 3 ]
				}).then(function(response) {
					return resolveBean(response);
				});
			},
		}
	});
})
