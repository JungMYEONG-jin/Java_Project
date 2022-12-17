/**
 * 서버로부터 가져와야하는 정보들.
 */

var init = function() {
	console.log(' # init()');
	
	oConfigStack.realm = login_user_config.realm;
	oConfigStack.impi = login_user_config.sip_id;
	oConfigStack.impu = "sip:" + login_user_config.sip_id + "@" + login_user_config.realm;
	oConfigStack.sipid = login_user_config.sip_id;
	oConfigStack.password = login_user_config.sip_password;
	oConfigStack.ice_servers = '[{ url:"turn:'+login_user_config.turnserver+'", username:"' + login_user_config.turn_username + '", credential:"' + login_user_config.turn_password + '"}]';
	oConfigStack.websocket_proxy_url= login_user_config.webrtcgw;
	oConfigStack.outbound_proxy_url= login_user_config.sipproxy;

	setSipCallbackFunctions();
	
	// jmkim - 20161205
	var start = function() { // jmkim - 20161125
	    if(consultInfoObject.appType == 'ios') {
	        var iosLoadInterval = setInterval(function() {
	            if(app.loadedComplete == true) {
	                SIP_Initialize(oConfigStack);
	                clearInterval(iosLoadInterval);
	            }
	        }, 100);
	    } else {
	        SIP_Initialize(oConfigStack);
	    }
	};
	
	// jmkim - 20161205
	hasMediaDevice(start);
}

var registerUser = function(){
	console.log(' # registerUser()');
	SIP_Register(oConfigRegister);
	timeoutRegister(true);
	
//	if(login_user_config.user_type == USER_CUSTOMER){
//		//로딩후 15초후에 Sip id가 레지가 되지않은 상태일 경우, 통화가 불가능하다고 판단하고 상담취소처리를 한다.
//		setTimeout(function() { 
//			if( consultInfoObject.state == STATE_BEFORE_CALL ) {
//				exitConsult('failToRegisterSipId'); timeoutRegister(false);
//			} 
//		}, READYCALL_TIMEOUT * 1000 );
//	} 
	
	setTimeout(function() {
		if(login_user_config.user_type == USER_CONSULTANT) {
			if( consultInfoObject.state == STATE_BEFORE_CALL ) {
				changeUI('failToRegisterSipId');
				timeoutRegister(false);
			}
		} else {
			if( consultInfoObject.state == STATE_BEFORE_CALL ) {
				exitConsult('failToRegisterSipId');
				timeoutRegister(false);
			} 
		}
	}, READYCALL_TIMEOUT * 1000 );
}

var callToCustomer = function( customer_id ){
	//alert('register complete! invite to customer!');
	console.log(' # callToCustomer('+customer_id+')');
	oConfigCall.video_local = document.getElementById("video_local");
	oConfigCall.video_remote = document.getElementById("video_remote");
	SIP_Invite( customer_id , oConfigCall );
	
	consultInfoObject.state = STATE_SEND_CALL;
	timeoutInvite(true);
}

var confirmCall = function( result ){
	console.log(' # acceptCall()');
	if( result == true ){
		//수락했을 경우, 전화 승락
		if( login_user_config.user_type = USER_CUSTOMER ){
			consultInfoObject.state = STATE_START_CALL;
		}
		
		oConfigCall.video_local = document.getElementById("video_local");
		oConfigCall.video_remote = document.getElementById("video_remote");
		SIP_Accept( oSipSessionCall );
		changeUI('connecting');
	} else {
		exitConsult('declineConsultantCall');
	}
}

var setSipCallbackFunctions = function(){
	console.log(' # setSipCallbackFunctions()');
	console.log(' # user type = '+login_user_config.user_type);
	if( login_user_config.user_type == USER_CONSULTANT ){
		console.log(' # set consultant\'s function');
		// when init complete, execute registerUser
		ConfigSipCallback.success_callbacks.initialize = registerUser;
		
		// when register complete, execute callToCustomer
		ConfigSipCallback.success_callbacks.register = function(){
			//connect to application server
			timeoutRegister(false);
			setASCallbackFunctions();
		    AS_Connect( login_user_config.user_type );
		};
		
		// when register complete, execute callToCustomer
		ConfigSipCallback.events.connected_call = function(){
			// Invite 타임아웃 종료
			timeoutInvite(false);
			
			// consultant camera off - 20161130 jmkim
			muteWebCam(true);
			
		    //change to call UI
			changeUI('connectCall');
			
			// consultant recording start - 20161129 jmkim
			var recording_start_interval = setInterval(function() {
				if(xfinger_session.o_remote_stream) {
					RecordingStart();
					clearInterval(recording_start_interval);
				}
			}, 100);
			
		};
		
		// when disconnectedcall - 20161129 jmkim
		ConfigSipCallback.events.disconnected_call = function(event) {
			clearInterval( consultInfoObject.intervalEventId );
			switch(event.description) {
	            case 'Call terminating...' : // User close
	            {
	                break;
	            }
	            case 'Call terminated' :     // Other user close
	            {
	                break;
	            }
	            case 'Transport error' :     // Not found user(30sec)
	            {
	                break;
	            }
	            case 'Not Found' :           // Not found user, Time out(30sec)
	            {
	                changeUI('cancelPopup');
	                break;
	            }
			}
		};
		
		ConfigSipCallback.events.authorize_register = function() {
			timeoutRegister(false);
			if(consultInfoObject.state == STATE_BEFORE_CALL) {
				changeUI('unauthorizedSipIdPopup');
			}
		};
		
		ConfigSipCallback.events.stopped_stack = function(event) {
			if(ASConnection == null) {
				SIP_Initialize(oConfigStack);
			}
		}
	} else {
		console.log(' # set customer\'s function');
	
		// when complete initialize, execute registerUser
		ConfigSipCallback.success_callbacks.initialize = registerUser;
		
		// when complete register, update req_key state.
		ConfigSipCallback.success_callbacks.register = function() {
			timeoutRegister(false);
			if(xfinger_session && xfinger_session.o_pc) {
				// 재 연결 시나리오 
				if(xfinger_session.o_pc.iceConnectionState == "failed" 
		    	|| xfinger_session.o_pc.iceConnectionState == "disconnected" 
		    	|| xfinger_session.o_pc.iceConnectionState == "closed") {
    				// 재 연결 요청
					sendMessageToAS('user_comeback');
    			}
			} else {
				// 첫 연결 시나리오 
				updateCallReadyStateToWAS( consultInfoObject.req_key );
			}
		};
		
		// when recieve call, execute acceptCall
		ConfigSipCallback.events.receiving_call = function() {
			if(ASConnection) {
				if( confirm('상담사와 연결이 끊어졌습니다. 재연결 하시겠습니까?') ){
					SIP_Accept( oSipSessionCall );
				}else{
					consultInfoObject.state = STATE_CANCEL_CALL;
					exitConsult('declineConsultantCall');
				}
			} else {
			    //connect to application server - 20161205 - jmkim
			    setASCallbackFunctions();
		        AS_Connect( login_user_config.user_type );
			}
		}
		
		// when connected_call complete, execute connectAS & changeUI
		ConfigSipCallback.events.connected_call = function(){
		    
		    //change to call UI
			changeUI('connectCall');
		};

		// disconnected_call
		ConfigSipCallback.events.disconnected_call = function() {
			//send result and close xfinger web view
			if( consultInfoObject.state != STATE_CANCEL_CALL ){
				if( consultInfoObject.approvalResult == null ){
					cancelConsult();
				}
			}
		}

		ConfigSipCallback.events.stopped_stack = function(event) {
			if(event.type == 'stopped') {
				// 영상 상담중 재 연결 중 스택 초기화 실패 시 스택 초기화
				if(xfinger_session && xfinger_session.o_pc
				&&(xfinger_session.o_pc.iceConnectionState == "failed"
	            || xfinger_session.o_pc.iceConnectionState == "disconnected"
			    || xfinger_session.o_pc.iceConnectionState == "closed")) {
				    SIP_Initialize(oConfigStack);
				// 첫 진입 후 SIP 스택 초기화 실패시 스택 초기화
				} else if(ASConnection == null) {
					SIP_Initialize(oConfigStack);
				}
			}
		}
		
		ConfigSipCallback.events.authorize_register = function() {
			timeoutRegister(false);
			if(consultInfoObject.state == STATE_BEFORE_CALL) {
				exitConsult('unauthorizedSipId');
			}
		};
	}
}
