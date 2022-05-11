/**
 * auth    : jmkim
 * file    : m2soft_util.js
 * create  : 2016-11-15
 * modify  : 2016-12-27
 * version : 1.0.8
 * desc    : Utility API 관리
 */
 
function hasMediaDevice(callback) {
	var audioItem = [];
	var videoItem = [];
	var i, src;

	var device_check = function(srcs) {
		if(srcs.length == 0) {
			console.error('Does not find Media devices.');
			alert("미디어 디바이스를 찾을 수 없습니다");
			return;
		}
	
		/******************************************************
		  LG 단말일 경우 문제가 발생하여 예외처리 해놓음
		  추후 안드로이드 웹킷 업데이트 시 삭제 필요합니다.
		 *******************************************************/
		var selectIndex = 0;
		var iVideoCnt = 0;
		for(i = 0; i < srcs.length; i++) {
			src = srcs[i];
			 if(src.kind.indexOf('video') != -1) {
				 iVideoCnt++;
			 }
		}
		if (iVideoCnt > 2) {
			// 한개를 버려버림
			selectIndex = 1;
			iVideoCnt=0;
		}
		/******************************************************/
		mediaConstraints.video.optional[0].sourceId = null;
		for(i = 0; i < srcs.length; i++) {
			src = srcs[i];
			
			if(src.kind.indexOf('audio') != -1) {
			    audioItem.push(src);
			} else if(src.kind.indexOf('video') != -1) {
			    videoItem.push(src);
			    // 구버전
			    if (MediaStreamTrack.getSources != undefined) {
			    	if(src.label.indexOf("front") != -1) {
				        mediaConstraints.video.optional[0].sourceId = src.id;
					}
				    // Android 디바이스 전면 카메라 설정
				    if(src.facing == 'user') {
				    	mediaConstraints.video.optional[0].sourceId = src.id;
				    }
			    } else {
			    	// WebRTC 버그 수정[2018.09.06]
			    	if (src.facing == 'user' || src.label.indexOf("front") != -1) {
			    		mediaConstraints.video.optional[0].sourceId = src.deviceId;
			    	} else {
			    		if(mediaConstraints.video.optional[0].sourceId == null && iVideoCnt == selectIndex) {
			    			mediaConstraints.video.optional[0].sourceId = src.deviceId;
			    		}
			    	}
//			    	if (iVideoCnt == selectIndex) {
//			    		// mediaConstraints.video.facingMode = "user";
//			    		mediaConstraints.video.optional[0].sourceId = src.deviceId;
//			    	}
			    }
			    
			    iVideoCnt++;
			}
		}

//		if( (videoItem.length == 0) || (audioItem.length == 0) ) {
		if( (videoItem.length == 0) ) {
			console.error('Does not find Media devices');
			alert("미디어 디바이스를 찾을 수 없습니다");
			return;
		} else {
			console.log('Media device exist');
			if(typeof callback == 'function') {
				callback();
			}
		}
	}
	
	try {
		if(consultInfoObject.appType != 'ios') {
			// PC 또는 Android 디바이스 확인 후 SIP Init
			if (MediaStreamTrack.getSources != undefined) {
				MediaStreamTrack.getSources(device_check);
			} else {
				navigator.mediaDevices.enumerateDevices().then(device_check);
			}
		} else {
			// iOS 일때 디바이스 확인 없이 SIP Init
			if(typeof callback == 'function') {
				callback();
			}
		}
	} catch(e) {
		console.error(e.message);
		alert("디바이스 확인에 실패했습니다");
	}
}

function getChromeVersion() { 
	var raw = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
	return raw? parseInt(raw[2], 10) : false;
}

/**
 * 2018-12-22 - dhshin 
 * Chrome71 window.URL.createObjectURL(stream) 제거 대응
 */
function getMediaStreamSource(stream) {
	try {
		return window.URL.createObjectURL(stream);
	} catch(e) {
		return null;
	}
}

function muteMicrophone(bEnabled) {
	console.log("muteMicroPhone : " + bEnabled);
	var nTrack;
	if (oSipSessionCall != null &&
	    oSipSessionCall.o_session != null &&
	    oSipSessionCall.o_session.o_stream_local != null &&
	    oSipSessionCall.o_session.o_stream_local.getAudioTracks().length > 0) {
		for (nTrack = 0; nTrack < oSipSessionCall.o_session.o_stream_local.getAudioTracks().length ; nTrack++) {
			oSipSessionCall.o_session.o_stream_local.getAudioTracks()[nTrack].enabled = !bEnabled;
		}
		console.log("MicroPhone " + bEnabled + " Success");
	} else {
		console.log("MicroPhone Mute Failed");
	}
}

function muteWebCam(bEnabled) {
	console.info("muteWebCam : " + bEnabled);
	var nTrack;
	if (oSipSessionCall != null &&
	    oSipSessionCall.o_session != null &&
	    oSipSessionCall.o_session.o_stream_local != null &&
        oSipSessionCall.o_session.o_stream_local.getVideoTracks().length > 0) {
		for (nTrack = 0; nTrack < oSipSessionCall.o_session.o_stream_local.getVideoTracks().length ; nTrack++) {
			oSipSessionCall.o_session.o_stream_local.getVideoTracks()[nTrack].enabled = !bEnabled;
		}
		console.log("WebCame Mute " + bEnabled + " Success");
	} else {
		console.log("WebCame Mute Falied");
	}
}

/**
 * 영상통화 결과값을 다른 웹뷰로 전달하는 함수
 * @param param Object
 *        param.appType 	= 'ios' or null
 *        param.req_key 	= '<req_key>'
 *        param.result 		= 'auth-cancel',
 *                            'auth-success',
 *                            'auth-failure',
 *                            'call-start', or
 *                            'ring'
 */
function sendMsgToApp( param ){
	if( param.appType == 'ios' ) {
		if(cordova.plugins.xfingeriface.setResult) {

			if(param.result == 'auth-cancel' || param.result == 'auth-success' || param.result == 'auth-failure') {
//				SIP_Bye();
//				SIP_Unregister();
//				SIP_Release();
				AS_Disconnect();
				if(xfinger_session && xfinger_session.close) {
					xfinger_session.close();
				}
				if(cordova && cordova.plugins && cordova.plugins.iosrtc && cordova.plugins.iosrtc.closeVideos) {
					cordova.plugins.iosrtc.closeVideos();
				}
			}

			cordova.plugins.xfingeriface.setResult( param.result , param.req_key.toString() );
		}
	}
	else {
		if(window.xfingerIface.setResult) {
			if(param.result == 'auth-cancel' || param.result == 'auth-success' || param.result == 'auth-failure') {
//				SIP_Unregister();
				AS_Disconnect();
				if(xfinger_session && xfinger_session.close) {
					xfinger_session.close();
				}
			}
			window.xfingerIface.setResult( param.result , param.req_key );
		}
	}
	
	if(param.result == 'auth-cancel' || param.result == 'auth-success' || param.result == 'auth-failure') {
		deleteSipId();
	}
}

/**
 * 웹뷰로 이벤트를 전달하는 함수
 * @param param Object
 *        param.appType    = 'ios' or null
 *        param.event      = 'resume' or 'exit'
 *        param.value      = 'pause~resume duration time in seconds'
 */
function recvMsgFromApp(param) {
	if( param.event=='failure-alert'){
//		sendMsgToApp({
//			'appType' : consultInfoObject.appType
//			, 'req_key' : consultInfoObject.req_key
//			, 'result' : "auth-failure"
//		});
	} else if(param.event == 'exit') {
		// 고객의 상담 취소 
		exitConsult('backButton');
	} else {
		if(xfinger_session) {
			if(param.value >= 60) {
				// 종료 처리
				exitConsult('reconnectTimeout');
				return;
			}
			
			if(ASConnection && ASConnection.readyState == 1) {
				// Nothing to do
			} else {
				AS_Reconnect();
			}
		} else {
			// 고객 대시 연결 요청 상태에서 background -> foreground 로 돌아올 경우 
			exitConsult('exitWhenStandbyState');
		}
	}
}

/**
 * 호 발신/수신 타이머 
 * @param param set = true or false '타이머 시작', '타이머 종료'
 */
var InviteTimer = null;
var InviteTimeout = 60; // second
var timeoutInvite = function(set) {
	if(set == true) {
		InviteTimer = setTimeout(function() {
			if(xfinger_session) {
				switch(xfinger_session.call_response) {
					case 100 : // 고객이 호 수신 못한 상황
						changeUI('connectErrorPopup');
						break;
					case 180 : // 고객이 호 수신한 상황
						changeUI('timeoutPopup');
						break;
					default :  // 예외 상황
						changeUI('unknownErrorPopup');
				}
			}
		}, 1000 * InviteTimeout);
	} else {
		if(InviteTimer) {
			clearTimeout(InviteTimer);
		}
		InviteTimer = null;
	}
}

/**
 * Register 타이머  
 * @param param set = true or false '타이머 시작', '타이머 종료'
 */
var RegisterTimer = null;
var RegisterTimeout = 3; // second
var timeoutRegister = function(set) {
	if(set == true) {
		RegisterTimer = setTimeout(function() {
			if(consultInfoObject.state == STATE_BEFORE_CALL) {
				console.log(' # Register Timeout!! re registerUser()');
				SIP_Register(oConfigRegister);
				timeoutRegister(true);
			} else {
				console.log(' # Register Timer stop()');
				clearTimeout(RegisterTimer);
			}
		}, 1000 * RegisterTimeout);
	} else {
		if(RegisterTimer) {
			console.log(' # Register Timer stop()');
			clearTimeout(RegisterTimer);
		}
		RegisterTimer = null;
	}
}

/**
 * Ice Gathering 타이머  
 * @param param set = true or false '타이머 시작', '타이머 종료'
 */
var IceGatheringTimer = null;
var IceGatheringTimeout = 10; // second
var timeoutIceGathering = function(set) {
	try {
		if(set == true) {
			if(IceGatheringTimer != null) {
				return;
			}
			
			IceGatheringTimer = setTimeout(function() {
				if(xfinger_session.o_pc.iceGatheringState != "complete") {
					tmedia_session_jsep01.onIceGatheringCompleted(xfinger_session);
//					confirm("iceGathringState : " + xfinger_session.o_pc.iceGatheringState);
				} else {
					clearTimeout(IceGatheringTimer);
				}
			}, 1000 * IceGatheringTimeout);
		} else {
			if(IceGatheringTimer) {
				clearTimeout(IceGatheringTimer);
			}
			IceGatheringTimer = null;
		}
	} catch(e) {
//		confirm(e);
	}
}

/**
 * PeerConnection Object 의 getStat 을 이용한 데이터 체크 타이머
 * @param param Tthis = JSEP Object (tmedia_session_jsep01)
 */
var periodic_stats = null;
var getStatCheck = function(This) {
    try {
        This.o_pc.getStats;
    } catch(e) {
        console.log(e);
        clearInterval(periodic_stats);
        return;
    }

    This.o_pc.getStats(function(stats) {
        if(xfinger_session.o_mgr.b_started == false) {
            return;
        }

        stats.result().forEach(function(result) {
            if(result.type == "ssrc") {
                if(result.stat("mediaType").indexOf("video") > -1) {
                    if(result.id.indexOf("send") > -1) {
                        send_video_data = result;
                    } else if(result.id.indexOf("recv") > -1) {
                        recv_video_data = result;
                    }
                }
            }
        });

        network_status_data["send"]["bps"] = send_video_data.stat("bytesSent") - last_send_bps;
        network_status_data["send"]["pktlost"] = send_video_data.stat("packetsLost");
        network_status_data["send"]["rtt"] = send_video_data.stat("googRtt");

        network_status_data["recv"]["bps"] = recv_video_data.stat("bytesReceived") - last_recv_bps;
        network_status_data["recv"]["pktlost"] = recv_video_data.stat("packetsLost");
        network_status_data["recv"]["currentdelay"] = recv_video_data.stat("googCurrentDelayMs");

        
        if( login_user_config.user_type == USER_CONSULTANT ){
            if(network_status_data["recv"]["bps"] == 0) {
                threshold_count++;
                console.log("recv video bps zero : " + threshold_count + "s");
            } else {
                if(ASConnection.connectionState == 'close') {
                    threshold_count++;
                    console.log("Application Server disconnected : " + threshold_count + "s");
                } else {
                    threshold_count = 0;
                }
            }

            if(threshold_count == threshold_sec) {
                changeUI('disconnectCall');
                // 怨| 媛~] background 紐⑤~S~\濡~\ 吏~D?^~E ?V~H?]~D ?U~L ?E뱁~Y~T 醫~E猷~L
                RecordingStop({
                    'req_key' : consultInfoObject.req_key
                    , 'movie_data' : null
                    , 'hwnno' : consultInfoObject.hwnno
                    , 'hwnname' : consultInfoObject.hwnname
                    , 'succ_yn' : 'I'
                });
            }else if( threshold_count == 60 ){
                changeUI('timeoutPopup');
                clearInterval(periodic_stats);
            }else if( threshold_count == 0 ){
                changeUI('connectCall');
                if(recorder == null && consultInfoObject.approvalResult == null) {
                    // 怨| 媛~] backgroud -> foreground 濡~\ ?O~L?U~D?Y~T?]~D ?U~L ?Kㅼ~K~\ ?E뱁~Y~T
                    RecordingRestart();
                }
            }
        }

        last_send_bps = send_video_data.stat("bytesSent");
        last_recv_bps = recv_video_data.stat("bytesReceived");

        network_health_check_time++;
        if(network_health_check_time % 10 == 0) {
            AS_Mesg_NetworkStatus(network_status_data);
            network_health_check_time = 0;
        }
    });
}

/**
 * 리소스 초기화 메소드 
 */
var clearResource = function() {
	ASConnection = null;
    oSipStack = null;
    oSipSessionRegister = null;
    oSipSessionCall = null;
    xfinger_session = null;
    consultant_audio_stream = null;
    recorder = null;
    record_blobs = null;
}

/**
 * Candidate 정보 중 P2P 정보 삭제 메소드
 */
var deleteHostCandidate = function(sdp) {
    sdp = sdp.replace(/a=candidate:(.*) udp (.*) host (.*)\r\n/gi, '');
    return sdp;
}

/**
 * 상담원/고객 영상 상담 페이지 -> init() 호출 타이머  
 * @param param set = true or false '타이머 시작', '타이머 종료'
 */
var DocumentReadyTimer = null;
var DocumentReadyTimeout = 10; // second
var timeoutDocumentReady = function(set) {
	console.log(' # Document Timer Set : ' + set);
	if(set == true) {
		DocumentReadyTimer = setTimeout(function() {
			if(SIP_STATUS == SIP_NONE) {
				exitConsult('failToStart');
			} else {
				console.log(' # Document Timer stop()');
				clearTimeout(DocumentReadyTimer);
				DocumentReadyTimer = null;
			}
		}, 1000 * DocumentReadyTimeout);
	} else {
		if(DocumentReadyTimer) {
			console.log(' # Document Timer stop()');
			clearTimeout(DocumentReadyTimer);
		}
		DocumentReadyTimer = null;
	}
}

/**
 * 상담원/고객 init() 호출 -> register() 호출 타이머  
 * @param param set = true or false '타이머 시작', '타이머 종료'
 */
var InitializeTimer = null;
var InitializeTimeout = 25; // second
var timeoutInitialize = function(set) {
	console.log(' # Initialize Timer Set : ' + set);
	if(set == true) {
		if(InitializeTimer) {
			return;
		}
		
		InitializeTimer = setTimeout(function() {
			console.log(' # Initialize failed)');
			if(login_user_config.user_type == USER_CONSULTANT) {
				changeUI('failToInit');
			} else {
				exitConsult('failToInit');
			}
			InitializeTimer = null;
		}, 1000 * InitializeTimeout);
	} else {
		if(InitializeTimer) {
			console.log(' # Initialize Timer stop()');
			clearTimeout(InitializeTimer);
		}
		InitializeTimer = null;
	}
}