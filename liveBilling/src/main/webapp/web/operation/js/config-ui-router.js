myapp.config(function($urlRouterProvider, $stateProvider) {
	$urlRouterProvider.when("", '/package/list');
	$stateProvider.state("packageList", {
		url : "/package/list?queryContent&startTime&endTime&status&packageType",
		templateUrl : "package/list.html",
		controller : 'packageListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					queryContent : $stateParams.queryContent,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					packageType : $stateParams.packageType,
					pageNo : $stateParams.pageNo
				};
			}
		}
	}).state("packageAdd", {
		url : "/package/add",
		templateUrl : "package/add.html",
		controller : 'packageAddAct',
		resolve : {}
	}).state("packageEdit", {
		url : "/package/edit?id",
		templateUrl : "package/edit.html",
		controller : 'packageEditAct',
		resolve : {
			bean : function($stateParams, packageService) {
				return packageService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("productList", {
		url : "/product/list?queryContent&startTime&endTime&status&productTypes",
		templateUrl : "product/list.html",
		controller : 'productListAct',
		resolve : {
			queryCondition : function($stateParams) {
				return {
					queryContent : $stateParams.queryContent,
					startTime : $stateParams.startTime,
					endTime : $stateParams.endTime,
					status : $stateParams.status,
					productTypes : $stateParams.productTypes,
				};
			}
		}
	}).state("productAdd", {
		url : "/product/add",
		templateUrl : "product/add.html",
		controller : 'productAddAct',
		resolve : {
			functionList : function(functionService) {
				return functionService.list().then(function(response) {
					return resolveBean(response);
				});
			}
		}
	}).state("productEdit", {
		url : "/product/edit?id",
		templateUrl : "product/edit.html",
		controller : 'productEditAct',
		resolve : {
			bean : function($stateParams, productService) {
				return productService.info($stateParams.id).then(function(response) {
					return resolveBean(response);
				});
			},
			functionList : function(functionService) {
				return functionService.list().then(function(response) {
					return resolveBean(response);
				});
			},
		}
	});
})
