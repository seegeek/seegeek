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



// 接口
function updateNickname()
{
var nickname=document.getElementById("Nickname").value;
console.log(nickname,"success");
updateCommon(nickname,null,null,null,null,null,null,null,null);
}

function updateSignature()
{
var personal_signature=document.getElementById("Personal_signature").value;
console.log(personal_signature,"success");
updateCommon(null,null,null,personal_signature,null,null,null,null,null);
}

function updateHaddress()
{
var province = document.getElementById("cmbProvince").value;
console.log(province);
var city = document.getElementById("cmbCity").value;
console.log(city);
var area = document.getElementById("cmbArea").value;
console.log(area);
var address = (province+city+area);
console.log(address);

updateCommon(null,null,null,null,null,address,null,null,null);
}


function updateWaddress()
{
var province1 = document.getElementById("cmbProvince").value;
console.log(province1);
var city1 = document.getElementById("cmbCity").value;
console.log(city1);
var area1 = document.getElementById("cmbArea").value;
console.log(area1);
var address1 = (province1+city1+area1);
console.log(address1);

updateCommon(null,null,null,null,null,null,address1,null,null);
}


function updateSex()
{
var sex1= document.getElementById("cmbSex").value;

	if(sex1 == '男'){
		sex1=1;
		}
		else{
			sex1=0;
			}
console.log(sex1);
updateCommon(null,null,null,null,null,null,null,null,sex1);
}
function updateCommon(p1,p2,p3,p4,p5,p6,p7,p8,p9)
{
 $.ajax({  
                url: '../rest/update',  
                type: 'POST',  
         		data : {
				Nickname:p1,
				MobilePhone:p2,
				Passwd:p3,
				Personal_signature:p4,
				Email:p5,
				Home_address:p6,
				Work_address:p7,
				Icon:p8,
				Sex:p9,
		
			},
			success : function(data, textStatus, jqXHR) {
				alert("更新成功--"+data);
			},
			error: function (e) {
			alert(123);
			
			},
			dataType : "json"
                });  
}

function getInfo()
{
 $.ajax({  
                url: '../rest/getInfo',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交"+data);
				$("#Nickname").html(data.nickname);
				$("#Personal_signature").html(data.personal_signature);
				$("#Home_address").html(data.home_address);
				$("#Work_address").html(data.work_address);
//				$("#Hobby").html(data.hobby);
			    	var sex2;
					if(data.sex == 1){
							sex2= '男';
									}else{
										sex2= '女';
										}
										console.log(data.Sex+"=="+sex2);
				$("#Sex").html(sex2);
				
				console.log("transfer successful");
			},
			dataType : "json"
                });  
}

/*function update()
{
	var Nickname = document.getElementById("Nickname").value;
	console.log(Nickname, " success ");
 $.ajax({  

 
                url: '../rest/update',  
                type: 'POST',  
         		data : {
				Nickname:Nickname,
				MobilePhone: '15010215479',
				Passwd :'salkflasjdkf',
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
function update1()
{
	var Personal_signature = document.getElementById("Personal_signature").value;
	console.log(Personal_signature, " success ");
 $.ajax({  

 
                url: '../rest/update',  
                type: 'POST',  
         		data : {
				Nickname:'Nickname',
				MobilePhone: '15010215479',
				Passwd :'salkflasjdkf',
				Personal_signature :Personal_signature,
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
				//$("#Nickname").html(data.nickname);
				$("#Personal_signature").html(data.personal_signature);
			},
			dataType : "json"
                });  
}*/




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
