var updateKey = "nickname";

function userLogin() {
	var userId = document.getElementById("username_login").value;
	var password = document.getElementById("password_login").value;
	if (!checkUserId(userId) || !checkPasswd(password)) {
		return false;
	}
	return verifyPasswd(userId, password);
}

function userRegist() {
	var userId = document.getElementById("username_regist").value;
	var password = document.getElementById("password_regist").value;
	if (!checkUserId(userId) || !checkPasswd(password)) {
		return false;
	}
	var verification = document.getElementById("verification").value;
	console.log(verification);
	if(verification.length != 6) {
		console.log("Error verification format.")
		return false;
	}
	var passwordEn = encrypt(password, getIMEI());
	console.log(passwordEn);
	console.log(decrypt(passwordEn, getIMEI()));
	postData({
		UserId: userId,
		PasswordEn: passwordEn,
		Verification: verification,
		IMEI: getIMEI()
	}, baseUrl + "rest/register");
	console.log(postRet);
}

function getVerification() {
	var userId = document.getElementById("username_regist").value;
	if (!checkUserId(userId)) {
		return "";
	}
	getData({
		UserId: userId,
		IMEI: getIMEI()
	}, baseUrl + "rest/getVerification");
	console.log(getRet);
}

function update() {
	var v = document.getElementById("upvalue").value;
	console.log(updateKey, " : ", v);
	postData({
		UpdateKey: v
	}, baseUrl + "rest/update");
	console.log(postRet);
}

function getPublishedList() {
	var userId = document.getElementById("username_target").value;
	if (!checkUserId(userId)) {
		return null;
	}
	getData({
		UserId: userId
	}, baseUrl + "rest/item/getPublishedList");
	console.log(getRet);
}

function getWatchedList() {
	getData({}, baseUrl + "rest/item/getWatchedList");
	console.log(getRet);
}

function verifyPasswd(userId, password) {
	var passwordEn = encrypt(password, getIMEI());
	console.log(passwordEn);
	console.log(decrypt(passwordEn, getIMEI()));
	getData({
		UserId: userId,
		PasswordEn: passwordEn,
		IMEI: getIMEI()
	}, baseUrl + "rest/verify");
	console.log(getRet);
}

function care() {
	postData({
		UserId: userId,
		IsCare: true
	}, baseUrl + "rest/item/care");
	console.log(postRet);
}
