//VT Player V4.0
				var nextplay="";
				var Player;
				var vwidth=640;
				var vheight=480;
			//�Ƿ�֧��flash
				function detectFlash() {
				         //navigator.mimeTypes��MIME���ͣ����������Ϣ
				     if(navigator.mimeTypes.length>0){
				     //application/x-shockwave-flash��flash���������
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
					if (typeof(Worker) !== "undefined")   
					{   
						return true;  
					} else   
					{   
						return false; 
					}  
				}
			
				/* 
				* ���ܻ�������汾��Ϣ: 
				* 
				*/
				  var browser={ 
				    		versions:function(){  
				        var u = navigator.userAgent, app = navigator.appVersion; 
			          var browserName=navigator.userAgent.toLowerCase(); 
				        return {//�ƶ��ն�������汾��Ϣ  
				               //IE�ں� 
								trident: /msie/i.test(browserName) && !/opera/.test(browserName), //IE�ں�
				                		presto: u.indexOf('Presto') > -1, //opera�ں� 
				                		webKit: u.indexOf('AppleWebKit') > -1, //ƻ�����ȸ��ں� 
				                		gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //����ں� 
											//�Ƿ�Ϊ�ƶ��ն�
								mobile: !!u.match(/AppleWebKit.*Mobile.*/),
				                		ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios�ն� 
				                		android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android�ն˻���uc����� 
				                		iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //�Ƿ�ΪiPhone����QQHD����� 
				                		iPad: u.indexOf('iPad') > -1, //�Ƿ�iPad 
				                		webApp: u.indexOf('Safari') == -1, //�Ƿ�webӦ�ó���û��ͷ����ײ� 
				                		Windows: u.indexOf('Windows') > -1 //�Ƿ�Windows
				            }; 
				         }(), 
				         language:(navigator.browserLanguage || navigator.language).toLowerCase() 
				};
			
		
			  function playnext() { //��һҳ��1
			  	//alert("������һ��");
			  	if (nextplay.lenght>0)
			  		location.replace(nextplay);
			  }        

	      function getmd5(){ //ע����
	      		var lic = ""; //new Array("71b05747cfe649eecb77a448af4b5a34","ebcecc41af34e2ff4599e0318d54b89e","cfd4bcfde15c894b6174cd313de5daeb");
	        	return lic;
	      }
	      
	      function doHTML5(url){
			document.write('<div class="video_control"  id="p_player">');
			document.write('<video id="video" width="'+vwidth+'" height="'+vheight+'" controls="controls" autoplay="true" src="'+url+'">');
	  		document.write('<source src="'+url+'" type="video/mp4" /></video>');
			document.write('</div>');	      
	      }
	      
//function play(url,pic,next,pic2,vwidth,vheight,rtspurl)
function play(video_info)	
{
	//Base 64
	var BasePlay = new Base64();
	//var str = BasePlay.encode("http://rtmplive2.cnr.cn/live/TEchex/playlist.m3u8");
	//var str = BasePlay.encode("rtsp://220.181.79.43:554/vod/20140426mr");
	//alert("base64 encode:" + str);
	//str="aHR0cDovL3J0bXBsaXZlMi5jbnIuY24vbGl2ZS9URWNoZXgvcGxheWxpc3QubTN1OA==";
	//str = BasePlay.decode("cnRzcDovL3YudmlzaW9udG9wcy5jb206NTU0L2xpdmUvVEVjaGV4");	
	//alert("base64 decode:" + str);
	//alert(BasePlay.decode(video_info.surl));
	vwidth = video_info.width;
	vheight = video_info.height;
	vlogo=video_info.logo;
						
	//�ж���������ն�
					  		
	if (browser.versions.iPad||browser.versions.iPhone) 
	{	
		//Apple�ն˻�֧��HTML5�������
		doHTML5(BasePlay.decode(video_info.src));
		//doHTML5(video_info.src);
	} else 
	{ 
		if (browser.versions.android)
		{ //Android 3.x�ն�
                  //�ж�android�汾 
                       	var ua = navigator.userAgent.toLowerCase();
                       	if(ua.indexOf("android 4")>0) 
                       	{
			//Apple�ն˻�֧��HTML5�������
				doHTML5(BasePlay.decode(video_info.src));
				//doHTML5(video_info.src);
			 }else
			{
				location.replace(BasePlay.decode(video_info.surl));
				//location.replace(video_info.surl);
			}
		}else
		{//�����ն�
				if (!detectFlash()){
					if(checkhHtml5()){
						doHTML5(BasePlay.decode(video_info.src));
						//doHTML5(video_info.src);
					}
				}
				else //��Ҫflash֧��
				{
					nextplay=video_info.next;
						            
					// For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. 
					var swfVersionStr = "10.1.0";
					 // To use express install, set to playerProductInstall.swf, otherwise the empty string. 
					var xiSwfUrlStr = "playerProductInstall.swf";

					var flashvars = {};
					flashvars.src=BasePlay.decode(video_info.src);  //video_info.src; //BasePlay.decode(video_info.src);
					flashvars.autoPlay=video_info.autoPlay;
					flashvars.streamType = "dvr"; //recorded, live, dvr
					flashvars.controlBarAutoHide =true; //"true" "false"; 
					flashvars.tintColor="FF0123"; //"1A679B";
					flashvars.scaleMode="letterbox"; //"none"; //"stretch"; //"letterbox"; 
					//flashvars.src_preroll="WT-WX.jpg";
					flashvars.javascriptCallbackFunction = 'onJSBridge';
					 
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
								Player.addEventListener("playStateChange", "onplayStateChange");
								
								$(window).resize(playerResize);
								return setTimeout(playerResize, 10);
						}
					};
												
					onDynamicStream = function() 
					{
						return setTimeout(updateDynamicStreamItems, 10);
					};


//ǰ�ù��ͼƬ												
					var adparameters=
					{
							id: "showImageAdCenter"
						    , url: "WT-WX.jpg"
						    , layoutInfo: {
						        width: 160
						        , height: 80
						        , horizontalAlign: "center"
						        , verticalAlign: "middle"
						        //, bottom: (vheight-360)/2
						    }
						    //, clickUrl: "http://www.visiontops.com"
						    , pauseMainMediaOnClick: true
						    , autoCloseAfter:4
						    , closable: false
							, hideScrubBarWhilePlayingAd: true
							, pauseMainMediaWhilePlayingAd: false
							, resumePlaybackAfterAd: false
					}
																 
		            var prerollShown = false;				
        		            
		            onBufferingChange = function(buffering) 
		            {
//alert("buffer");		            
					    if (buffering && !prerollShown) {
					        prerollShown = true;
//alert("buffer-show");       
					        Player.displayAd(adparameters);
									//Player.closeAd(adparameters.id);
					    } else
					    {
					    	prerollShown = false;
						}
					}
					
					onplayStateChange= function(playstate) 
		            {
//alert("pause "+prerollShown);		            
					    //if (getPaused()){ // && !prerollShown) {
					    if (!prerollShown) {
					        prerollShown = true;
//alert("pause-show "+prerollShown);	        
					        Player.displayAd(adparameters);
									
					    }else
					    {
					    	prerollShown = false;
//alert("pause-noshow "+prerollShown);						    	
					    	Player.closeAd(adparameters.id);
					    }
//alert("state"+prerollShown);	
				    
					}

					//onMediaError = function(code, message, detail, playerId)
					//{
					//	Player.load();
						//return setTimeout(mediaError, 10, code, message, detail);
					//};
					onMediaError = function(buffering) 
		            {
//alert("error "+prerollShown);			            
					    if (!prerollShown) {
					        prerollShown = true;

//alert("error-show "+prerollShown);	        
					        Player.displayAd(adparameters);
							//Player.closeAd(adparameters.id);
					    }
					}

					if (!flashvars.controlBarAutoHide)	vheight=30+Number(vheight);

					var params = {};
					params.quality = "high";
					params.bgcolor = "#000000";
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
	      	}				
	}
}	      
  
  

