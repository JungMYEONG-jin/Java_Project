/*********************************************************************
 * @제목 : 신한DS sframe 공통 모듈
 * @설명 : S-Frame Native 연동 모듈
 * @변경이력
 * 1. 2019.02.04 : 최초작성
 * 
 * @author kswksk
 * @date 2019.02.04
 ********************************************************************/
// Java Map 처럼 사용하기 위한 객체
var SFMap = function() {};

SFMap.prototype = {
    map : new Object(),

    // 데이터 입력
    put : function(key, value){
        this.map[key] = value;
    },

    // 데이터 가져오기
    get : function(key){
        return this.map[key];
    },

    // 데이터 내용에 값이 있는지 찾기
    containsKey : function(key){
        return key in this.map;
    },

    // value 가 있는지 확인
    containsValue : function(value){
        for(var prop in this.map){
            if(this.map[prop] == value) return true;
        }
        return false;
    },

    // 비여 있는지 확인
    isEmpty : function(key){
        return (this.size() == 0);
    },

    // 데이터 지우기
    clear : function(){
        for(var prop in this.map){
            delete this.map[prop];
        }
    },

    // 해당 키의 데이터 삭제
    remove : function(key){
        delete this.map[key];
    },

    // 입력된 데이터 키목록 가져오기
    keys : function(){
        var keys = new Array();
        for(var prop in this.map){
            keys.push(prop);
        }
        return keys;
    },

    // value 전체 가져오기
    values : function(){
        var values = new Array();
        for(var prop in this.map){
            values.push(this.map[prop]);
        }
        return values;
    },

    // 데이터 사이즈 가져오기
    size : function(){
        var count = 0;
        for (var prop in this.map) {
            count++;
        }
        return count;
    }
};

var SFrame = function () {};

SFrame.Const = {
    // 개발모드 여부
    isDevelop : true,

    // Browser OS TYPE
    BROWSER_TYPE_IOS 	: 0,
    BROWSER_TYPE_AND 	: 1,
    BROWSER_TYPE_OTHER  : 2,

    // 네트워크 관련 키
    KEY_ERROR_CODE : "error_code",
    KEY_ERROR_MSG  : "error_msg",
    KEY_ERROR_DATA : "receiveData",

    // IOS 연동 스키마
    IOS_SCHEME_PRE : "sframe://",
};

SFrame.util = {
    JSONtoString : function(object) {
        // 프레임워크가 바뀌면 수정되어야 할 부분
        return JSON.stringify(object);
    },

    stringToJSON : function(jsonStr) {
        if (jsonStr == null || jsonStr == '') {
            return [];
        }

        while (jsonStr.indexOf('+') > -1) {
         	jsonStr = jsonStr.replace('+', ' ');
        }

        var decJsonStr = decodeURIComponent(jsonStr);

        // 프레임워크가 바뀌면 수정되어야 할 부분
        var jsonData = JSON.parse(decJsonStr);
        return jsonData;
    },

    getDeviceType : function() {
        /*
         * 		// navigator.userAgent.match(/Android/i)
         // || navigator.userAgent.match(/webOS/i)
         // || navigator.userAgent.match(/iPhone/i)
         // || navigator.userAgent.match(/iPad/i)
         // || navigator.userAgent.match(/iPod/i)
         // || navigator.userAgent.match(/BlackBerry/i)
         // || navigator.userAgent.match(/Windows Phone/i)
         */
        if( navigator.userAgent.match(/Android/i)) {
            return SFrame.Const.BROWSER_TYPE_AND;
        } else if (navigator.userAgent.match(/iPhone/i)
                   || navigator.userAgent.match(/iPad/i)
                   || navigator.userAgent.match(/iPod/i)) {
            return SFrame.Const.BROWSER_TYPE_IOS;
        } else {
            return SFrame.Const.BROWSER_TYPE_OTHER;
        }
    },

    getUnickKey : function() {
        // 년월일밀리초_랜덤값
        var toDay = new Date();
        var key = SFrame.util.addLeft(toDay.getFullYear(), 4, "0")
        + SFrame.util.addLeft(toDay.getMonth()+1, 2, "0")
        + SFrame.util.addLeft(toDay.getDate(), 2, "0")
        + SFrame.util.addLeft(toDay.getHours(), 2, "0")
        + SFrame.util.addLeft(toDay.getMinutes(), 2, "0")
        + SFrame.util.addLeft(toDay.getSeconds(), 2, "0")
        + SFrame.util.addLeft(toDay.getMilliseconds(), 3, "0");
        // 랜덤정보 생성
        key = key + "_" + String(Math.round(100000*Math.random()));

        return key;
    },

    addLeft : function(data, size, prefix) {
        if (data == null) {
            data = "";
        }
        data = String(data);
        while (data.length < size) {
            data = prefix + data;
        }
        return data;
    }
};

SFrame.plugin = {
    register : new SFMap(),
    execute : function(params) {
        try {
            var pluginId = params.pluginId;
            var method = params.method;
            if (pluginId == null) {
                alert('요청 pluginId 값이 존재하지 않습니다.');
                return;
            }

            // 유니크 키를 생성하여 등록함
            var callBacKey = SFrame.util.getUnickKey();
            params.callBacKey = callBacKey;
            this.register.put(callBacKey, params);
            var strParams = SFrame.util.JSONtoString(params.params);

            if (strParams == null) {
                strParams = '';
            }

            if (SFrame.util.getDeviceType() == SFrame.Const.BROWSER_TYPE_AND) {
                window.sframe.execute(pluginId, method, callBacKey, strParams);
            } else if (SFrame.util.getDeviceType() == SFrame.Const.BROWSER_TYPE_IOS) {
                var schemeUrl = SFrame.Const.IOS_SCHEME_PRE
                +"plugin?pluginId="+encodeURIComponent(pluginId)
                +"&method="+encodeURIComponent(method)
                +"&callBackKey="+callBacKey
                +"&params="+encodeURIComponent(strParams);
                this.executeForiOS(schemeUrl);
            } else {
                alert('PC 브라우져에서는 지원되지 않는 기능입니다.');
            }
        } catch (e) {
            console.log(e);
            // alert('PC 브라우져에서는 지원되지 않는 기능입니다.');
        }
    },
    callBack : function (isOK, callBackKey, jsonData) {
    	//var jsonData = SFrame.util.stringToJSON(strData);
        var params = this.register.get(callBackKey);

        if (params == null) return;

        if (params.callBack != null) {
            setTimeout(function() {
               params.callBack(isOK, jsonData);
            }, 0);
        }
        // 콜백을 여러번 받을 경우가 발생하여 제거하지 않는다.
        // this.register.remove(callBackKey);
    },
    getExecuteId : function(pluginId, method) {
        return pluginId + "-" + method;
    },
    executeQueueForiOS : [],
    executeForiOS : function(url) {
        if (this.executeQueueForiOS.length < 1) {
            this.executeSubForiOS();
        }
        this.executeQueueForiOS.push(url);
    },
    executeSubForiOS : function() {
        var self = this;
        setTimeout(function() {
                   var url = self.executeQueueForiOS.shift();
                   window.location.href = url;
                   if (self.executeQueueForiOS.length > 0) {
                   self.executeSubForiOS();
                   }
                   }, 0);
    }
};


// WebView History 관련 플러그인 (back, forward, clear)
SFrame.history = {
    KEY_BACK 	: "back",
    KEY_FORWARD : "forward",
    KEY_CLEAR 	: "clear",
    KEY_HOME 	: "home",

    clear : function() {
        this.execute(this.KEY_CLEAR);
    },

    goBack : function() {
        this.execute(this.KEY_BACK);
    },

    goForward : function() {
        this.execute(this.KEY_FORWARD);
    },

    goHome : function() {
        this.execute(this.KEY_HOME);
    },

    execute : function(command) {
        var params = {
            pluginId : 'history',
            method : 'onExecute',
            params : {'command' : command}
        };
        SFrame.plugin.execute(params);
    }
};

///////////////////////////////////////////////////////////////////////
// 기본적으로 제공하는 기능 정의 부
// 1. 네트워크 요청
// 2. 로딩빠 표출 기능
// 3. 진동처리 기능
///////////////////////////////////////////////////////////////////////
// 크롬 브라우져에서 테스트 여부 확인
 function isBrowserTest() {
     if (getOSType() == "B" || (getOSType() == "A" && typeof window.sframe == "undefined")) {
         return true;
     } else {
         return false;
     }
 }
 
function getOSType() {
    if (isMobile()) {
        if (/Android/i.test(navigator.userAgent)) {
        	return "A";
        } else {
        	return "I";
        }
	} else {
	    return "B";
	}
}

// S-Frame 모바일 앱 여부
function isMobileApp() {
    if (typeof SFrame !== "undefined") {
        return true;
    } else {
        return false;
    }
}

function isMobile() {
    // return /Android|webOS|iPhone|iPad|BlackBerry/i.test(navigator.userAgent);
    return /Android|iPhone|iPad/i.test(navigator.userAgent);
}

/**
 * 네이티브를 이용한 네트워크 통신
 *
 *	 @param params :
 *	   {
 *			 "method" 	: "POST",                         				// (필수) http 통신 방식 : GET or POST
 *			"fullUrl" 		: "https://test.com/a.jsp",  			    // (필수) 통신요청 URL : 전체 URL (fullUrl or tailUrl 둘중하나 필수)
 *			"tail Url" 	: "/a.jsp",										    // (필수) 통신요청 URL : 도메인을 제외한 URL, 도메인은 앱에 설정(fullUrl or tailUrl 둘중하나 필수)
 *			"params" 	: {"a1":"a1_value", "a2" : "a2_value"}, // (선택) 요청 파라메터
 *         "isLoadingBar" : true,										// (선택) 로딩바 표출여부
 *			"tag"			: 1,												    // (선택) 콜백 함수를 1개로 구현 시 통신을 구분하기 위한 태그 정보 (숫자)
 *			"header"	: {"header1" : "header1_value"}	    // (선택) 해더 추가 정보 필요 시
 *	   }
 *  @param callBack : 처리 결과 콜백
 */
function SFRequestData(params, callBack) {
    SFrame.plugin.execute({
		'pluginId':'http',
		'method':'request',
		'callBack':callBack,
		'params':params
	});
}

/**
 * 네이티브의 로딩바를 표출 한다.
 * @param isLoadingBar : 로딩바 표출여부
 * @param callBack : 처리 결과 콜백
 */
function SFSetLoadingBar(isLoadingBar, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':'loadingbar',
            'method':'setLoadingBar',
            'callBack':callBack,
            'params':{"isLoadingBar" : isLoadingBar}
        });
	} else {
	    callBack(true, {});
	}
}

/**
 * 진동 설정
 * @param isVibrate : 진동 여부
 * @param callBack : 처리 결과 콜백
 */
function SFSetVibrate(isVibrate, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':'vibrate',
            'method':'setVibrate',
            'callBack':callBack,
            'params':{"isVibrate" : isVibrate}
        });
	}
}

/**
 * 네이티브에서 확장한 플러그인을 호출한다.

 * @param pluginId    : 클래스 아이디
 * @param methodId : 메소드 아이디
 * @param params    : 네이티브에 전달 할 파라메터
 * @param callBack   : 처리결과 콜백 함수
 */
function SFCallNative(pluginId, methodId, params, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':pluginId,
            'method':methodId,
            'callBack':callBack,
            'params':params
        });
    }
}

/**
 * 기기 정보 조회
 */
function SFGetDeviceInfo(callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SFDeviceInfo",
            'method': "getDeviceInfo",
            'callBack': callBack,
            'params': {}
        });
    } else {
        var browserInfo = getBrowserInfo();
        var testHeader = {
            app_name : browserInfo.name,
            app_ver : "1.0.0",
            pkg_name : "com.shinhands.sframe.sample",
            os_type : "A",
            device_model : navigator.userAgent,
            device_id : "sframeserverdeviceid=",
            device_ins_id : "sframeserverdeviceinsid=",
            os_version : browserInfo.version
        };
        callBack(true, testHeader);
    }
}

/**
 * 네이티브에서 확장한 플러그인을 호출한다.
 */
function SFCloseView() {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SFClose",
            'method': "closeView",
            'callBack': function(isOk,result){},
            'params': {}
        });
    }
}

/**
 * 네이티브에서 확장한 플러그인을 호출한다.
 */
function SFCloseApp() {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SFClose",
            'method': "closeApp",
            'callBack': function(isOk,result){},
            'params': {}
        });
    }
}

/**
 * S-Auth 생체 인증 호출
 */
function SFSAuth(type, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SAuth",
            'method': "auth",
            'callBack': function(isOk,result){
                if (callBack != null && callBack != undefined) {
                    callBack(isOk,result);
                }
            },
            'params': {'type': type}
        });
    } else {
        callBack(false, {error_code:'9999', error_msg:'모바일에서만 지원되는 기능입니다.'});
    }
}

/**
 * S-Auth 생체 인증 호출
 */
function SFSAuthInit(callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SAuth",
            'method': "authInit",
            'callBack': function(isOk,result){
                if (callBack != null && callBack != undefined) {
                    callBack(isOk,result);
                }
            },
            'params': {}
        });
    } else {
        // callBack(true, {error_code:'0000', error_msg:'모바일에서만 지원되는 기능입니다.'});
    }
}

/**
 * S-Auth 생체 인증 등록
 */
function SFSAuthReg(type, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SAuth",
            'method': "authReg",
            'callBack': function(isOk,result){
                if (callBack != null && callBack != undefined) {
                    callBack(isOk,result);
                }
            },
            'params': {'type':type}
        });
    } else {
        callBack(false, {error_code:'9999', error_msg:'모바일에서만 지원되는 기능입니다.'});
    }
}

// 로그인 여부 설정
function SFSetLoginInfo(isLogin, data, callBack) {
    if (!isBrowserTest()) {
        SFrame.plugin.execute({
            'pluginId':"SFSetLoginInfo",
            'method': "setLoginInfo",
            'callBack': function(isOk,result){
                if (callBack != null && callBack != undefined) {
                    callBack(isOk, result);
                }
            },
            'params': {'isLogin':isLogin, 'loginInfo' : data}
        });
    } else {
        if (callBack != null && callBack != undefined) {
            callBack(true, {});
        }
    }
}