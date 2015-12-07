var isAndroid = isAndroidSystem();

//�ж�ϵͳ�Ƿ���android
function isAndroidSystem() {
	var u = navigator.userAgent;
	if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {
		return true;
	} else {
		return false;
	}
}

//��ʾtoast
function alertToast(str) {
	if (isAndroid) {
		AndJs.showToast(str);
	} else {
		alert(str);
	}
}

//��ȡ�ֻ�IMEI
function getIMEI(){
	if (isAndroid) {
		return AndJs.getIMEI();
	} else {
		return "1111111111111111";
	}
}

//��ȡ�ֻ�IMSI
function getIMSI(){
	if (isAndroid) {
		return AndJs.getIMSI();
	} else {
		return "222222222222222";
	}
}

//��ȡ�ֻ���
function getPhoneNum(){
	if (isAndroid) {
		return AndJs.getPhoneNum();
	} else {
		return "3333333333333333";
	}
}

//��ȡGPS
function getCurrentGPS(){
	if (isAndroid) {
		return AndJs.getCurrentGPS();
	} else {
		return "123456,654321";
	}
}

//��ȡ����
function getLongitude(){
	if (isAndroid) {
		return AndJs.getLongitude();
	} else {
		return "123456";
	}
}

//��ȡγ��
function getLatitude(){
	if (isAndroid) {
		return AndJs.getLatitude();
	} else {
		return "654321";
	}
}

//��ȡ��ǰλ��
function getCurrentLocation(){
	if (isAndroid) {
		return AndJs.getCurrentLocation();
	} else {
		return "BeijingBeijing";
	}
}

//��ʾ�ֻ�֪ͨ
function showNotification(a, b){
	alertToast("---------");
	if (isAndroid) {
		
		AndJs.showNotification(a, b);
	} else {
		//alert(a + b);
		console.log(a, b);
	}
}
