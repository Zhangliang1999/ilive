var myapp = angular.module("myapp", [ 'ui.router', "ui.router.state.events", 'ngFileUpload', 'ngCookies' ])
myapp.run(function($rootScope, $state, $stateParams, $interval, $sce, $timeout, frameService) {
	$rootScope.imanagerHomeUrl = imanagerHomeUrl;
	function GetQueryString(name) {
		var url = location.href; // 获取url中"?"符后的字串
		var theRequest = new Object();
		if (url.indexOf("?") != -1) {
			var str = url.substr(url.indexOf("?") + 1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		}
		return theRequest[name];
	}
	frameService.getTop(GetQueryString("auth")).then(function(response) {
		console.log(response);
		if (response.code == 1) {
			$rootScope.currentUser = response.data.currentUser;
			if ($rootScope.currentUser.id == -10000) {
				$rootScope.thirdModel = true;
			} else {
				$rootScope.thirdModel = false;
			}
		} else if (response.code == 503) {
			window.location.href = loginUrl;
		} else {
			$rootScope.thirdModel = true;
		}
	}, function(error) {
	});
	// 系统初始化
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	$rootScope.first = true;
	$rootScope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {
		$rootScope.previousState_name = fromState.name;
		$rootScope.previousState_params = fromParams;
		$('html,body').animate({
			scrollTop : '0px'
		});
	});
	$rootScope.$on("$viewContentLoaded", function(event) {
		var winH = $(window).height() - 72;
		if($rootScope.thirdModel){
			winH = $(window).height();
		}
		$("#rightWrapper").slimScroll({
			height : winH,
			alwaysVisible : true,
		});
	});
	$rootScope.$on("$stateChangeStart", function(event, toState, toParams, fromState, fromParams) {
		var heightWithoutNavbar = $("body > #wrapper").height() - 70;
		$('nav.navbar-default').css("height", "calc(100% - 70px)");
		var navbarHeigh = $('nav.navbar-default').height();
		var wrapperHeigh = $('#page-wrapper').height();
		if (navbarHeigh > wrapperHeigh) {
			$rootScope.pageWrapperStyle = {
				"min-height" : navbarHeigh + "px"
			};
		}
		if (navbarHeigh < wrapperHeigh) {
			$rootScope.pageWrapperStyle = {
				"height" : $(window).height() - 70,
				"min-height" : $(window).height() - 70
			};
		}
	});
	$rootScope.returnPrevious = function() {
		if (isBlank($rootScope.previousState_name)) {
			history.go(-1);
		}
		$state.go($rootScope.previousState_name, $rootScope.previousState_params);
	};

	$rootScope.trustSrc = function(url) {
		return $sce.trustAsResourceUrl(url);
	}

	$rootScope.checkPermissions = function(urls) {
		if ($rootScope.currentUser && urls) {
			if ($rootScope.currentUser.superAdmin) {
				return true;
			}
			var permissions = $rootScope.currentUser.permissions;
			for (i = 0; i < urls.length; i++) {
				var url = urls[i];
				for (j = 0; j < permissions.length; j++) {
					var uris = permissions[j].uri.split(",");
					for (l = 0; l < uris.length; l++) {
						if (url.startWith(uris[l])) {
							return true;
						}
					}
				}
			}
		}
		return false;
	};

	function banBackSpace(e) {
		var ev = e || window.event;// 获取event对象
		var obj = ev.target || ev.srcElement;// 获取事件源
		var t = obj.type || obj.getAttribute('type');// 获取事件源类型
		// 获取作为判断条件的事件类型
		var vReadOnly = obj.getAttribute('readonly');
		// 处理null值情况
		vReadOnly = (vReadOnly == "") ? false : vReadOnly;
		// 当敲Backspace键时，事件源类型为密码或单行、多行文本的，
		// 并且readonly属性为true或enabled属性为false的，则退格键失效
		var flag1 = (ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea") && vReadOnly == "readonly") ? true
				: false;
		// 当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
		var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true : false;

		// 判断
		if (flag2) {
			return false;
		}
		if (flag1) {
			return false;
		}
	}
	// 禁止后退键 作用于Firefox、Opera
	document.onkeypress = banBackSpace;
	// 禁止后退键 作用于IE、Chrome
	document.onkeydown = banBackSpace;
});
