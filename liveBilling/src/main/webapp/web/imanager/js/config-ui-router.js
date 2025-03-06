myapp.config(function($urlRouterProvider, $stateProvider) {
	$urlRouterProvider.when("", '/flow/list');
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
		url : "/flow/list?enterpriseContent&userContent&startTime&endTime&settlementStatus&flowType",
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
			}
		}
	}).state("enterpriseList", {
		url : "/enterprise/list?queryContent",
		templateUrl : "enterprise/list.html",
		controller : 'enterpriseListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					queryContent : $stateParams.queryContent,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("agentList", {
		url : "/agent/list?queryContent&startTime&endTime&status ",
		templateUrl : "agent/list.html",
		controller : 'agentListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					queryContent : $stateParams.queryContent,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("agentAdd", {
		url : "/agent/add",
		templateUrl : "agent/add.html",
		controller : 'agentAddAct',
		resolve : {}
	}).state("agentEdit", {
		url : "/agent/edit?id",
		templateUrl : "agent/edit.html",
		controller : 'agentEditAct',
		resolve : {
			bean : function($stateParams, agentService) {
				return agentService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("paymentList", {
		url : "/payment/list?enterpriseId&startTime&endTime&status&paymentType&pageNo",
		templateUrl : "payment/list.html",
		controller : 'paymentListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					enterpriseId : $stateParams.enterpriseId,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("paymentAdd", {
		url : "/payment/add",
		templateUrl : "payment/add.html",
		controller : 'paymentAddAct',
		resolve : {}
	}).state("paymentEdit", {
		url : "/payment/edit?id",
		templateUrl : "payment/edit.html",
		controller : 'paymentEditAct',
		resolve : {
			bean : function($stateParams, paymentService) {
				return paymentService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("workOrderList", {
		url : "/workOrder/list?enterpriseId&startTime&endTime&status&wordOrderType&pageNo",
		templateUrl : "workOrder/list.html",
		controller : 'workOrderListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					enterpriseId : $stateParams.enterpriseId,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					wordOrderType : $stateParams.wordOrderType,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("workOrderAdd", {
		url : "/workOrder/add",
		templateUrl : "workOrder/add.html",
		controller : 'workOrderAddAct',
		resolve : {}
	}).state("workOrderEdit", {
		url : "/workOrder/edit?id",
		templateUrl : "workOrder/edit.html",
		controller : 'workOrderEditAct',
		resolve : {
			bean : function($stateParams, workOrderService) {
				return workOrderService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	});
})
