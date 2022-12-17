/*********************************************************************
 * @제목 : S-Frame 2.0 공통유틸용 라이브러리
 * @설명 : 프로젝트에서 사용되는 공통 기능을 정의한다.
 * @변경이력
 * 1. 2020.02.27 : 최초작성
 *
 * @author kswksk
 * @date 2020.02.27
 ********************************************************************/
/*****************************
 * 서버환경 정보
 *****************************/
var _spass_testData = null;
var _spass_testData1 = {"movieRoomInfo":{"password":"1234","movieRoomId":"1234567890","startDttm":"2020042220335700","endDttm":"","movieRoomNm":"테스트","movieRoomCnt":"5","openYn":"Y","movieState":"01","fileId":"0621124020200422203356868","fileChgYn":"Y","regHwnno":"06211240","regDttm":"2020042220335500","jkwNm":"","jkwiNm":"","brNm":"","enterCnt":"","cusNn":"","fileGroupId":"1"},"userList":[{"cusNo":"888575150","movieRoomId":"0621124020200422203355474","urlKey":"c0Yk7E06Al8baFNU9Wp0Bl","startDttm":"","endDttm":"","hwnno":"","regDttm":"2020042220335500","cusNn":"김신한","movieRoomSeq":"1","userG":"C","selfG":"Y","mstBrno":"2018","topsGrade":"9","phoneNo":"01010002000","hwnnm":"","brno":"","brnm":"","jkgpnm":""},{"cusNo":"","movieRoomId":"0621124020200422203355474","urlKey":"daWUCdKEAQ_9dvxZXBhvfq","startDttm":"2020042220340100","endDttm":"","hwnno":"12345677","regDttm":"2020042220335600","cusNn":"","movieRoomSeq":"2","userG":"H","selfG":"N","mstBrno":"","topsGrade":"","phoneNo":"010-7141-1875","hwnnm":"김영희","brno":"1884","brnm":"반월금융센터","jkgpnm":"부지점장"}]};
var _spass_testData2 = {"movieRoomInfo":{"password":"1234","movieRoomId":"1234567890","startDttm":"2020042220335700","endDttm":"","movieRoomNm":"테스트","movieRoomCnt":"5","openYn":"Y","movieState":"01","fileId":"0621124020200422203356868","fileChgYn":"Y","regHwnno":"12345677","regDttm":"2020042220335500","jkwNm":"","jkwiNm":"","brNm":"","enterCnt":"","cusNn":"","fileGroupId":"1"},"userList":[{"cusNo":"888575150","movieRoomId":"0621124020200422203355474","urlKey":"c0Yk7E06Al8baFNU9Wp0Bl","startDttm":"","endDttm":"","hwnno":"","regDttm":"2020042220335500","cusNn":"김신한","movieRoomSeq":"1","userG":"C","selfG":"N","mstBrno":"2018","topsGrade":"9","phoneNo":"01010002000","hwnnm":"","brno":"","brnm":"","jkgpnm":""},{"cusNo":"","movieRoomId":"0621124020200422203355474","urlKey":"daWUCdKEAQ_9dvxZXBhvfq","startDttm":"2020042220340100","endDttm":"","hwnno":"12345677","regDttm":"2020042220335600","cusNn":"","movieRoomSeq":"2","userG":"H","selfG":"Y","mstBrno":"","topsGrade":"","phoneNo":"010-7141-1875","hwnnm":"김영희","brno":"1884","brnm":"반월금융센터","jkgpnm":"부지점장"}]};
if (window.location.href.indexOf("mstep2.do") != -1) {
	_spass_testData = _spass_testData1;
} else {
	_spass_testData = _spass_testData2;
}
if (getUrlParam("isFile") == "N") {
	// 파일 없는 경우 테스트
	_spass_testData.movieRoomInfo.fileId = "";
	_spass_testData.movieRoomInfo.fileChgYn = "N";
}
if (getUrlParam("userId") != undefined && getUrlParam("userId") != "") {
	// 파일 없는 경우 테스트
	for (var i = 0; i < _spass_testData.userList.length; i++) {
		if (_spass_testData.userList[i].selfG != "") {
			if (_spass_testData.userList[i].cusNo != "") {
				_spass_testData.userList[i].cusNo = getUrlParam("userId");
				_spass_testData.userList[i].cusNn = "가"+(Math.random() * 100);
			} else {
				_spass_testData.userList[i].hwnno = getUrlParam("userId");
				_spass_testData.userList[i].hwnnm = "나"+ (Math.random() * 100);
			}
		}
	}
}

if (getUrlParam("roomId") != undefined && getUrlParam("roomId") != '') {
	_spass_testData.movieRoomInfo.movieRoomId = getUrlParam("roomId");
	for (var i = 0; i < _spass_testData.userList.length; i++) {
		_spass_testData.userList[i].movieRoomId = getUrlParam("roomId");
	}
}
/**
 * 
 */
function SFConfig() {
	this.log_level = "none";  // error < (debug==log) < info < none
	this.devUrl = "devnoface2.shinhan.com";
	this.mobileUrl = "devnoface.shinhan.com";
};

var config = new SFConfig();

//if(window.location.hostname.indexOf("dev") == 0)
//{
//	config.log_level = "debug";
//}
//else
//{
//	config.log_level = "none";
//}
config.log_level = "debug";

/**
 * 개발환경 여부
 */
SFConfig.prototype.isDev = function() {
	if (window.location.href.indexOf(this.devUrl) != -1) {
		return true;
	} else {
		return false;
	}
};

//모바일 환경여부
SFConfig.prototype.isMobile = function() {
	if (window.location.href.indexOf("devnoface.shinhan.com") > -1 || window.location.href.indexOf("noface1.shinhan.com") > -1) {
		return true;
	} else {
		return false;
	}
};

/*****************************
 * 통신 관련
 *****************************/
function ajaxMbs(strUrl, params, success, error) {
	if (isMovTest()) {
		if (strUrl.indexOf("selectMovieRoomInfo") != -1) {
			// 룸정보 갖오기
			success(_spass_testData);
			return;
		} else if (strUrl.indexOf("selectMovieFileInfo") != -1) {
			var data = {"movieRoomInfo":_spass_testData.movieRoomInfo,"fileNameList": ["/mov/common/images/0001.jpg","/mov/common/images/0002.jpg","/mov/common/images/0003.jpg"]};
			if (getUrlParam("isFile") == "N") {
				// 파일 없는 경우 테스트
				data.fileNameList = [];
			}

			success(data);
			return;
		}
	}

	if (!params) {
		params = {};
	}

	log.debug("-----------------------------------------------------------");
	log.debug(">>> 요청 : " + strUrl);
	log.debug(">>> 파람 : " + JSON.stringify(params));
	log.debug("-----------------------------------------------------------");

	$.ajax({
		url : strUrl,
		type : 'post',
		data : JSON.stringify(params),
		dataType :"json",
		complete:function(){},
        contentType : 'application/json;charset=UTF-8',
		success : function(response) {
			log.debug("-----------------------------------------------------------");
			log.debug(">>> 응답 : " + strUrl);
			log.debug(">>> 받음 : " + JSON.stringify(response));
			log.debug("-----------------------------------------------------------");

			if (success) {
				success(response);
			}
		},error : function(e) {
			log.debug("-----------------------------------------------------------");
			log.debug(">>> 에러 : " + strUrl);
			log.debug(e);
			log.debug("-----------------------------------------------------------");
			if (error) {
				error(e);
			}
		}
	});
}

/**
 * 영상테스트여부
 * @returns {Boolean}
 */
function isMovTest() {
	if (getUrlParam("isTest") == "Y") {
		return true;
	} else {
		return false;
	}
}
/**
 * iPhone 여부
* @returns {Boolean}
*
 */
function isIphone() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	if (userAgent.match('iphone')) {
		return true;
	} else {
		return false;
	}
};
/**
 * 안드로이드 여부
* @returns
*
 */
function isAndroid() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	return !!userAgent.match('android');
};

/*****************************
 * 로그 출력 관련
 *****************************/
// 로그 출력 객체
function SFLog() {
		this.log_level = config.log_level;  	// error < (debug==log) < info < none
};
var log = new SFLog();
/**
 * 에러 로그 출력
 */
SFLog.prototype.error = function(data, style) {
	if (this.log_level == "none") return;

	if (typeof data == "Object") {
		console.trace(data);
	} else {
		if (style == null || style == undefined) {
			console.trace(data);
		} else {
			console.trace(data, style);
		}
	}
};

/**
 * 디버그 로그 출력 (debug)
 */
SFLog.prototype.debug = function(data, style) {
	if (this.log_level == "none") return;

	if (typeof data == "Object") {
		console.log(data);
	} else {
		if (this.log_level == "debug" || this.log_level == "info") {
			if (style == null || style == undefined) {
				console.trace(data);
			} else {
				console.trace(data, style);
			}
		}
		else
		{
			if (style == null || style == undefined) {
				console.log(data);
			} else {
				console.log(data, style);
			}
		}
	}
};

/**
 * 디버그 로그 출력 (debug)
 */
SFLog.prototype.log = function(data, style) {
	if (this.log_level == "none") return;
	if (style == null || style == undefined) {
		console.log(data);
	} else {
		console.log(data, style);
	}
};

/**
 * 인포 로그 출력 (debug)
 */
SFLog.prototype.info = function(data, style) {
	if (this.log_level == "none") return;

	if (typeof data == "Object") {
		console.info(data);
	} else {
		if (this.log_level == "info") {
			if (style == null || style == undefined) {
				// console.log(data, 'color:black');
				console.info(data);
			} else {
				console.info(data, style);
			}
		}
	}
};
/****************************************
 * 유틸성 함수
  ****************************************/
function getUrlParam(param) {
	var retVal = "";
	var url = location.href;
	var parameters = (url.slice(url.indexOf('?')+1, url.length)).split('&');
	for (var i = 0; i < parameters.length; i++) {
		var varName = parameters[i].split('=')[0];
		if (varName.toUpperCase() == param.toUpperCase()) {
			retVal = parameters[i].split('=')[1];
			retVal = decodeURIComponent(retVal);
			break;
		}
	}
	return retVal;
}
/**
 * 이전으로 가기
 */
function sfGoBack() {
    if ("referrer" in document) {
        window.location = document.referrer;
    } else {
        window.history.back();
    }
}