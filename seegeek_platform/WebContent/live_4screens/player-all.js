//VT Player V4.0
var nextplay="";
var Player;
var vwidth=640;
var vheight=480;
var BasePlay = new Base64();

//是否支持flash
function detectFlash() {
        //navigator.mimeTypes是MIME类型，包含插件信息
     if(navigator.mimeTypes.length>0){
     //application/x-shockwave-flash是flash插件的名字
         var flashAct = navigator.mimeTypes["application/x-shockwave-flash"];
         return flashAct != null ? flashAct.enabledPlugin!=null : false;
     } else if(self.ActiveXObject) {
         try {
             new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
             return true;
         } catch (oError) {
             return false;
         }
     }
 }
 
function checkhHtml5()   
{   
	//alert("checkhHtml5"+typeof(Worker));
	if(typeof(Worker) == "object")
	{   
		return true;  
	} else   
	{   
		return false; 
	}  
}
			
/* 
* 智能机浏览器版本信息: 
* 
*/
var browser={ 
	versions:function(){  
		var u = navigator.userAgent, app = navigator.appVersion; 
		//	alert(u+" "+app);	
		var browserName=navigator.userAgent.toLowerCase(); 
		return {//移动终端浏览器版本信息  
		            //IE内核 
						trident: /msie/i.test(browserName) && !/opera/.test(browserName), //IE内核
		        		presto: u.indexOf('Presto') > -1, //opera内核 
		        		webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
			            mac: u.indexOf('Macintosh') > -1, //mac 
		        		gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核 
							      //是否为移动终端
						mobile: !!u.match(/AppleWebKit.*Mobile.*/),
		        		ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
		        		android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器 
		        		iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器 
		        		iPad: u.indexOf('iPad') > -1, //是否iPad 
		        		webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部 
		        		Windows: u.indexOf('Windows') > -1 //是否Windows
		    }; 
	 }(), 
	 language:(navigator.browserLanguage || navigator.language).toLowerCase() ,
	 ua:navigator.userAgent.toLowerCase()
};
			

function playnext() { //下一页面1
//alert("播放下一个");
if (nextplay.lenght>0)
	location.replace(nextplay);
}        

function getmd5(){ //注册码
var lic = ""; //new Array("71b05747cfe649eecb77a448af4b5a34","ebcecc41af34e2ff4599e0318d54b89e","cfd4bcfde15c894b6174cd313de5daeb");	        	return lic;
}

function doHTML5(url){
	document.write('<div class="video_control"  id="p_player">');
	document.write('<video id="video" width="'+vwidth+'" height="'+vheight+'" controls="controls" autoplay="true" src="'+url+'"/>');
	document.write('<button onclick="document.getElementById('video').play()">Play</button>');
  	document.write('<button onclick="document.getElementById('demo').pause()">Pause</button>');
  	document.write('<button onclick="document.getElementById('demo').volume+=0.1">Volume Up</button>');
  	document.write('<button onclick="document.getElementById('demo').volume-=0.1">Volume Down</button>');
	document.write('</div>');	      
}

function DoAndroidPlay(play_info){
//判断android版本 
	//var ua = navigator.userAgent.toLowerCase();
	if(browser.ua.indexOf("android 4")>0) 
	{
			//doHTML5(BasePlay.decode(play_info.src));
			doHTML5(play_info.src);
    }else
    {
    		//location.replace(BasePlay.decode(play_info.surl));
    		location.replace(play_info.surl);
    }

}

function DoMacPlay(play_info){

	if (checkhHtml5()&&!detectFlash()){
		doHTML5(play_info.src);
	}else{
		DoFlashPlay(play_info);
	}
}	      

function DoiOSPlay(play_info){

	//doHTML5(BasePlay.decode(play_info.src));
	doHTML5(play_info.src);
}	      

function DoFlashPlay(play_info){

	nextplay=video_info.next;
    // For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. 
    var swfVersionStr = "11.1.0";
    // To use express install, set to playerProductInstall.swf, otherwise the empty string. 
    var xiSwfUrlStr = "playerProductInstall.swf";

    var flashvars = {};
    flashvars.src=video_info.src; //BasePlay.decode(video_info.src);
	flashvars.autoPlay=video_info.autoPlay;
    flashvars.streamType = "dvr"; //recorded, live, dvr
    flashvars.controlBarAutoHide = "true";
    flashvars.tintColor="FF0123"; //"1A679B";
    flashvars.scaleMode="letterbox"; //"none"; //"stretch"; //"letterbox"; 
    flashvars.javascriptCallbackFunction = 'onJSBridge';

    //Width = video_info.width;
	//Height = video_info.height;
    
    onJSBridge = function(playerId, event, obj) 
    {
		switch (event) 
		{
		    case 'onJavaScriptBridgeCreated':			
	    		
				Player = document.getElementById(playerId);
					
				//if (!Player.getCanLoad()) location.replace("http://www.visiontops.com");	   

		        //Player = $("#" + playerId)[0];
			        
		        Player.addEventListener('isDynamicStreamChange', 'onDynamicStream');
		        Player.addEventListener('switchingChange', 'onDynamicStream');
		        Player.addEventListener('autoSwitchChange', 'onDynamicStream');
		        Player.addEventListener('mediaError', 'onMediaError');
		        Player.addEventListener("bufferingChange", "onBufferingChange");
		        $(window).resize(playerResize);
		        return setTimeout(playerResize, 10);
		}
	};
				
	onDynamicStream = function() 
	{
		return setTimeout(updateDynamicStreamItems, 10);
	};

//前置广告图片												
	var adparameters=
	{
			id: "showImageAdCenterBottom"
		    , url: "preview.jpg"
		    , layoutInfo: {
		        width: vwidth
		        , height: vheight
		        , horizontalAlign: "center"
		        , verticalAlign: "middle"
		        //, bottom: (vheight-360)/2
		    }
		    , clickUrl: "http://www.visiontops.com"
		    , pauseMainMediaOnClick: true
		    , autoCloseAfter:5
		    , closable: true
			, hideScrubBarWhilePlayingAd: true
			, pauseMainMediaWhilePlayingAd: true
			, resumePlaybackAfterAd: true
	}
								 
    var prerollShown = false;				
            
    onBufferingChange = function(buffering) 
    {
		if (buffering && !prerollShown) {
			prerollShown = true;
    
			Player.displayAd(adparameters);
			//Player.closeAd(adparameters.id);
		}
	}

	onMediaError = function(code, message, detail, playerId){
		if (!prerollShown) {
			prerollShown = true;
    
			Player.displayAd(adparameters);
			//Player.closeAd(adparameters.id);
		}
		//Player.load();
		//return setTimeout(mediaError, 10, code, message, detail);
	}

    var params = {};
    params.quality = "high";
    params.bgcolor = "#ffffff";
    params.allowscriptaccess = "sameDomain";
    params.allowfullscreen = "true";
    var attributes = {};
    attributes.id = "Main";
    attributes.name = "Main";
    attributes.align = "middle";
    swfobject.embedSWF(
        "Main.swf", "flashContent", 
        vwidth,vheight,
        swfVersionStr, xiSwfUrlStr, 
        flashvars, params, attributes);
    // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
    swfobject.createCSS("#flashContent", "display:block;text-align:left;");

}
//function play(url,pic,next,pic2,vwidth,vheight,rtspurl)
function play(video_info)	
{

		//Base 64

		//var str = BasePlay.encode("http://msvod.chinacache.com:1938/live/home.smil/playlist.m3u8");
		//alert("base64 encode:" + str);
		//str = BasePlay.decode(str);
		//alert("base64 decode:" + str);
		vwidth = video_info.width;
		vheight = video_info.height;
		
		//判断浏览器和终端
	  		
		if (browser.versions.ios){	//iPhone or iPad
		
			alert("iOS:iPhone or iPad");
	    	DoiOSPlay(video_info);
		} else 
		{ 
			if (browser.versions.android){ //Android 终端
				alert("android");
				DoAndroidPlay(video_info);
			}else{
				if (browser.versions.mac){ //MAC				
					alert("MAC");
					DoMacPlay(video_info);
				}
				else{ //需要flash支持
					alert("Flash");
					DoFlashPlay(video_info);
				}
			}				
		}
}	      



