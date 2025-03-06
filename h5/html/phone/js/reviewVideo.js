$(document).ready(function() {

	//判断手机类型
	var u = navigator.userAgent;
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
			$(".videoPlay").hide();
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
				$(".videoPlay").hide();
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
				$(".videoPlay").show();
				$("video").css("z-index","-1");
			}
		});
		var video = document.getElementById('video');
		$(".videoPlay").on('click', function() {
			$(".videoPlay").hide();
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
				$(".videoPlay").hide();
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
				$(".videoPlay").show();
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
			var aaa = $(this).attr("fullScreen");
			if(aaa == 0) {
				henping();
				$('#fullScreenBtn').attr('fullScreen', '1');
			} else {
				shuping();
				$('#fullScreenBtn').attr('fullScreen', '0');
			}
		});
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
	}else{
		$(".videoBtn").hide();
		$("#video").attr("controls","controls");
		$("#video").attr("poster","http://pic.zb.tv189.com/img/livebg_default.png");
		var video = document.getElementById('video');
		$(".videoPlay").on('click', function() {
			$(".videoPlay").hide();
			if(video.paused) {
				video.play();
				$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
				$(".videoPlay").hide();
				$("video").css("z-index","0");
			} else {
				video.pause();
				$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
				$(".videoPlay").show();
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
//				$(".drawList").css("height",winHeight-VideoBox-drawTit);
		$("body").height(winHeight);
		$("body").css("overflow","hidden");
	};
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

	function formattime( second_time ){

		var time = "00:00";
		if(!isNaN(second_time)){
			second_time = parseInt(second_time);
			var s = second_time%60;
			var m = parseInt(second_time/60);

			if(s<10){
				s = "0"+String(s);
			}
			time = String(m) + ":" + (s);
		}

		return time;
	}

//自定义样式
	function timeStamp( second_time ){
		var time1 = ((parseInt(second_time)/100)).toString();
		var time2 = time1.split(".")[1]? (time1.split(".")[1].length == 1 ? time1.split(".")[1]+'0' : time1.split(".")[1]) :'00';
		var time =00 + ':' + time2;
		if( parseInt(second_time )> 60){
			var second = ((parseInt(second_time) % 60)/100).toString();
			var min = (parseInt(second_time / 60)/100).toString();
			var time3 = second.split(".")[1]? (second.split(".")[1].length == 1 ? second.split(".")[1]+'0' : second.split(".")[1]) :'00';
			var time4 = min.split(".")[1]? (min.split(".")[1].length == 1 ? min.split(".")[1]+'0' : min.split(".")[1]) :'00';
			time = time4 + ":" + time3;
		}
		return time;
	}

	var video2 = $("#video");
	var isUpdateOrogressBar = 1;
	//视频总时间
	video2.on('loadedmetadata', function() {
		var video = document.getElementById("video");
		var time = video.duration;
		var allTime = formattime(time);
		//$('.allTime').html(allTime);
	});

	//视频进度时间
	video2.on('timeupdate', function() {
		if(true){

		var time = video.currentTime,alltime = video.duration;
		var percentage = 100 *(time / alltime);
		$('.progressTime').text(formattime(time));
		$('.progressBar').css({'width':percentage+ '%'});
		$('.progressBox .imgBox').css({'margin-left':percentage-1 +'%'});
		if(time == alltime){
			$('.btnPlay').removeClass('icon-zanting').addClass("icon-play"); //如果播放时间到总时间播放按钮图片换成停止图片
		}
		var timea = video.duration;
                var allTime = formattime(timea);
		$('.allTime').html(allTime);

		}

	});
	//拖拉函数
	function changeBar(item){
		var progress = $('.progressBox');
		var maxduration = video.duration; //视频总时间
		var barIcon = $('#fullScreenBtn').attr('fullScreen'),position,percentage;
		if(barIcon == 0){
			position = item - progress.offset().left; //横屏模式
		}else{
			position = item - progress.offset().top; //全屏竖屏模式
		}
		percentage = 100 * (position / progress.width());
		if(percentage > 100) {
			percentage = 100;
		}
		if(percentage < 0) {
			percentage = 0;
		}
		$('.progressBar').css('width', percentage+'%');
		$('.progressBox .imgBox').css({'margin-left':percentage +'%'});
		video.currentTime = maxduration * (percentage / 100);//视频进度时间传给当前时间
		//video.play();
		$('.btnPlay').removeClass('icon-play').addClass("icon-zanting")
	}
	//视频拖拉按钮事件
	var progressBox = document.getElementById('progressBox_special');
	progressBox.addEventListener('touchstart', progressBox_item, false);
	function progressBox_item(e){
		console.log(1232)
		var barIcon = $('#fullScreenBtn').attr('fullScreen');
		var point = fristPoint(e);
		if(barIcon == 0){
			changeBar(point.pageX);
		}else{
			changeBar(point.pageY);
		}

	};
	//拖拉按钮
	var statePic = document.getElementById('progressImg');
	statePic.addEventListener('touchmove', itemMove, false);//发现h5的touchmove行为要通过addEventListener注册
	function itemMove(e){
		console.log(123)
		e.stopPropagation();//阻止默认行为
		var barIcon = $('#fullScreenBtn').attr('fullScreen');
		var point = fristPoint(e);
		if(barIcon == 0){
			changeBar(point.pageX);//横屏
		}else{
			changeBar(point.pageY); //全屏竖屏
		}
		//video.play();
		isUpdateProgressBar=0;

	};

	statePic.addEventListener('touchend',itemTouchEnd,false);
	function itemTouchEnd(e){
		e.stopPropagation();
		video.play();
		isUpdateProgressBar=1;
	};
	//第一个手指为准
	function fristPoint(e){
		return e.touches ? e.touches[0] : e;
	}

});
