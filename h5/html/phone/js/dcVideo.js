$(document).ready(function() {
	//判断手机类型
	var u = navigator.userAgent;
	var ua = window.navigator.userAgent.toLowerCase();
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	$(".videoBox").height($(".videoBox").width()*9/16);
	// var html = "<div class='videoPlay'><i class='iconfont icon-ziyuan'></i></div>";
	// $(".videoBox").append(html);
	if(isAndroid){
		var player = document.getElementById("video");
		var video = document.getElementById("video");
		//点击播放
		$('.btnPlay').on('click', function() {
			$(".videoPlay").addClass('hide');
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
                $(".videoPlay").addClass('hide');
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
				$(".videoPlay").removeClass('hide');
				$("video").css("z-index","-1");
			}
		});
		var video = document.getElementById('video');
		$(".videoPlay").on('click', function() {
            $(".videoPlay").addClass('hide');
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
                $(".videoPlay").addClass('hide');
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
                $(".videoPlay").removeClass('hide');
				$("video").css("z-index","-1");
			}
		})
		//全屏事件
		var itemHeight = window.screen.height; //获取手机高度
		var itemWidth = window.screen.width; //手机宽度
		//手机全屏点击
		$('#fullScreenBtn').on('click', function(event) {
			event.stopPropagation();
			event.preventDefault();
			var aaa = $(this).attr("aaa");
			if(aaa == 0) {
				henping();
				$('#fullScreenBtn').attr('aaa', '1');
			} else {
				shuping();
				$('#fullScreenBtn').attr('aaa', '0');
			}
		});
		 if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			 player.addEventListener('x5videoenterfullscreen',function(){
					//设为屏幕尺寸
					var width = window.screen.width;
					var height = window.screen.height;
					if (width > height) {
						width = [height, height = width][0];
					}
					player.style.width = width + 'px';
					player.style.height = height + 'px';
					//在body下添加样式类以控制全屏下的展示
					document.body.classList.add('fullscreen');
					player.style["object-position"] = "0px 0px"
					getHeight()
					shuping()
				});
				player.addEventListener('x5videoexitfullscreen',function(){
					document.body.classList.remove('fullscreen');
					player.style["object-position"] = "0px 0px"
					$('.btnPlay').removeClass('icon-zanting').addClass("icon-play");
					getHeight()
					shuping();
					$("body").css({"width":itemWidth,"height":itemHeight,"overflow":"hidden"});
				},false);
				player.addEventListener('ended', function(e){
					player.pause();
					$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
				}, false);
		    }
		
	}else{
		$(".videoBtn").addClass('hide');
		$("#video").attr("controls","controls");
		//$("#video").attr("poster","http://pic.zb.tv189.com/img/livebg_default.png");
		var video = document.getElementById('video');
		$(".videoPlay").on('click', function() {
            if ($(".videoPlay").find('i').hasClass('hide')) return false;
            //$(".videoPlay").addClass('hide');
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
                $(".videoPlay").addClass('hide');
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
                $(".videoPlay").removeClass('hide');
				$("video").css("z-index","-1");
			}
		})
	}

	$(window).resize(function(){
		getHeight();
	});
	//横屏时方法
	function henping(){
		//$("body").css({"width":itemHeight,"height":itemWidth,"overflow":"hidden"});
		$('.videoBox').css({
			'-webkit-transform': 'rotate(90deg)',
			'width': itemHeight,
			'height': itemWidth,
			'background':"#FFF!important",
			'overflow':'hidden',
			'position': 'fixed' // eidt by caibin
		});
		$('video').css({
			"width":itemHeight,
			"height":itemWidth,
			"z-index":-1, //edit by caibin
			"object-position": "center center"
		});
		$(".videoBtn").css("width","90%"); //edit by caibin
		$(".firstBox").css("z-index","-1");
		$(".videoBox").offset({
			top: 0,
			left: 0,
		});
		$(".playBox").css("margin-left","1rem");
		$(".expandBox").css("margin-right","0.8rem");
	};
	//竖屏时方法
	function shuping(){
		$("body").css({"width":itemWidth,"height":itemHeight,"overflow":"hidden"});
		var hei = $(window).width()*9/16;
		var hei1 = $(".videoBtn").height();
		$('.videoBox').css({
			'-webkit-transform': 'rotate(0deg)',
			'width': "100%",
			'height': hei+hei1,
			'overflow':'hidden',
			'position':'relative',
			'top':'0',
			'left':'0'
		});
		$('video').css({
			"width":itemWidth,
			"height":itemHeight,
			"object-position": "center top"
		});
		$(".videoBtn").css("width", "100%"); //add by caibin
		$(".firstBox").css("z-index","0");
		$(".mainBox").css({
			"z-index": 9999
		})
		$(".playBox").css("margin-left","0.3rem");
		$(".expandBox").css("margin-right","0");
	};
	//高度改变时高度
	function getHeight(){
		var winHeight = $(window).height();
		$(".videoBox").height($(window).width()*9/16);
		var VideoBox = $(".videoBox").height();
		var menuBox = $(".menuBox").height();
		var talkCon = $(".newtalkCon").height();
		var replyHeader = $(".replyHeader").height();
		var drawTit = $(".drawTitle").height()+1;
		var voteButton = $(".voteendButton").height();
		var voteConBtn = $(".voteconButton").height();
		$(".contentBox").css("height", winHeight - VideoBox - menuBox - 2);
		$(".livemask").css("height", winHeight - VideoBox - menuBox - 2);
		$(".giftMask").css("height", winHeight - VideoBox - menuBox - 2);
		$(".rewardMask").css("height", winHeight - VideoBox - 2);
		$(".giftmask").css("height", winHeight);
		$(".passMask").css("height",winHeight);
		$(".contentBox>ul>li").css("height", winHeight - VideoBox - menuBox);
		$(".replyBox").css("height",winHeight - VideoBox - menuBox);
		$(".replyCon").css("height",winHeight - VideoBox - menuBox-replyHeader);
		$(".talkLi").css("height", winHeight - VideoBox - menuBox - talkCon);
		$(".notheme").css("height", winHeight - VideoBox - menuBox);
		$(".videoMask").css("height", winHeight);
		$(".drawMask").css("height", winHeight - VideoBox);
		$(".drawContent").css("height",winHeight-VideoBox-drawTit);
		$(".voteMask").css("height", winHeight - VideoBox);
		$(".voteContent").css("height",winHeight-VideoBox-drawTit);
		$(".rewardContent").css("height",winHeight-VideoBox-drawTit);
		$(".voteResult").css("height",winHeight-VideoBox-drawTit-voteButton);
		$(".voteMain").css("height",winHeight-VideoBox-drawTit-voteConBtn);
		$(".downloadMask").css("height", winHeight - VideoBox);
		$(".downloadContent").css("height",winHeight-VideoBox-drawTit);
		$(".downloadeResult").css("height",winHeight-VideoBox-drawTit-voteButton);
		$(".downloadMain").css("height",winHeight-VideoBox-drawTit-voteConBtn);
		$(".ewmMask").css("height",winHeight);
	}
	//判断手机横竖屏状态：
	function orient() {
		if (window.orientation == 90 || window.orientation == -90) {
		//ipad、iphone竖屏；Andriod横屏
			$("body").attr("class", "landscape");
			orientation = 'landscape';
			getHeight();
			return false;
		}
		else if (window.orientation == 0 || window.orientation == 180) {
		//ipad、iphone横屏；Andriod竖屏
					$("body").attr("class", "portrait");
					orientation = 'portrait';
					getHeight();
					return false;
				}
			}

		//用户变化屏幕方向时调用
			$(window).bind( 'orientationchange', function(e){
				orient();
			});
});
//$("video").click(function(){
//$(".videoPlay").fadeIn();
//	setTimeout(function(){
//		$(".videoPlay").fadeOut();
//	},1000)
//});
