var updateKey = "Nickname";

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
	if(verification.length != 4) {
		console.log("Error verification format.")
		return false;
	}
	//var passwordEn = encrypt(password, getIMEI());
	var passwordEn = password;
	//console.log(passwordEn);
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

function update()
{
	var Passwd = document.getElementById("Passwd").value;
	console.log(Passwd, " success ");
 $.ajax({  

 
                url: '../rest/update',  
                type: 'POST',  
         		data : {
				Nickname:'Nickname',
				MobilePhone: '15010215479',
				Passwd :Passwd,
				Personal_signature :'1sdfasdlfasdlkfjlsk',
				Email :'1sdfasdlfasdlkfjlsk',
				Home_address :'1sdfasdlfasdlkfjlsk',
				Work_address :'1sdfasdlfasdlkfjlsk',
				Icon :'1sdfasdlfasdlkfjlsk',
				Sex :0,
			},
			success : function(data, textStatus, jqXHR) {
				alert("data transfer success--"+data);
			},
			dataType : "json"
                }); 
				alert("data transfer"); 
}

function getInfo()
{
 $.ajax({  
                url: '../rest/getInfo',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交"+data);
				$("#Passwd").html(data.passwd);
				alert("异步方法2"+data);
			},
			dataType : "json"
                });  
}



/*function update1() {

	var v = document.getElementById("Nickname").value;
	alert(v);
	console.log(v, " success ");
	update({
        Id: 3,
		Nickname:v,
		MobilePhone: 'salkflasjdkf',
		Passwd : 'salkflasjdkf',
		Personal_signature : 'salkflasjdkf',
		Email : 'salkflasjdkf',
		Home_address : 'salkflasjdkf',
		Work_address : 'salkflasjdkf',
		Icon : 'salkflasjdkf',
		Sex : 0,
	});
	console.log("hello");
	
}
*/ 


/*function getValue()
{
 //$.ajax({  
   //             url: '../rest/item/update',  
     //           type: 'GET',
       //             data : {
		//					Id: updateKey == "Id" ? v : null,
			//				Nickname: updateKey == "Nickname" ? v : null,
				//			MobilePhone: updateKey == "MobilePhone" ? v : null,
					//		Passwd : updateKey == "Passwd" ? v : null,
					//		Personal_signature : updateKey == "Personal_signature" ? v : null,
						//	Email : updateKey == "Email" ? v : null,
							//Home_address : updateKey == "Home_address" ? v : null,
							//Work_address : updateKey == "Work_address" ? v : null,
							//Icon : updateKey == "Icon" ? v : null,
							//Sex : updateKey == "Sex" ? v : null
						
				},
			success : function(data, textStatus, jqXHR) {
				$("#Nickname").html(data.Nickname);
				alert("成功");
							       
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			 },
			dataType : "json"
                });  
}/*/

function postData1(d, u) {
	$.ajax({
		url : u,
		type : "POST",
		crossDomain: true,
		async: false,
		//data: JSON.stringify(d),
		data: d,
		success: function(data) {
			console.log(u, "====", data);
			postRet = data;
		},
		error: function(e) {
			console.log("postData error: ", e);
		},
		//contentType: "application/json",
		dataType : "json"
	});
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
	//var passwordEn = encrypt(password, getIMEI());
	var passwordEn = password;
	console.log(passwordEn);
	//console.log(decrypt(passwordEn, getIMEI()));
	postData({
		UserId: userId,
		PasswordEn: passwordEn,
		IMEI: getIMEI()
	}, baseUrl + "rest/verify");
	console.log(getRet);
}

function care(userId) {
	postData({
		UserId: userId,
		IsCare: true
	}, baseUrl + "rest/item/care");
	console.log(postRet);
}

function getCared() {
	getData({}, baseUrl + "rest/item/getCared");
	console.log(getRet);
}

function getFansList() {
	getData({}, baseUrl + "rest/item/getFansList");
	console.log(getRet);
}
