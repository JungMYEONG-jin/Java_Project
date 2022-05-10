/**
 * auth    : yujin
 * file    : m2soft_asevent.js
 * create  : 2016-11-15
 * modify  : 2016-12-15
 * version : 1.0.10
 * desc    : Application Server API, Event Manage
 */

// 브라우저별 Websocket 변수화 (크롬, 파이어폭스)
window.WebSocket = window.WebSocket || window.mozWebSocket;

var AS_URL;
var ASConnection = null;    // 어플리케이션 서버 소켓 객체
var ASRetryConnect = false; // 어플리케이션 서버 재연결 여부
var ASRetryCount = 0;       // 어플리케이션 서버 재연결 횟수
var ASRetryTime = 5 * 1000; // 어플리케이션 서버 재연결 시간 (초)

/**
 * @ desc  : Application Server 연결 메소드
 *           ASConnection(전역 변수) 변수에 WebSocket 초기화
 */
var AS_Connect = function() {
	console.info('%c AS_Connect', 'color:blue; font-weight:bold;');

	AS_URL = login_user_config.appserver;
	
	if(ASConnection != null) {
		ASConnection.close();
		ASConnection = null;
	}

	try {
		// 어플리케이션 서버 Websocket 연결	
		console.log("application server connect url : " + AS_URL);
		ASConnection = new WebSocket(AS_URL);
	} catch(e) {
		AS_Reconnect();
		console.error(e);
		return;
	}

	// 어플리케이션 서버 콜백 메소드 등록
	ASConnection.onopen = onAS_Open;
	ASConnection.onerror = onAS_Error;
	ASConnection.onclose = onAS_Close;
	ASConnection.onmessage = onAS_Message;
}

/**
 * @ desc  : Application Server 연결 종료 메소드
 */
var AS_Disconnect = function() {
	console.info('%c AS_Disconnect', 'color:blue; font-weight:bold;');
	
    if(ASConnection != null) {
	    ASConnection.close();
		ASConnection = null;
	}
}

/**
 * @ desc  : Application Server 메시지 송신 메소드
 * @ param : mesg = {
 *               'mesg_type': "xx",
 *               'mesg_data': {
 *                   'xx': "xx"
 *                    ...
 *               }
 *           }
 */
var AS_SendMessage = function(mesg) {
	console.info('%c AS_SendMessage (' + mesg.mesg_type + ')', 'color:blue; font-weight:bold;');
	
	try {
		ASConnection.send(JSON.stringify(mesg));
	} catch(e) {
		console.error('Send Message Error : ' + e);
	}
}

/**
 * @ desc  : Application Server 재접속 메소드
 */
var AS_Reconnect = function() {
	console.info('%c AS_Reconnect', 'color:blue; font-weight:bold;');
	
	if(ASRetryConnect == true) {
		return;
	}

	ASRetryConnect = true;
	ASRetryCount += 1;

	if(ASRetryCount > 3) {
	    // 어플리케이션 서버 재연결 횟수 초과, 연결 종료 처리
		if( login_user_config.user_type == USER_CONSULTANT){
			changeUI('disconnectASPopup');
		}else{
			exitConsult('disconnectAS');
		}
		return;
	} else { 
		// 어플리케이션 서버 연결 끊어졌을때 UI 처리
	}

	if(ASConnection != null) {
		ASConnection.close();
		ASConnection = null;
	}

	setTimeout(function() {
		AS_Connect();
		ASRetryConnect = false;
	}, ASRetryTime);
}

/**
 * @ desc : Application Server open 콜백 메소드
 */
var onAS_Open = function() {
	console.info('%c onAS_Open', 'color:green; font-weight:bold;');
	
	ASRetryConnect = false;
	if(ConfigASCallback.events.open != null) {
		ConfigASCallback.events.open();
	}
}

/**
 * @ desc : Application Server error 콜백 메소드
 * @ param : error
 */
var onAS_Error = function(event) {
	console.info('%c onAS_Error', 'color:green; font-weight:bold;');
	console.error("AS Error Event : ", event);

    if(ASConnection != null) {
		AS_Reconnect();
	}
	if(ConfigASCallback.events.error != null) {
		ConfigASCallback.events.error();
	}
}

/**
 * @ desc : Application Server close 콜백 메소드
 * @ param : event
 */
var onAS_Close = function(event) {
	console.info('%c onAS_Close', 'color:green; font-weight:bold;');
	console.error("AS Close Event : ", event);

	if(ASConnection != null) {
		AS_Reconnect();
	}
	if(ConfigASCallback.events.close != null) {
		ConfigASCallback.events.close();
	}
}

/**
 * @ desc : Application Server message 콜백 메소드
 * @ param : application_mesg = {
 *               'mesg_type': "xx",
 *               'mesg_data': {
 *                    ...
 *               }
 *           }
 */
var onAS_Message = function(application_mesg) {
	console.info('%c onAS_Message', 'color:green; font-weight:bold;');
	
    var mesg;
	try {
		/** 메시지 파싱 **/
		mesg = JSON.parse(application_mesg.data);
	} catch(e) {
		console.error('Message Parsing Error : ' + e);
		return;
	}
	console.log("Message Type : " + mesg.mesg_type);
	
	/*
	// 메시지 타입에 따른 처리
	switch( mesg.mesg_type ) {
		case 'connect' : // 유저 접속 완료 메시지
		{
			console.log(mesg.mesg_data);
			break;
		}
		case 'disconnect' : // 유저 접속 종료 메시지
		{
		    // 룸 내 상대방 접속 종료
			console.log(mesg.mesg_data);
			break;
		}
		case 'consulting_status' : // 영상상담 인증 진행 상태 메시지
		{
		    // 상담원: 고객 응답 수신 후 UI 처리
		    // 고객: 상담원 진행에 따른 UI 처리, res 송신
			console.log(mesg.mesg_data);
			break;
		}
		case 'approval_result : // 영상 상담 결과
		{
		    break;
		}
	}
	*/
	
	if(ConfigASCallback.events.message != null) {
		ConfigASCallback.events.message(mesg);
	}
}

/**
 * @ desc : Application Server 연결 메시지 전송 메소드
 */
var AS_Mesg_Connect = function( room_id, user_type , user_id, user_name ) {
	console.info('%c AS_Mesg_Connect', 'color:blue; font-weight:bold;');

    AS_SendMessage({ 
		"mesg_type": "connect",
		"mesg_data": {
			"room_id": room_id,
		    "user_type": user_type,
			"user_id": user_id,
			"user_name": user_name
		}
	});
	ASRetryCount = 0;	
	ASRetryConnect = false;
}

/**
 * @ desc : Application Server 영상인증 상태 변경 메시지 전송 메소드
 * @ param : data_type   ( "req" | "res" )
 * @ param : status      ( 1 ~ 6 )
 */
var AS_Mesg_ConsultingStatus = function(type, status, user_name) {
	console.info('%c AAS_Mesg_ConsultingStatus : ' + status, 'color:blue; font-weight:bold;');
	
	var data_type = type;
	
	AS_SendMessage({ 
		"mesg_type": "consulting_status",
		"mesg_data": {
			"user_name": user_name,
		    "data_type": data_type,
			"status":  status
		}
	});
}

/**
 * @ desc : Application Server 네트워크 상태 기록 메시지 전송 메소드
 *          PeerConnection 객체로 수집되는 video 데이터를 추출하여 전송
 * @ param : status = {
 *               'mesg_type': "network_status",
 *               'mesg_data': {
 *                   'send': { [상담원의 경우 send 데이터 공란 ""]
 *                       'bps': ...,
 *                       'pktlost': ..., 
 *                       'rtt': ..., 
 *                   },
 *                   'recv': {
 *                       'bps': ..., 
 *                       'pktlost': ..., 
 *                       'currentdelay': ..., 
 *                   }
 *               }
 *           }
 */
var AS_Mesg_NetworkStatus = function(status) {
	console.info('%c AS_Mesg_NetworkStatus : ', 'color:blue; font-weight:bold;');
	console.info(status);
	
	AS_SendMessage({ 
		"mesg_type": "network_status",
		"mesg_data": {
		    "send": (login_user_config.user_type == USER_CONSULTANT) ? "" : {
			    "bps": status.send.bps,
				"pktlost": status.send.pktlost,
				"rtt": status.send.rtt
			},
			"recv": {
			    "bps": status.recv.bps,
				"pktlost": status.recv.pktlost,
				"currentdelay": status.recv.currentdelay
			}
		}
	});
}

/**
 * @ desc : Application Server 영상인증 결과 전송 메소드
 * @ param : data_type   ( "req" | "res" )
 * @ param : result      ( true | false )
 */
var AS_Mesg_ApprovalResult = function(type, result) {
	console.info('%c AAS_Mesg_ApprovalResult : ' + result, 'color:blue; font-weight:bold;');
	
	var data_type = type;
	
	AS_SendMessage({
		"mesg_type": "approval_result",
		"mesg_data": {
		    "data_type": data_type,
			"result":  result
		}
	});
}

/**
 * @ desc : Application Server 영상인증 중 고객쪽에 상담사 메세지 전달
 * @ param : data_type   ( "requestMsg" )
 * @ param : msg      ( request message )
 */
var AS_Mesg_RequestMsg = function(type, msg) {
	console.info('%c AS_Mesg_RequestMsg : ' + msg, 'color:blue; font-weight:bold;');
	
	var data_type = type;
	
	AS_SendMessage({
		"mesg_type": "request_msg",
		"mesg_data": {
			"data_type": data_type,
			"request_msg":  msg
		}
	});
}

/**
 * @ desc : 상담원 이미지 전송 메소드
 * @ param : user_name  (상담원 이름)
 * @ param : img_data   (base64로 인코딩된 상담원 이미지 데이터)
 */
var AS_Mesg_ConsultantImg = function(img_data) {
	console.info('%c AS_Mesg_ConsultantImg', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": "consultant_img",
		"mesg_data": {
		    "img": img_data
		}
	});
}

/**
 * @ desc : 호 연결 거절 메시지
 */
var AS_Mesg_SIP_Decline = function(room_id, user_type , user_id) {
	console.info('%c AS_Mesg_SIP_Decline', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": "sip_decline",
		"mesg_data": {
			"room_id": room_id,
		    "user_type": user_type,
			"user_id": user_id,
		}
	});
}

/**
 * @ desc : 호 연결 종료 메시지
 */
var AS_Mesg_SIP_Bye = function(room_id, user_type , user_id) {
	console.info('%c AS_Mesg_SIP_Bye', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": "sip_bye",
		"mesg_data": {
			"room_id": room_id,
		    "user_type": user_type,
			"user_id": user_id,
		}
	});
}

/**
 * @ desc : 호 연결 종료 메시지
 */
var AS_Mesg_Declare = function(room_id, user_type , user_id) {
	console.info('%c AS_Mesg_Declare', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": "declare",
		"mesg_data": {
			"room_id": room_id,
			"user_type": user_type,
			"user_id": user_id
		}
	});
}

/**
 * @ desc : 유저 영상통화 페이지 이탈/재참여 상태 메시지
 */
var AS_Mesg_User_Away_OR_Comeback = function(type, room_id, user_type , user_id) {
	console.info('%c AS_Mesg_User_Away_OR_Comeback', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": type,
		"mesg_data": {
			"room_id": room_id,
		    "user_type": user_type,
			"user_id": user_id,
		}
	});
}

/**
 * @ desc : 상담원의 영상 상담 요청 타임아웃 메시지 (고객이 송신)
 */
var AS_Mesg_Accept_Timeout = function(type, room_id, user_type , user_id) {
	console.info('%c AS_Mesg_Accept_Timeout', 'color:blue; font-weight:bold;');
	
	AS_SendMessage({
		"mesg_type": type,
		"mesg_data": {
			"room_id": room_id,
		    "user_type": user_type,
			"user_id": user_id,
		}
	});
}