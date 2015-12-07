var isAndroid = isAndroidSystem();

//判断系统是否是android
function isAndroidSystem() {
	var u = navigator.userAgent;
	if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {
		return true;
	} else {
		return false;
	}
}

//显示toast
function alertToast(str) {
	if (isAndroid) {
		AndJs.showToast(str);
	} else {
		alert(str);
	}
}

//获取手机IMEI
function getIMEI(){
	if (isAndroid) {
		return AndJs.getIMEI();
	} else {
		return "1111111111111111";
	}
}

//获取手机IMSI
function getIMSI(){
	if (isAndroid) {
		return AndJs.getIMSI();
	} else {
		return "222222222222222";
	}
}

//获取手机号
function getPhoneNum(){
	if (isAndroid) {
		return AndJs.getPhoneNum();
	} else {
		return "3333333333333333";
	}
}

//获取GPS
function getCurrentGPS(){
	if (isAndroid) {
		return AndJs.getCurrentGPS();
	} else {
		return "123456,654321";
	}
}

//获取经度
function getLongitude(){
	if (isAndroid) {
		return AndJs.getLongitude();
	} else {
		return "123456";
	}
}

//获取纬度
function getLatitude(){
	if (isAndroid) {
		return AndJs.getLatitude();
	} else {
		return "654321";
	}
}

//获取当前位置
function getCurrentLocation(){
	if (isAndroid) {
		return AndJs.getCurrentLocation();
	} else {
		return "BeijingBeijing";
	}
}

//显示手机通知
function showNotification(a, b){
	alertToast("---------");
	if (isAndroid) {
		
		AndJs.showNotification(a, b);
	} else {
		//alert(a + b);
		console.log(a, b);
	}
}
