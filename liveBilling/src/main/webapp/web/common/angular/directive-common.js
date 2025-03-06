/**
 * Created by renquan on 2017/3/28.
 */
myapp.directive("navbarMinimalize", function($timeout) {
	return {
		restrict : 'C',
		link : function(scope, element) {
			$timeout(function() {
				element.click(function() {
					$("body").toggleClass("mini-navbar");
					SmoothlyMenu();
				});
			});
		}
	};
});

function SmoothlyMenu() {
	if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
		// Hide menu in order to smoothly turn on when maximize menu
		$('#side-menu').hide();
		// For smoothly turn on menu
		setTimeout(function() {
			$('#side-menu').fadeIn(400);
		}, 200);
	} else if ($('body').hasClass('fixed-sidebar')) {
		$('#side-menu').hide();
		setTimeout(function() {
			$('#side-menu').fadeIn(400);
		}, 100);
	} else {
		// Remove all inline style from jquery fadeIn function to reset menu
		// state
		$('#side-menu').removeAttr('style');
	}
}
myapp.directive("metismenu", function($timeout) {
	return {
		restrict : 'C',
		link : function(scope, element) {
			$timeout(function() {
				element.metisMenu();
				element.find("li .active").parent().addClass("in").parent().addClass("active");
			}, 200);
		}
	};
});
myapp.directive("fullHeightScroll", function($timeout) {
	return {
		restrict : 'C',
		link : function(scope, element) {
			$timeout(function() {
				element.slimscroll({
					height : '100%',
					railVisible : true
				})
			}, 200);
		}
	};
});
myapp.directive("errSrc", function() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			element.bind('error', function() {
				if (attrs.src != attrs.errSrc) {
					attrs.$set('src', attrs.errSrc);
				}
			});
		}
	}
});
myapp.directive("audioPlayer", function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			$timeout(function() {
				element.audioPlayer();
			}, 200);
		}
	}
});