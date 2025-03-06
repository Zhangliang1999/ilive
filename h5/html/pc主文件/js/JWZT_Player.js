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
	function SetVolume(vol)
	{
		var flashObject = getflashObject();
		flashObject.setVolume(vol);
	}
