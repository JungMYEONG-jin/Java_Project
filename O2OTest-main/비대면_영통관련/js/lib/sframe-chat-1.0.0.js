/*********************************************************************
 * @제목 : S-Frame 2.0 채팅 라이브러리
 * @설명 : WebSocket 기반 채팅 서버 연동 모듈 
 * @변경이력
 * 1. 2020.02.27 : 최초작성
 * 
 * @author kswksk
 * @date 2020.02.27
 ********************************************************************/
function SFrameChat(roomId, userId, callBack) {
	log.log("----------------------------------------");
	log.log(" SFrameChat new :::: roomId : " + roomId + " / userId :" + userId);
	log.log("----------------------------------------");
	
	// 메시지 타입
	this.TYPE_MSG 		= "MSG";		// 클라이언트 -> 클라이언트 메시지 또는 서버 -> 클라이언트 메시지 (메시지용)
	this.TYPE_SVR 		= "SVR";		// 서버 -> 클라이언트 메시지 (시스템적인 메시지)
	this.TYPE_INIT 	 	= "INIT";		// 클라이언트 -> 서버 : 나의 정보 초기화
	this.TYPE_INIT_FAIL = "INIT_FAIL";	// 서버 -> 클라이언트 : 정보 초기화 실패
	this.TYPE_CLOSE	 	= "CLOSE";		// 서버 -> 클라이언트 : 방에서 빠져나감
	this.TYPE_CONN 	 	= "CONNECTED"; 	// 서버 -> 클라이언트 : 접속됨
	this.TYPE_PING 	 	= "PING"; 		// TIMEOUT 되지 않도록 PING 발송
	this.TYPE_ERR		= "ERR"; 		// 에러발생 시 화면에 전달용
	this.TYPE_SPASS 	= "SPASS";		// 영상관련 정보
	this.TYPE_RECV_OK 	= "RECV_OK";	// 수신 완료
	
	this.roomId = roomId;
	this.userId = userId;
	this.callBack = callBack;
	this.spassCallBack = null;
	this.webSocket = null;
	this.webSocketInterval = null;
	this.checkPingCount = 0;
	this.messageQueue = new Array();
	this.wssUrl = null;
	this.isConnected = false;
	this.isConnecting = false;
	this.connectCnt = 0;
	this.MAX_CONN_CNT = 3;
	this.roomUserList = [];
	
	this.isFinishSocket = false;
	
	$(window).bind('beforeunload', function () {
		this.close();
	}.bind(this)); 
};

/*************************************
 * 웹 소켓 관련
 ***********************************/
/**
 * 웹소켓 연결
 */
SFrameChat.prototype.connect = function(wssUrl) {
	log.log("----------------------------------------");
	log.log(" SFrameChat wssUrl : " + wssUrl);
	log.log("----------------------------------------");
	
	if (this.webSocket && this.webSocket.readyState == WebSocket.OPEN) {
		if (this.wssUrl == wssUrl) {
			return;
		} else {
			this.webSocket.close();
			this.webSocket = null;
		}
	}
	
	this.isConnected = false;
	this.isConnecting = true;
	this.connectCnt++;
	this.wssUrl = wssUrl;
	this.webSocket = new WebSocket(wssUrl);
	this.webSocket.onmessage = this.gotMessageFromServer.bind(this);
	this.webSocket.onopen = this.gotOnOpen.bind(this);
	this.webSocket.onerror = this.gotError.bind(this);
	this.webSocket.onclose = this.gotClose.bind(this);
	
	// 백그라운드로 내려갔을 때 끊어 졌는지를 체크하는 인터발 (모바일용)
	if (this.webSocketInterval == null) {
		this.webSocketInterval = setInterval(this.checkPing.bind(this), 1000);
	}	
};

/**
 * 주기적인 핑체크
 */
SFrameChat.prototype.checkPing = function() {
	if (!this.webSocket) return;
	
	this.checkPingCount++;
	if (this.webSocket.readyState !== WebSocket.OPEN) {
		if (this.isConnecting == false) {
			this.connect(this.wssUrl);
		}
		return;
	} 
	
	var pingSecond = 60; // 1분30초에 한번씩 핑 체크
	if ((this.checkPingCount % 10) == pingSecond) {
		this.webSocket.send(JSON.stringify(this.generateMsg(this.TYPE_PING, {})));
	}
	
	if (this.checkPingCount == pingSecond) {
		this.checkPingCount = 0;
	}
};

/**
 * 서버에 로그를 찍기 위한 기능 추가
 */
SFrameChat.prototype.sendLog = function(text) {
	var logData =  {
			'type' : 'LOG', 
			'text' : text,
			'userId' : this.userId,
			'roomId' : this.roomId,
			'_msgId' : this.getUUID()
		};
	
	if (this.webSocket && this.webSocket.readyState == WebSocket.OPEN && this.isConnected) {
		log.log("---------------------------------------------------------");
		log.debug("sendLog CHAT SEND : " + JSON.stringify(logData));
		log.log("---------------------------------------------------------");
		this.webSocket.send(JSON.stringify(logData));
	} else {
		this.messageQueue.push(logData);
	}
};

/**
 * 일반적인 메시지 전송
 */
SFrameChat.prototype.sendMsg = function(json, desUserId) {
	var sendData = this.generateMsg(this.TYPE_MSG, json, desUserId);
	
	if (this.webSocket && this.webSocket.readyState == WebSocket.OPEN && this.isConnected) {
		log.log("---------------------------------------------------------");
		log.debug("sendMsg CHAT SEND : " + JSON.stringify(sendData));
		log.log("---------------------------------------------------------");
		this.webSocket.send(JSON.stringify(sendData));
	} else {
		this.messageQueue.push(sendData);
	}
};

/**
 * 영상솔루션 관련 정보 전달
 */
SFrameChat.prototype.sendSPassMsg = function(json, desUserId) {
	var sendData = this.generateMsg(this.TYPE_SPASS, json, desUserId);
	
	if (this.webSocket && this.webSocket.readyState == WebSocket.OPEN && this.isConnected) {
		log.log("---------------------------------------------------------");
		log.debug("sendSPassMsg CHAT SEND : " + JSON.stringify(sendData));
		log.log("---------------------------------------------------------");
		this.webSocket.send(JSON.stringify(sendData));
	} else {
		this.messageQueue.push(sendData);
	}
};

/**
 * 메시지 응답 처리
 */
SFrameChat.prototype.gotMessageFromServer = function (message) {
	var jsonData = JSON.parse(message.data);
	log.log("---------------------------------------------------------");
	log.log("CHAT RECV at SFrameChat.gotMessageFromServer: " + JSON.stringify(jsonData));
	log.log("---------------------------------------------------------");
	
	if (jsonData.type == this.TYPE_SVR) {
		// 서버에서 보낸 메시지 (라이브러리단의 처리를 위해서) 
	} else if (jsonData.type == this.TYPE_INIT_FAIL) {
		// 초기화 실패
		this.callBack(this.generateErrMsg('9001', jsonData.data.msg));
		this.isFinishSocket = true;
		this.webSocket.close();
	} else if (jsonData.type == this.TYPE_PING) {
		// PING은 무시
	} else if (jsonData.type == this.TYPE_SPASS) {
		// SPass쪽으로도 무조건 넣어 줌
		if (this.spassCallBack) {
			this.spassCallBack(jsonData);
		}
	} else {
		if (jsonData.type == this.TYPE_INIT) {
			// 현 접속자 전체
			this.roomUserList = jsonData.data.connectedUsers;
			this.isConnected = true;
			
			if (this.spassCallBack) {
				this.spassCallBack(this.generateMsg(this.TYPE_SPASS, {"subType":"CHAT_READY"}));
			}
			
			while (this.messageQueue.length > 0) {
				this.webSocket.send(JSON.stringify(this.messageQueue[0]));
				var reQueue = this.messageQueue;
				this.messageQueue = new Array();
				for (var i = 1; i < reQueue.length; i++) {
					this.messageQueue[i-1] = reQueue[i];
				}
			}
			
			// 초기화가 완료됬음을 알린다.
			this.callBack(jsonData);
		} else if (jsonData.type == this.TYPE_CONN) {
			// 현 접속자 추가
			var isExist = false;
			for (var i = 0; i < this.roomUserList.length; i ++) {
				if (this.roomUserList[i] == jsonData.data.userId) {
					isExist = true;
					break;
				}
			}
			
			if (!isExist) {
				this.roomUserList[this.roomUserList.length] = jsonData.data.userId;
			}
		} else if (jsonData.type == this.TYPE_CLOSE) {
			// 현 접속자 삭제
			var reArray = [];
			for (var i = 0; i < this.roomUserList.length; i++) {
				if ( this.roomUserList[i] != jsonData.data.userId) {
					reArray[reArray.length] = this.roomUserList[i];
				}
			}
			this.roomUserList = reArray;
		}
		
		// 정의 되지 않은 데이터는 화면으로 보냄
		if (jsonData.userId != this.userId && jsonData.type != this.TYPE_INIT) {
			// 내가 보낸 메시지는 걸러낸다. (필요시 넘기도록 나중에 변경하자!!)
			if (this.callBack) {
				this.callBack(jsonData);
			} else {
				log.error("SFrame Chat not found callback !! do set callBack!!");
			}
		}
	}
};

/**
 * 웹소켓 정상 오픈 이벤트
 */
SFrameChat.prototype.gotOnOpen = function (event) {
	this.isConnecting = false;
	this.connectCnt = 0;
	var sendData = this.generateMsg(this.TYPE_INIT, {});
	log.log("---------------------------------------------------------");
	log.debug("gotOnOpen CHAT SEND : " + JSON.stringify(sendData));
	log.log("---------------------------------------------------------");
	this.webSocket.send(JSON.stringify(sendData));
};

/**
 * 웹소켓 에러 이벤트
 */
SFrameChat.prototype.gotError = function(e) {
	log.error(e);
	this.isConnected = false;
	this.isConnecting = false;
	this.clearCheckPing();
	this.callBack(this.generateErrMsg("9002", "통신상태가 원활하지 않아 연결에 실패하였습니다."));
};

/**
 * 웹소켓 종료 이벤트
 */
SFrameChat.prototype.gotClose = function(e) {
	log.debug(e);
	this.isConnected = false;
	this.isConnecting = false;
	this.webSocket = null;
	this.messageQueue = new Array();
	this.clearCheckPing();
	
	if (this.isFinishSocket) return;
	
	// 재연결 시도 (3회 해보고 않되면 오류)
	if (this.connectCnt >= 3) {
		this.callBack(this.generateErrMsg("9001", "통신상태가 원활하지 않아 연결에 실패하였습니다."));	
	} else {
		this.connect(this.wssUrl);
	}
};

/**
 * 일반적인 메시지 생성
 */
SFrameChat.prototype.generateMsg = function(type, data, desUserId) {
	if (desUserId == undefined) {
		desUserId = "";
	}
	
	const msg = {
		'type' : type, 
		'datatype' : 'json', 
		'data' : data, 
		'userId' : this.userId,
		'roomId' : this.roomId,
		'desUserId' : desUserId,
		'_msgId' : this.getUUID()
	};
	
	return msg;
};

/**
 * 에러메시지 생성
 */
SFrameChat.prototype.generateErrMsg = function(errCode, errMsg) {
	const msg = {
		'type' : this.TYPE_ERR, 
		'datatype' : 'json', 
		'data' : {}, 
		'userId' : this.userId,
		'roomId' : this.roomId,
		'errCode' : errCode,
		'errMsg' : errMsg
	};
	
	return msg;
};

/**
 * PING 인터발 초기화
 */
SFrameChat.prototype.clearCheckPing = function() {
	if (this.webSocketInterval) {
		clearInterval(this.webSocketInterval);
		this.webSocketInterval = null;
		this.checkPingCount = 0;
	}
};

/**
 * UUID 생성
 */
SFrameChat.prototype.getUUID = function() {
	var s4 = function() {
		return ((1+Math.random()) * 0x10000 | 0).toString(16).substring(1);
	};
	
	return s4() + s4() +   '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
};

/**
 * 파라메터 읽기
 */
SFrameChat.prototype.getParameter = function(param) {
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
};

SFrameChat.prototype.close = function() {
	if (this.webSocket) {
		this.webSocket.close();
		this.webSocket = null;
	}
};