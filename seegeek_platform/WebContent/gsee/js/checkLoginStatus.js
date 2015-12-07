var loginStatus = 1; // 0 login; 1 nologin; 2 login status expired
// if verify password success, set loginStatus 0; set userId: window.localStorage.setItem("userId", "13242342412");
// if logout, set loginStatus 1

function checkLoginStatus() { //call in onload() when start application
	if(window.localStorage){
		alert('This browser supports localStorage');
	}else{
		alert('This browser does NOT support localStorage');
	}
	var storage = window.localStorage;
	if (!storage.getItem("userId"))
		storage.setItem("userId", 0);
	var userid = storage.getItem("userId");
	getData({
		userId: userid,
		IMEI: getIMEI()
	}, baseUrl + "rest/checkLoginStatus");
	console.log(getRet);
	if (getRet)
		loginStatus = getRet.value;
}

// check loginStatus when need to check login status
