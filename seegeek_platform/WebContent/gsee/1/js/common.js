var postRet, getRet;

function checkUserId(u) {
	console.log("checkUserId: ", u);
	if(u.length != 11 || u.indexOf("1") != 0) {
		console.log("Error telephone number.")
		return false;
	}
	return true;
}

function checkPasswd(p) {
	console.log("checkPasswd: ", p);
	if(p.length < 8) {
		console.log("Too short password.")
		return false;
	}
	return true;
}

function postData(d, u) {
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

function getData(d, u) {
	$.ajax({
		url : u,
		type : "GET",
		crossDomain: true,
		async: false,
		//data: JSON.stringify(d),
		data: d,
		success: function(data) {
			console.log(u, "====", data);
			getRet = data;
		},
		error: function(e) {
			console.log("getData error: ", e);
		},
		//contentType: "application/json",
		dataType : "json"
	});
}