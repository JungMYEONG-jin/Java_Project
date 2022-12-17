/**
 * auth    : jmkim
 * file    : m2soft_sipevent.js
 * create  : 2016-11-15
 * modify  : 2016-12-27
 * version : 1.0.11
 * desc    : SIP API, Event 관리
 */

var oSipStack, oSipSessionRegister, oSipSessionCall;

/**
 * @ desc  : SIP Stack 초기화 메소드
 */
var SIP_Initialize = function(config) {
	console.info('%c SIP_Initialize', 'color:blue; font-weight:bold;');
	
	// SIP STACK 시작
	SIP_STATUS = 0;
	// SIP STACK 타이머 시작
	timeoutDocumentReady(false);
    // SIP INIT 타이머 시작 
	timeoutInitialize(true);
	
	SIPml.setDebugLevel("info");
	SIPml.b_initialized = false;
    SIPml.b_initializing = false;
	SIPml.init(function() {
		oSipStack = new SIPml.Stack(config);
		
		if (oSipStack.start() != 0) {
	        console.error("SIP Stack Initialize Error");
			if(ConfigSipCallback.failed_callbacks.initialize != null) {
			    ConfigSipCallback.failed_callbacks.initialize();
			}
		} else {
	        console.info('%c SIP_Initialize Success', 'color:gray; font-weight:bold;');
		}
	}, function(e) {
	    console.error("SIP Stack Initialize Error : " + e.description);
		if(ConfigSipCallback.failed_callbacks.initialize != null) {
			ConfigSipCallback.failed_callbacks.initialize(e);
		}
	});
}

/**
 * @ desc : SIP Register 메소드
 */
var SIP_Register = function(config) {
	console.info('%c SIP_Register', 'color:blue; font-weight:bold;');
	
	// SIP INIT 타이머 종료 
	timeoutInitialize(false);
	try {
		oSipSessionRegister = oSipStack.newSession('register', config);
		oSipSessionRegister.register();
		
		console.info('%c SIP_Register Success', 'color:gray; font-weight:bold;');
	} catch (e) {
		console.error("SIP Register Error : "+ e);
		if(ConfigSipCallback.failed_callbacks.register != null) {
			ConfigSipCallback.failed_callbacks.register(e);
		}
	}
}

/**
 * @ desc : SIP Unregister 메소드
 */
var SIP_Unregister = function() {
	console.info('%c SIP_Unregister', 'color:blue; font-weight:bold;');
	
	try {
		if(oSipSessionRegister) {
			oSipSessionRegister.unregister();
			console.info('%c SIP_Unregister Success', 'color:gray; font-weight:bold;');
		}
	} catch(e) {
	    console.error("SIP SIP_Unregister Error : "+ e);
	}
}

/**
 * @ desc : SIP Invite 메소드
 */
var SIP_Invite = function(callee_sip_id, config) {
	console.info('%c SIP_Invite', 'color:blue; font-weight:bold;');
	
	try {
		oSipSessionCall = oSipStack.newSession(call_type, config);
		if (oSipSessionCall.call(callee_sip_id) != 0) {
			// Invite 실패
			console.error("SIP Invite Error");
			oSipSessionCall = null;
			if(ConfigSipCallback.failed_callbacks.invite != null) {
			    ConfigSipCallback.failed_callbacks.invite();
		    }
			return;
		} else {
			// Invite 성공
	        console.info('%c SIP_Invite Success', 'color:gray; font-weight:bold;');
			if(ConfigSipCallback.success_callbacks.invite != null) {
			    ConfigSipCallback.success_callbacks.invite();
		    }
			// consultant mic capture start - 20161129 jmkim
		    if(login_user_config.user_type == USER_CONSULTANT) {
		    	ConsultantAudioCapture();
		    }
		}
	} catch(e) {
	    console.error("SIP Invite Error : " + e);
		if(ConfigSipCallback.failed_callbacks.invite != null) {
			ConfigSipCallback.failed_callbacks.invite(e);
		}
	}
}

/**
 * @ desc : SIP Accept 메소드
 */
var SIP_Accept = function(Recv_SipCallSession) {
	console.info('%c SIP_Accept', 'color:blue; font-weight:bold;');
	
	try {
		oSipSessionCall = Recv_SipCallSession;
		oSipSessionCall.accept(oConfigCall);
		timeoutIceGathering(true);
		
		console.info('%c SIP_Accept Success', 'color:gray; font-weight:bold;');
		if(ConfigSipCallback.success_callbacks.accept != null) {
			ConfigSipCallback.success_callbacks.accept();
		}
	} catch(e) {
	    console.error("SIP Accept Error : "+ e);
		if(ConfigSipCallback.failed_callbacks.accept != null) {
			ConfigSipCallback.failed_callbacks.accept(e);
		}
	}
}

/**
 * @ desc : SIP Reject 메소드
 */
var SIP_Reject = function(Recv_SipCallSession) {
	console.info('%c SIP_Reject', 'color:blue; font-weight:bold;');

	try {
		Recv_SipCallSession.reject();
		
		console.info('%c SIP_Reject Success', 'color:gray; font-weight:bold;');
		if(ConfigSipCallback.success_callbacks.reject != null) {
			ConfigSipCallback.success_callbacks.reject();
		}
	} catch(e) {
	    console.error("SIP Reject Error : " + e);
		if(ConfigSipCallback.failed_callbacks.reject != null) {
			ConfigSipCallback.failed_callbacks.reject(e);
		}
	}
}

/**
 * @ desc : SIP Bye 메소드
 */
var SIP_Bye = function() {
	console.info('%c SIP_Bye', 'color:blue; font-weight:bold;');
	
	try {
		if (oSipSessionCall) {
			oSipSessionCall.hangup({
				events_listener: { 
					events: '*', 
					listener: onSipEventSession
				}
			});
			
			console.info('%c SIP_Bye Success', 'color:gray; font-weight:bold;');
			if(ConfigSipCallback.success_callbacks.bye != null) {
			    ConfigSipCallback.success_callbacks.bye();
		    }
		}
	} catch(e) {
	    console.error("SIP Bye Error : " + e);
		if(ConfigSipCallback.failed_callbacks.bye != null) {
			ConfigSipCallback.failed_callbacks.bye(e);
		}
	}
}

/**
 * @ desc : SIP Stack Release 메소드
 */
var SIP_Release = function() {
	console.info('%c SIP_Release', 'color:blue; font-weight:bold;');
	
	try {
	    if(oSipStack) {
		    if(oSipStack.stop() != 0) { 
	            console.error("SIP Release Error : "+ e);
			} else {
			    oSipStack = null;
	            console.info('%c SIP_Release Success', 'color:gray; font-weight:bold;');
			}
		}
	} catch(e) {
	    console.error("SIP Release Error : "+ e);
	}
}

/**
 * @ desc : SIP Stack Initialized 콜백 메소드
 */
var onSIP_Initialized = function(event) {
	console.info('%c onSIP_Initialized', 'color:blue; font-weight:bold;');
	
	timeoutInitialize(false);
	//txt_sip_state.innerHTML = "SIP STACK Initialized";
	if(ConfigSipCallback.success_callbacks.initialize != null) {
		ConfigSipCallback.success_callbacks.initialize();
	}
}

/**
 * @ desc : SIP Stack Stopped 콜백 메소드
 */
var onSIP_Stopped = function(event) {
	console.info('%c onSIP_Stopped', 'color:blue; font-weight:bold;');
    console.info("onSIP_Stopped");

	oSipStack = null;
	oSipSessionRegister = null;
	oSipSessionCall = null;
	
	switch(event.type) {
		case 'stopped' :
	    {	
			break;
		}
		case 'failed_to_start' :
		{
			/*
		    // SIP Stack 초기화 실패 재 초기화
			SIPml.b_initialized = false;
			SIPml.b_initializing = false;

			SIP_Initialize(oConfigStack);
			*/
			break;
		}
	}
	
	//txt_sip_state.innerHTML = "SIP STACK Stopped";
	if(ConfigSipCallback.events.stopped_stack != null) {
		ConfigSipCallback.events.stopped_stack(event);
	}
}

/**
 * @ desc : SIP Registered 콜백 메소드
 */
var onSIP_Registered = function(event) {
	console.info('%c onSIP_Registered', 'color:blue; font-weight:bold;');

	//txt_sip_state.innerHTML = "SIP Registered";
	if(ConfigSipCallback.success_callbacks.register != null) {
		ConfigSipCallback.success_callbacks.register();
	}
}

/**
 * @ desc : SIP Unregistered 콜백 메소드
 */
var onSIP_Unregistered = function(event) {
	console.info('%c onSIP_Unregistered : ' + event.description, 'color:blue; font-weight:bold;');
	
	switch(event.description) {
		case 'Unauthorized' :
		{
			/*
			if( login_user_config.user_type == USER_CONSULTANT){
				changeUI('unauthorizedSipIdPopup');
			}else{
				exitConsult('unauthorizedSipId');
			}
			*/
			if(ConfigSipCallback.events.authorize_register != null) {
				ConfigSipCallback.events.authorize_register();
			}
			break;
		}
	}
}

/**
 * @ desc : SIP Call Received 콜백 메소드
 */
var onSIP_CallReceived = function(event) {
	console.info('%c onSIP_CallReceived', 'color:blue; font-weight:bold;');

	if (oSipSessionCall) {
		// 이미 통화 중
		SIP_Reject(event.newSession);
	} else {
	    // 통화 수락
		//SIP_Accept(event.newSession);
		
		// TEST
		oSipSessionCall = event.newSession;
	    //txt_sip_state.innerHTML = "SIP Call Received : " + oSipSessionCall.getRemoteFriendlyName();
		if(ConfigSipCallback.events.receiving_call != null) {
		    ConfigSipCallback.events.receiving_call(oSipSessionCall);
	    }
	}
}

/**
 * @ desc : SIP ICE Gathering Completed 콜백 메소드 (완료 후 Invite, 200 OK 메시지 송신) 
 */
var onSIP_IceGatheringCompleted = function() {
	console.info('%c onSIP_IceGatheringCompleted', 'color:blue; font-weight:bold;');
	
	/*
	if(xfinger_session.o_mgr.b_started) {
		if(ConfigSipCallback.events.connected_call != null) {
			ConfigSipCallback.events.connected_call(event);
		}
	}
	*/
}

/**
 * @ desc : SIP Call Connected 콜백 메소드
 */
var onSIP_CallConnected = function(event) {
	console.info('%c onSIP_CallConnected', 'color:blue; font-weight:bold;');

	var ConnectedTimer = setInterval(function() {
        // console.log("iceConnectedState : " + xfinger_session.o_pc.iceConnectionState);
        if(xfinger_session.o_pc.iceConnectionState == "connected") {
            if(ConfigSipCallback.events.connected_call != null) {
                ConfigSipCallback.events.connected_call();
            }
            
            if(periodic_stats == null) {
            	last_send_bps = 0;
                last_recv_bps = 0;
                threshold_count = 0;
                periodic_stats = setInterval(function() {
                    getStatCheck(xfinger_session);
                }, getStatTime);
            }
            clearInterval(ConnectedTimer);
        }
    }, 100);
}

/**
 * @ desc : SIP Call Disconnected 콜백 메소드
 */
var onSIP_CallDisconnected = function(event) {
	console.info('%c onSIP_CallDisconnected : ' + event.description, 'color:blue; font-weight:bold;');

	/* Event guide
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
	    case 'Decline' :             // Other user reject
	    {
	        break;
	    }
	    case 'Not found' :           // Not found user, Time out(30sec)
	    {
	        break;
	    }
	}
	*/

	//txt_sip_state.innerHTML = "SIP Call Disconnected";
	if(ConfigSipCallback.events.disconnected_call != null) {
		ConfigSipCallback.events.disconnected_call(event);
	}
}

/**
 * @ desc : SIP Stack 이벤트 콜백 메소드
 */
var onSipEventStack = function(e) {
	console.info('%c Receive SIP Stack Event: ' + e.type, 'color:green; font-weight:bold;');

	switch (e.type) {
		case 'starting': break;
		case 'started':
		{
			onSIP_Initialized(e);
			break;
		}
		case 'stopping': case 'stopped': case 'failed_to_start': case 'failed_to_stop':
		{
			onSIP_Stopped(e);
			break;
		}
		case 'i_new_call':
		{
		    onSIP_CallReceived(e);
			break;
		}
		case 'm_permission_requested':
		{
		    // 미디어 사용 권한 요청 
			break;
		}
		case 'm_permission_accepted':
		{
		    // 미디어 사용 권한 획득
			break;
		}
		case 'm_permission_refused':
		{
		    // 미디어 사용 권한 거절
			break;
		}
		default: break;
	}
};

/**
 * @ desc : SIP Session 이벤트 콜백 메소드
 */
var onSipEventSession = function(e) {
	console.info('%c Receive SIP Session Event : ' + e.type, 'color:green; font-weight:bold;');

	switch (e.type) {
		case 'connecting': break;
		case 'connected':
		{	
			if (e.session == oSipSessionRegister) {
				onSIP_Registered(e);
			} else if(e.session == oSipSessionCall) {
				onSIP_CallConnected(e);
			}
			break;
		}
		case 'terminating': break;
		case 'terminated':
		{
			if (e.session == oSipSessionRegister) {
				onSIP_Unregistered(e);
			} else if (e.session == oSipSessionCall) {
				onSIP_CallDisconnected(e);
			}
			break;
		}
		case 'm_stream_video_local_added':
		{
			if (e.session == oSipSessionCall) {
			}
			break;
		}
		case 'm_stream_video_local_removed':
		{
			if (e.session == oSipSessionCall) {
			}
			break;
		}
		case 'm_stream_video_remote_added':
		{
			if (e.session == oSipSessionCall) {
			}
			break;
		}
		case 'm_stream_video_remote_removed':
		{
			if (e.session == oSipSessionCall) {
			}
			break;
		}

		case 'm_stream_audio_local_added':
		case 'm_stream_audio_local_removed':
		case 'm_stream_audio_remote_added':
		case 'm_stream_audio_remote_removed':
		{
			break;
		}
		case 'i_ao_request':
		{
			if(e.session == oSipSessionCall){
				var iSipResponseCode = e.getSipResponseCode();
				
				switch(iSipResponseCode) {
					case 100 :
					{
						console.log("100 Giving a try");
						xfinger_session.call_response = 100;
						break;
					}
					case 180 :
					case 183 :
					{
						console.log("180 Ringing");
						xfinger_session.call_response = 180;
						break;
					}
				}
			}
			break;
		}
		case 'm_early_media':
		{
			if(e.session == oSipSessionCall){
				console.log("Early media started");
			}
			break;
		}
		case 'm_local_hold_ok':
		{
			if(e.session == oSipSessionCall){
				if (oSipSessionCall.bTransfering) {
					oSipSessionCall.bTransfering = false;
					this.AVSession.TransferCall(this.transferUri);
				}

				console.log("Call placed on hold");
				oSipSessionCall.bHeld = true;
			}
			break;
		}
		case 'm_local_hold_nok':
		{
			if(e.session == oSipSessionCall){
				oSipSessionCall.bTransfering = false;
				console.log("Failed to place remote party on hold");
			}
			break;
		}
		case 'm_local_resume_ok':
		{
			if(e.session == oSipSessionCall){
				oSipSessionCall.bTransfering = false;
				console.log("Call taken off hold");
				oSipSessionCall.bHeld = false;

				if (SIPml.isWebRtc4AllSupported()) {
				}
			}
			break;
		}
		case 'm_local_resume_nok':
		{
			if(e.session == oSipSessionCall){
				oSipSessionCall.bTransfering = false;
				console.log("Failed to unhold call");
			}
			break;
		}
		case 'm_remote_hold':
		{
			if(e.session == oSipSessionCall){
				console.log("Placed on hold by remote party");
			}
			break;
		}
		case 'm_remote_resume':
		{
			if(e.session == oSipSessionCall){
				console.log("Taken off hold by remote party");
			}
			break;
		}
		case 'i_notify':
		{
			break;
		}
		case 'i_info':
		{
		    break;
		}
		default: break;
	}
}
