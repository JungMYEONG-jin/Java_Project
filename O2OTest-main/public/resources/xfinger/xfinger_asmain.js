/**
 * application으로 메시지보내기
 * @param msg_type
 */
function sendMessageToAS( msg_type ){
	console.log(' # sendMessageToAS('+msg_type+')');
	switch ( msg_type ) {
		case 'login':
		{
			console.log(' # login_user_config.user_type='+login_user_config.user_type);
			console.log(' # login_user_config.sip_id='+login_user_config.sip_id);
			// jmkim 20161129
			var name = (login_user_config.user_type == USER_CONSULTANT) ? consultInfoObject.hwnname : "";
			AS_Mesg_Connect( consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString(), name );
			break;
		}
		case 'consulting_status_req':
		{
			console.log(' # consultInfoObject.currentStep='+consultInfoObject.currentStep);
			AS_Mesg_ConsultingStatus( 'req' , consultInfoObject.currentStep , consultInfoObject.hwnname );
			break;
		}
		case 'consulting_status_res':
		{
			console.log(' # consultInfoObject.currentStep='+consultInfoObject.currentStep);
			AS_Mesg_ConsultingStatus( 'res' , consultInfoObject.currentStep );
			break;
		}
		case 'approval_result_req':
		{
			console.log(' # consultInfoObject.approvalResult='+consultInfoObject.approvalResult);
			AS_Mesg_ApprovalResult( 'req' , consultInfoObject.approvalResult );
			break;
		}
		case 'approval_result_res':
		{
			console.log(' # consultInfoObject.approvalResult='+consultInfoObject.approvalResult);
			AS_Mesg_ApprovalResult( 'res' , consultInfoObject.approvalResult );
			break;
		}
		// 20161201 jmkim
		case 'consultant_img' :
		{
			AS_Mesg_ConsultantImg(consultInfoObject.photo);
			break;
		}
		case 'decline' :
		{
			AS_Mesg_SIP_Decline( consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
		case 'sip_bye' :
		{
			AS_Mesg_SIP_Bye( consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
		//20170117 Teari
		case 'declare' :
		{
			AS_Mesg_Declare( consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
		case 'user_away' :
		{
			AS_Mesg_User_Away_OR_Comeback( msg_type, consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
		case 'user_comeback' :
		{
			AS_Mesg_User_Away_OR_Comeback( msg_type, consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
		case 'accept_timeout' :
		{
			AS_Mesg_Accept_Timeout( msg_type, consultInfoObject.req_key, login_user_config.user_type , login_user_config.sip_id.toString() );
			break;
		}
	}
}
/**
 * application으로 메시지보내기2
 * @param msg_type, msg
 */
function sendMessageToAS2( msg_type , msg){
	console.log(' # sendMessageToAS2('+msg_type+')');
	switch ( msg_type ) {
		case 'request_msg':
		{
			console.log(' # consultInfoObject.requestMsg='+consultInfoObject.requestMsg);
			AS_Mesg_RequestMsg( msg_type, consultInfoObject.requestMsg );
			break;
		}
	}
}

/**
 * AS로부터 Message를 받았을 경우, mesg.mesg_type별로 처리
 * @param mesg
 */
function onRecieveMessage( mesg ){
	console.log(' # onRecieveMessage('+mesg+')');
	
	var mesgSenderType = mesg.mesg_data.user_type;
	var myUserType 	   = login_user_config.user_type;
	switch( mesg.mesg_type ) {
		case 'connect' : // 유저 접속 완료 메시지
		{
			console.log(mesg.mesg_data);
			var loginUserCount = mesg.mesg_data.user_count;
			var roomState      = mesg.mesg_data.room_status;
			
			// 수신한 사용자의 타입에 따라 처리 
			switch( myUserType ) {
			    case USER_CONSULTANT : // 내가 상담원 일때
				{
			    	// 상담원 자신이 입장 후 입장 인원이 2명 이상일 경우 상담진행중으로 퇴장 처리
			    	if(mesg.mesg_data.user_id == consultInfoObject.hwnno && loginUserCount >= 2) {
		    			changeUI('alreadyConsultingPopup');
			    		return;
			    	}
			    	
			    	if ( mesgSenderType == USER_CONSULTANT ) {
				    	if ( loginUserCount == 1 ) {
				    		// 상담원 최초 입장시
				    		if (roomState == -1) {
				    			// 고객에게 영상통화 연결 요청
				    			callToCustomer(consultInfoObject.req_key);
				    		} else {
					    		changeUI('noCustomerPopup');
				    		}
				    	} else if ( loginUserCount == 2 ) {
				    		changeUI('connectCall');
				    	}
			    	} else if ( mesgSenderType == USER_CUSTOMER ) {
			    		// 고객이 최초 연결 완료 했을때 
			    		if(roomState == -1) {
			    			sendMessageToAS('consulting_status_req');
		    				sendMessageToAS('consultant_img'); 
			    		}else{
			    			changeUI('connectCall');
			    		}
			    	}
				    break;
				}
			    case USER_CUSTOMER :  // 내가 고객 일때
				{
			    	// 상담원이 로그인 했을 때
			    	if (mesgSenderType == USER_CONSULTANT) {
				    	if ( loginUserCount == 2 ) {
				    	}
				    // 고객이 로그인 했을 때
			    	}else if (mesgSenderType == USER_CUSTOMER) {
				    	if ( loginUserCount == 1 ){
				    		exitConsult('noConsultant');
				    	} else if ( loginUserCount >= 2 ) {
				    		if (roomState == -1) {
				    			changeUI('confirmCall');
				    		} else {
				    			// user comeback 상태 일때
				    			if(xfinger_session.o_pc 
				    			&&(xfinger_session.o_pc.iceConnectionState == "failed" 
				    			|| xfinger_session.o_pc.iceConnectionState == "disconnected" 
				    			|| xfinger_session.o_pc.iceConnectionState == "closed")) {
				    				// SIP 재 초기화 및 등록 
				    				SIP_Initialize(oConfigStack);
				    			}
				    		}
				    	}
			    	}
			    	break;
				} // end of switch
			}
			
			break;
		}
		case 'disconnect' : // 유저 접속 종료 메시지
		{
		    // 룸 내 상대방 접속 종료
			console.log(mesg.mesg_data);
			switch(login_user_config.user_type) {
			    case USER_CONSULTANT : // 내가 상담원 일때
				{
			    	if( mesgSenderType == USER_CUSTOMER ){
			    		changeUI('disconnectCall');
			    	}
			    	break;
				}
			    case USER_CUSTOMER :  // 내가 고객 일때
				{
			    	if( mesgSenderType == USER_CONSULTANT && consultInfoObject.approvalResult == null ){
			    		exitConsult('noConsultant');
			    	}
			    	break;
				}
			}
			break;
		}
		case 'consulting_status' : // 영상상담 인증 진행 상태 메시지
		{
		    // 상담원: 고객 응답 수신 후 UI 처리
		    // 고객: 상담원 진행에 따른 UI 처리, res 송신
			console.log(mesg.mesg_data);
			if( mesg.mesg_data.data_type == 'req' ){
				//change currentStep value
				consultInfoObject.currentStep	= mesg.mesg_data.status;
				consultInfoObject.hwnname 		= mesg.mesg_data.user_name;
				//응답메세지 전송
				sendMessageToAS( "consulting_status_res" );
				//고객 UI변경
				changeUI( 'step' , {} );
			}else{
				//상담원 UI변경
				changeUI( 'step' , {} );
			}
			break;
		}
		case 'approval_result' : // 영상상담 인증 진행 상태 메시지
		{
			if( mesg.mesg_data.data_type == 'req' ){
				//customer (when receive request from consultant)
				consultInfoObject.approvalResult = mesg.mesg_data.result;
				
				sendMessageToAS( "approval_result_res" );
				
				if( mesg.mesg_data.result == true ){
					exitConsult('approval');
				}else if( mesg.mesg_data.result == false ){
					exitConsult('disapproval');
				}
			}else{
				//consultant (when receive response msg from customer)
				forceExitConsultRoom('approvalButton');
			}
			break;
		}
		case 'request_msg' : // 영상상담 인증 진행 상태 메시지
		{
			//customer (when receive request from consultant)
			consultInfoObject.requestMsg = mesg.mesg_data.request_msg;
			
			if( mesg.mesg_data.request_msg != '' ){
				changeUI('request_msg',mesg.mesg_data.request_msg);
			}
			break;
		}
		case 'consultant_img' : // 상담원 이미지 데이터 메시지
		{
			if(login_user_config.user_type == USER_CUSTOMER) {
//				document.getElementById('consultantImg').setAttribute('src', "data:image/png;base64," + mesg.mesg_data.img);
				document.getElementById('consultantImg').setAttribute('src', mesg.mesg_data.img);
			}
			break;
		}
		case 'sip_decline' : // SIP 호 거절 메시지
		{
			switch(login_user_config.user_type) {
			    case USER_CONSULTANT : // 내가 상담원 일때
				{
			    	// Invite 타임아웃 종료
			    	timeoutInvite(false);
			    	// 고객이 영상 상담 거절
			    	changeUI('cancelPopup');
			    	//AS_Disconnect();
			    	break;
				}
			    case USER_CUSTOMER :  // 내가 고객 일때
				{
			    	break;
				}
			}
			break;
		}
		case 'sip_bye' : // SIP 호 종료 메시지
		{
			switch(login_user_config.user_type) {
			    case USER_CONSULTANT : // 내가 상담원 일때
				{
					//상담 현황으로 이동
					location.href = '/web/viewConsult.do';
			    	break;
				}
			    case USER_CUSTOMER :  // 내가 고객 일때
				{
			    	if(mesgSenderType == USER_CONSULTANT){
			    		consultInfoObject.approvalResult = false;
						exitConsult('consultantExitByButton');
			    	}
			    	break;
				}
			}
			break;
		}
		case 'declare' : // SIP 호 종료 메시지
		{
			switch(login_user_config.user_type) {
				case USER_CONSULTANT : // 내가 상담원 일때
				{
					//상담 현황으로 이동
					location.href = '/web/viewConsult.do';
					break;
				}
				case USER_CUSTOMER :  // 내가 고객 일때
				{
					if(mesgSenderType == USER_CONSULTANT){
						consultInfoObject.approvalResult = false;
						exitConsult('consultantDeclareByButton');
					}
					break;
				}
			}
			break;
		}
		case 'user_away' : // 사용자 영상 상담 이탈 상태
		{
			// 상담원 내부 처리 (수신 비디오 스트림 데이터 체크)
			break;
		}
		case 'user_comeback' : // 사용자 영상 상담 재 접속 상태
		{
			switch(login_user_config.user_type) {
			    case USER_CONSULTANT : // 내가 상담원 일때
				{
			    	// 상담원, 고객 현재 영상 상담 재 가능 상태 처리
			    	// 현재까지 녹화된 영상업로드
					RecordingStop({
						'req_key' : consultInfoObject.req_key
						, 'movie_data' : null
						, 'hwnno' : consultInfoObject.hwnno
						, 'hwnname' : consultInfoObject.hwnname
						, 'succ_yn' : 'I'
					});
					
			    	SIP_Bye();
			    	// 고객에게 영상통화  연결 요청 
	    			callToCustomer(consultInfoObject.req_key);
			    	break;
				}
			    case USER_CUSTOMER :  // 내가 고객 일때
				{
			    	break;
				}
			}
			break;
		}
		case 'accept_timeout' : // 고객 상담 요청 응답 타임아웃
		{
			// Invite 타임아웃 종료
			timeoutInvite(false);
			changeUI('timeoutPopup');
			break;
		}
	}
}

function setASCallbackFunctions(){
	ConfigASCallback.events.open = function(){ 
		sendMessageToAS('login'); 
	};
	ConfigASCallback.events.message = onRecieveMessage;
}
