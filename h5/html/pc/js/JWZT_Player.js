//播放器默认状态控制
var _playAuto = _playAuto || '';
    if(_playAuto == 'false'){_playAuto = 'false';}
var ifShiftMove = ifShiftMove || 'false';
    if(ifShiftMove == 'false'){ifShiftMove = 'false';}
var  ifAuthCDN_WS=  ifAuthCDN_WS || 'false';
    if(ifAuthCDN_WS == 'false'){ifAuthCDN_WS = 'false';}
var ifShowTime = ifShowTime || '';
    if(ifShowTime == 'false'){ifShowTime = 'false';}
var ifShowPoopBtn = ifShowPoopBtn || '';
    if(ifShowPoopBtn == 'false'){ifShowPoopBtn = 'false';}
var ifShowImageBtn = ifShowImageBtn || '';
    if(ifShowImageBtn == 'false'){ifShowImageBtn = 'false';}
var ifShowSetBtn = ifShowSetBtn || '';
    if(ifShowSetBtn == 'false'){ifShowSetBtn = 'false';}

var ifShowStream = ifShowStream || '';
    if(ifShowStream == 'false'){ifShowStream = 'false';}

var  PlayVideoAspect=  PlayVideoAspect || 'mc43'; //mc43 diaplasis（按比例） mc169 bespread(铺满)


var bufferLongTime=bufferLongTime||'';

//var streamType=streamType||'super'; //low,high,super
var streamType=streamType;
var function_lamp_method=function_lamp_method || '';   //method(obj); off,on


var small_video_window=small_video_window||'true';

var function_stop_method=function_stop_method||'';

var ifForbitSeek=ifForbitSeek||'';

var interfaceType=interfaceType||'false';

var shareStatu=shareStatu||''; //值'1'第一个面板显示。值'2'第二个面板显示。值'3'全部显示。若无此属性或值为'',则分享面板隐藏.

var ifShowMenuItem=ifShowMenuItem||'true';//右键菜单项里是否显示"经纬中天"选项,
var ifShowLogo=ifShowLogo||'true';
var shareSwfUrl=shareSwfUrl||'';
var shareHtmlUrl=shareHtmlUrl||'';
var authUrl=authUrl||'false';
var interfaceHttp = interfaceHttp || '';
var _playerFile = _playerFile || 'js/player.swf';
var AddressSeparator =AddressSeparator || '';  //地址分隔符 由于接口地址中具有&的符号在传递的过程中不能获取
var playerName = playerName ||'JwztPlayer',
	swfUrl = _playerFile + '?r='+ Math.round(Math.random()*100);
//_playAuto='false';
var AudioPlayerType=AudioPlayerType||'0';//0:特效,1,柱状，2波形
var AVPlayerType=AVPlayerType||'0';//0:自动判定,1,视频，2音频


function shareFunc(id){
    alert(id);
}

function getflashPlayStatus(swfName)
{
	var flashobj;
	var playStatus = "";
	try
	{
		swfName=swfName ||'JwztPlayer';
		if (navigator.appName.indexOf("Microsoft") > -1)
	 	{
	     	   flashobj = window[swfName];
	  	}
	  	else
	  	{
		    	flashobj = document[swfName];
	  	}
		//alert(flashobj);
	  	if(flashobj!=null)
	  	{
			try
			{
					playStatus=flashobj.FlvPLayStatue();
			}
			catch (e)
			{
				//2012/11/23alert(e);
				window.status=e;
			}


	  	}else
		{	window.status = playStatus;
		}

  	}catch(e)
  	{}
  	return playStatus;
}
function getflashObject()
{
	var flashobj;
	try
	{
		var swfName='JwztPlayer';

		if (navigator.appName.indexOf("Microsoft") > -1)
	 	{
	     	   flashobj = window[swfName];
	  	}
	  	else
	  	{
		    	flashobj = document[swfName];
	  	}

  	}catch(e)
  	{

	}
  	return flashobj;
}
var ifReturnStop=false;
function nextPlayer()
{

	try
	{
		//alert(333)
		var status = getflashPlayStatus();
		window.status = status;
		if(status=="stop"&&!ifReturnStop)
		{
			try
			{

				var bRetun = nextPlayerPage();
				if(bReturn=true)
				{
					ifReturnStop=true;
				}else
				{
						ifReturnStop=true;
				}

			}catch(e)
			{
				ifReturnStop=true;
			}

		}else if(status!="stop")
		{
				ifReturnStop=false;
		}
	}catch(e)
	{}
}
function doTime()
{
	setInterval("nextPlayer()",1000);
}
function loadVideo(fileId)
{
	interfaceHttp="http://192.168.1.112:9600/3600.mp4";
	loadJwztPlayer();
}

function loadJwztPlayer()
{
	flashvars ="interfaceHttp="+interfaceHttp+"&AddressSeparator="+AddressSeparator+"&ifShiftMove="+ifShiftMove+"&ifShowTime="+ifShowTime+"&ifShowPoopBtn="+ifShowPoopBtn+"&ifShowImageBtn="+ifShowImageBtn+"&ifShowSetBtn="+ifShowSetBtn+"&playAuto="+_playAuto+"&ifShowStreamBtn="+ifShowStream+"&bufferLongTime="+bufferLongTime+"&streamType="+streamType+"&function_lamp_method="+function_lamp_method+"&function_share_method="+function_share_method+"&small_video_window="+small_video_window;

	swfHtml = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="100%" height="100%" id="' + playerName + '" align="middle">\
		<param name="movie" value="' + swfUrl + '" />\
		<param name="quality" value="high" />\
		<param name="bgcolor" value="#000000" />\
		<param name="devicefont" value="false" />\
		<param name="wmode" value="opaque" />\
		<param name="allowScriptAccess" value="always" />\
		<param name="allowFullscreen" value="true" />\
		<param name="allownetworking" value="all" />\
		<param name="salign" value="lt" />\
		<param name="flashvars" value="' + flashvars + '"  />\
			<a href="http://www.adobe.com/go/getflash">\
				<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="获得 Adobe Flash Player" />\
			</a>\
	</object>';
	if(navigator.plugins&&navigator.mimeTypes&&navigator.mimeTypes.length){
	swfHtml = '<embed   src="' + swfUrl + '" id="' + playerName + '" name="' + playerName + '" type="application/x-shockwave-flash" data="' + swfUrl + '"  quality="high" allowScriptAccess="always" redirectUrl="http://www.adobe.com/shockwave/download/download.cgi?" swLiveConnect="true" menu="false" allowFullScreen="true" salign="lt"  width="100%" height="100%" flashvars="' + flashvars + '">\
		</embed>';
	}
	document.getElementById(_id).innerHTML=swfHtml;

}
function loadLiveVideo(liveId)
{
	loadJwztPlayer();
}
var playIndex = 1;
function playUrl(index,url)
{
	playIndex=index;
	interfaceHttp=url;
	loadJwztPlayer();
}

function UseChangeBitrateMode(bitRateMode)
{
	//alert(bitRateMode);
}
function nextPlayerPage()
{

	playIndex = playIndex+1;
	$("#play_"+playIndex).click();
}
//function setJwztUrl()
//{

	//var flashObject = getflashObject();
        //alert(flashObject);
	//flashObject.setNewAddr("http://tvzone.wasu.cn:8000//music//2012/09/27/1_13487286507KUU.mp3");
//}
doTime();

function IEVersion() {
    var userAgent = navigator.userAgent;
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1;
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE;
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if(isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if(fIEVersion == 7) {
            return true;
        } else if(fIEVersion == 8) {
            return true;
        } else if(fIEVersion == 9) {
            return true;
        } else if(fIEVersion == 10) {
            return true;
        } else {
            return true;
        }
    } else if(isEdge) {
        return true;
    } else if(isIE11) {
        return true;
    }else{
        return false;
    }
}

function flashChecker() {
    var hasFlash = 0;　　　　 //是否安装了flash
    var flashVersion = 0;　　 //flash版本

    if(document.all) {
        var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        if(swf) {
            hasFlash = 1;
            VSwf = swf.GetVariable("$version");
            flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
        }
    } else {
        if(navigator.plugins && navigator.plugins.length > 0) {
            var swf = navigator.plugins["Shockwave Flash"];
            if(swf) {
                hasFlash = 1;
                var words = swf.description.split(" ");
                for(var i = 0; i < words.length; ++i) {
                    if(isNaN(parseInt(words[i]))) continue;
                    flashVersion = parseInt(words[i]);
                }
            }
        }
    }
    return { f: hasFlash, v: flashVersion };
}
if (IEVersion()){
    var fls = flashChecker();
    if(!fls.f) {
    	alert('请启用Adobe Flash Player')
        //$('#noflash').css('display','block')
    }
}


