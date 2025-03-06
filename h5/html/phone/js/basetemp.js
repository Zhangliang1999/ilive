/**
 * 
 */

function tempJumpP(){
	var href = window.location.href;
	var arr = href.split("?");
	if(arr.length != 1){
		var str = arr[1].split("&");
		for(var i=0;i<str.length;i++){
			var rtemp = str[i].split("=");
			if(rtemp[0] == "roomId"){
				console.log("直播间id为： "+rtemp[1]+"   *****************");
				if(rtemp[1]==tempRoomId){
					var newUrl = window.location.href = "http://" + h5Base + "/phone/liveT.html";//Phone
					if (!/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
						newUrl = "http://" + h5Base + "/pc/playT.html";//PC
				    }
					window.location.href = newUrl;
				}
			}
		}
	}
}
//直播间名称
var tempRoomName = "2018杭州马拉松奥体中心无人机画面";
//直播间观看人数
var TempRoomNumber = "100";
//开播时间
var tempStartTime = "2018-11-02 10:10:00";
//PC端推流地址
var tempPushStreamAddr = "http://livehls01.zb.nty.tv189.com/live/live2465/5000k/tzwj_video.m3u8";
//h5视频地址
var tempVideoAddr = "http://livehls01.zb.nty.tv189.com/live/live2465/5000k/tzwj_video.m3u8";

var tempRoomId = 1264;
tempJumpP();