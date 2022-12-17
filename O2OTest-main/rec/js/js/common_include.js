/**
 * Mobile ajax call function
 * 
 * @param reqKey 요청코드
 * @param params {key1: val1, key2: val2...}
 * @param callback 성공시 실행 함수
 */
function mobileCall(reqCd, argParams, argCallBack) {
	var execParams = {
			pluginId : 'webCommon' ,
			method : 'callService' ,
			params : {
				"JsonData" : JSON.stringify(argParams),
				"RequestCd" : reqCd
			},
			callBack : function(isOK, json) {
				if(typeof(argCallBack) === "function")
					argCallBack(json);
			}
		};
	SDSFrameWork.plugin.execute(execParams);
}
/**
 * ajax call function
 * 
 * @param requestUrl
 * @param dataType (json, xml....)
 * @param params {key1: val1, key2: val2...}
 * @param callback 성공시 실행 함수
 * @param async (동기화여부 - 생략시 기본값 false)
 */
function ajaxCall(requestUrl, dataType, params, callback, async) {
	
	var defAsync = false;
	
	if(async != null && async != undefined) {
		defAsync = async;
	}
	
	if(params == null) {
		params = "{}";
	}
	
	console.log("-------------- Ajax --------------");
	console.log(" * requestUrl : " + requestUrl);
	console.log(" *   dataType : " + dataType);
	console.log(" *     params : " + JSON.stringify(params));
	console.log("----------------------------------");

	$.ajax({
		type: "post",
		url : requestUrl,
		dataType : dataType,
		data : JSON.stringify(params),
		contentType: "application/json; charset=utf-8",
		async : defAsync,
		beforeSend: function() {
		},
		success : function(json){
			if(typeof(callback) === "function")
				callback.call(this, json);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("ajax error!!!");
			console.log("jqXHR.readyState : "+jqXHR.readyState);
			console.log("jqXHR.status     : "+jqXHR.status);
			console.log("jqXHR.statusText : "+jqXHR.statusText);
			
			if(jqXHR.readyState == 4) {
				if(jqXHR.status == 200) {
				}
			}
		},
		complete : function(json) {
		}
	});
}

// 앱 강제종료
function nativeAlertExit(msg){ 
	var params = {
		  "pluginId" : 'webCommon'
		, "method"   : 'nativeAlert'
		, "params"   : {"msg":msg}
		, "callBack" : function(isOK, json){
			var p = {
					pluginId	: 'webCommon' ,
					method		: 'closeApp' ,
					params		: {},
					callBack	: function(isOK, json) {
						// 실패
						if(!isOK){
							alert("통신상태 불안정으로\n서비스가 원활하지 않습니다.\n이용에 불편을 드려 죄송합니다.");
							return;
						}
					}
				};		
			SDSFrameWork.plugin.execute(p);
		}
	};		
	SDSFrameWork.plugin.execute(params);
}

/**
 * 쿠키 저장
 * 
 * @param cookieName
 * @param value
 * @param exdays
 */
function setCookie(cookieName, value, exdays) {
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() + exdays);
	document.cookie = cookieName + "=" + value + "; expires=" + expireDate.toGMTString();
}

/**
 * 쿠키값 return
 * 
 * @param cookieName
 * @returns
 */
function getCookie(cookieName) {
	cookieName += "=";
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cookieName);
	var cookieValue = "";
	
	if(start != -1) {
		start += cookieName.length;
		var end = cookieData.indexOf(";", start);
		if(end == -1)
			end = cookieData.length;
		
		cookieValue = cookieData.substring(start, end);
	}
	
	return unescape(cookieValue);
}

$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function () {
		var name = $.trim(this.name),
		value = $.trim(this.value);
		
		if(o[name]) {
			if(!o[name].push) {
				o[name] = [o[name]];
			}
			o[name].push(value || '');
		} else {
			o[name] = value || '';
		}
	});
	return o;
}