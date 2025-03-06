myapp.controller("paymentAddAct", function($scope, $rootScope, $state, paymentService, packageService, agentService,
		operationUserService) {
	$scope.bean = {
		channelType : 1,
	};
	$scope.packageInfo = {};
	$scope.agentInfo = {};
	$scope.paymentTypeBeans = paymentTypeBeans;
	$scope.paymentAutoBeans = paymentAutoBeans;
	$scope.paymentWayBeans = paymentWayBeans;

	agentService.list({
		status : 2
	}).then(function(response) {
		if (response.code == 1) {
			$scope.agentList = response.data
		}
	}, function(error) {
		$scope.error = error;
	});
	operationUserService.list({}).then(function(response) {
		if (response.code == 1) {
			$scope.userList = response.data
		}
	}, function(error) {
		$scope.error = error;
	});
	$scope.$watch("userInfo", function(newValue, oldValue, scope) {
		if ($scope.userInfo) {
			$scope.bean.sellUserId = $scope.userInfo.userId;
		}
	}, true);
	$scope.$watch("agentInfo", function(newValue, oldValue, scope) {
		if ($scope.agentInfo) {
			if ($scope.bean.agentDeductionRate && $scope.bean.agentDeductionRate > 0) {
				$scope.bean.agentId = $scope.agentInfo.id;
				$scope.bean.agentDeductionRate = $scope.agentInfo.deductionRate;
			}
		}
	}, true);
	$scope.$watch("packageInfo", function(newValue, oldValue, scope) {
		if ($scope.packageInfo) {
			$scope.bean.packageId = $scope.packageInfo.id;
			if ($scope.bean.paymentPrice && $scope.bean.paymentPrice > 0) {
			} else {
				if ($scope.bean.channelType == 1) {
					$scope.bean.paymentPrice = $scope.packageInfo.officialPlatformPrice;
				} else if ($scope.bean.channelType == 2) {
					$scope.bean.paymentPrice = $scope.packageInfo.channelAgentPrice;
				} else if ($scope.bean.channelType == 3) {
					$scope.bean.paymentPrice = $scope.packageInfo.groupProductLibraryPrice;
				}
			}
		}
	}, true);
	$scope.$watch("bean.channelType", function(newValue, oldValue, scope) {
		if ($scope.bean.channelType) {
			packageService.list({
				channelType : $scope.bean.channelType,
				status : 2
			}).then(function(response) {
				if (response.code == 1) {
					$scope.packageList = response.data
				}
			}, function(error) {
				$scope.error = error;
			});
		}
	}, true);
    $scope.DateAdd=function(interval, number, date){
    	var sdate=new Date(date);
    	var patt1 =  /^\d{4}-([0-1]?[0-9])-([0-3]?[0-9])$/;  //判断输入的日期是否符合格式正则表达式
    	    if(!(sdate && typeof(sdate) == "string" &&  patt1.test(sdate))){
    	        sdate = new Date(); //不满足日期的则使用当前年月日
    	    }
    	   number = isNaN(parseInt(number)) ? 0 : parseInt(number);//若没有输入间隔，则使用当前日
    	   var  caret = "-";
    	    var gdate = new Date(sdate).getTime();//获取指定年月日
    	    switch (interval) {
    	        case 1: {
    	            gdate = gdate + 1000*60*60*24*number*365; //加减相差毫秒数
    	        }
    	        case 2: {
    	          gdate = gdate + 1000*60*60*24*number*30; //加减相差毫秒数
    	        }
    	        case 3: {
    	          gdate = gdate + 1000*60*60*24*number; //加减相差毫秒数
    	        }
    	        }
    	  
    	    var speDate = new Date(gdate);//获取指定好毫秒数时间
    	    var preYear = speDate.getFullYear();
    	    var preMonth = speDate.getMonth()+1;
    	    var preDay = speDate.getDate();
    	    preMonth = (preMonth < 10) ? ("0" + preMonth) :preMonth;
    	    preDay = (preDay < 10) ? ("0" + preDay) :preDay;
    	    var preDate =  preYear + caret +  preMonth + caret + preDay;
    	    console.log(preDate);
    	    return preDate+"";
    }
	$scope.saveAgent = function() {
		if (typeof ($scope.bean.enterpriseId) == "undefined" || $scope.bean.enterpriseId <= 0) {
			alert("请选择企业");
			return;
		}
		if (typeof ($scope.bean.paymentType) == "undefined" || $scope.bean.paymentType <= 0) {
			alert("请选择订购类型");
			return;
		}
		if (typeof ($scope.bean.paymentAuto) == "undefined" || $scope.bean.paymentAuto < 0) {
			alert("请选择续订类型");
			return;
		}
		if (typeof ($scope.bean.packageId) == "undefined" || $scope.bean.packageId <= 0) {
			alert("请选择套餐");
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
		if ($scope.bean.channelTypes.length > 0) {
			$scope.bean.channelTypes = $scope.bean.channelTypes.substring(0, $scope.bean.channelTypes.length - 1);
		}
		paymentService.save($scope.bean).then(function(response) {
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