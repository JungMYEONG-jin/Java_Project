var app = {
    start: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },

    receivedEvent: function(id) {
        console.log('Received Event: ' + id);

        var videos = document.getElementsByTagName('video');
        for(var i in videos) {
            videos[i].playsInline = true;
        }

        cordova.plugins.iosrtc.registerGlobals();
        cordova.plugins.iosrtc.debug.enable('iosrtc*');
        this.loadedComplete = true;
        
    	// spass 모듈 초기화 진행
    	if (this.spass) {
    		console.log(">>>>>>>>>>>> initWebSocket call cordova APP !!! ");
    		this.spass.initWebSocket();
    	}
    },

    setVideoSrcObject: function(video, stream) {
        video.srcObject = stream;
    },

    loadedComplete: false,
    spass : null
};
app.start();

function isIphone() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	if (userAgent.match('iphone')) {
		return true;
	} else {
		return false;
	}
}

function isSafari() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	if (userAgent.match('safari')) {
		return true;
	} else {
		return false;
	}
}

function isAndroid() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	if (userAgent.match('android')) {
		return true;
	} else {
		return false;
	}
}

function isCordova() {
	if (window.cordova == undefined || window.cordova == null) {
		return false;
	} else {
		return true;
	}
}

if (isIphone() && !isSafari()) {
	app.loadedComplete = false;
} else {
	app.loadedComplete = true;
}

/**
 * 영상 통화 시 고객 화면에서 사용되는 영상 라이브러리
 * 
 * @auth ShinhanDS (kswksk)
 * @date 2018.09.07
 */
function SPassUser (reqKey, videoLocalID, videoRemoteID) {
	var my = this;
	
	this.uuid = reqKey;
	
	/*
	console.log('>>>>>>>> SPassUser -        reqKey : ' + reqKey);
	console.log('>>>>>>>> SPassUser -  videoLocalID : ' + videoLocalID);
	console.log('>>>>>>>> SPassUser - videoRemoteID : ' + videoRemoteID);
	*/
	
	this.localVideoID = videoLocalID;
	this.remoteVideoID = videoRemoteID;
	this.localVideo = null;
	this.remoteVideo = null;
	
	this.localStream = null;
	this.peerConnection = null;
	this.serverConnection = null;
	
	// 웹소켓 연결확인 
	this.webSocketInterval = null;
	
	// 이벤트 관련 콜백
	this.onmessage 	= null;
	this.onstartmovie = null;
	this.onendmovie = null;
	this.oncalling = null;

	this.turnPort = '9100';
	this.wssPort = '9200';
	
	this.videoSender = null;
	this.audioSender = null;
	this.checkIntervalTime = null;
	this.checkStatusTime = null;
	this.beforPeerStatus = null;
	this.checkPingCount = 0;
	
	if (window.location.hostname == 'spass.shinhansys.co.kr') {
		// 내부에서 테스트 할경우f
		this.turnPort = '19100';
		this.wssPort = '19200';
	}
	
	this.peerConnectionConfig = {
	  'iceServers': [
	    {'urls': 'turn:'+window.location.hostname+':'+this.turnPort+'?transport=tcp', username:'spass', credential:'spass'}
	  ]
	};
	
	// 객체를 삽입한다.
	app.spass = this;
	
	this.initWebSocket = function() {
		console.log(">>>>>>>>>>>> initWebSocket : " + app.loadedComplete);
		if (isCordova() && isIphone()) {
			$('html, body').css('background-color', 'white');
		}
		
		// 코도바가 로드될때까지 대기v
		if (!app.loadedComplete) return;
		
		my.serverConnection = new WebSocket('wss://' + window.location.hostname + ':'+this.wssPort);
		my.serverConnection.onmessage = my.gotMessageFromServer;
		my.serverConnection.onopen = my.gotOnOpen;
		my.serverConnection.onerror = my.gotError;
		my.serverConnection.onclose = my.gotClose;
		
		// 백그라운드로 내려갔을 때 끊어 졌는지를 체크하는 인터발
		if (my.webSocketInterval != null) {
			clearInterval(my.webSocketInterval);
			my.webSocketInterval = null;
		}
		
		my.webSocketInterval = setInterval(function () { 
			if (my.serverConnection.readyState !== WebSocket.OPEN) {
				console.log('%cWebSocket RECONNECT !!', 'color:red;font-size:large');
				my.initWebSocket();
			} else {
				var currTime = Date.now();
				if (my.checkIntervalTime) {
					var different = currTime - my.checkIntervalTime;
					if (different > 9*1000) {
						// 9초가 오바 되었다면 리커넥트 요청해봄
						var reconnMsg = {
								'msgType' : 'signaling',
								'cmd' : 'reconnected'
							};
						my.sendMsg(reconnMsg);
					}
				}
			}
			my.checkIntervalTime = currTime;
			
			if ((my.checkPingCount % 3) == 0) {
				my.sendPing();
			}
			my.checkPingCount++;
			if (my.checkPingCount == 30) {
				my.checkPingCount = 0;
			}
		}, 3000);
	};
	
	this.gotOnOpen = function (event) {
		// OPEN 상태가 아닐경우가 있음
		var sendMsg = function () {
			if (my.serverConnection.readyState !== WebSocket.OPEN) {
				setTimeout(function () {
					sendMsg();
				}, 10);
				return;
			}

			my.serverConnection.send(JSON.stringify({'cmd': 'myInfo', 'uuid': my.uuid, 'myType' : 'user'}));
			if (!my.localVideo) {
				my.initMedia();	
			} else {
				// 재연결일 경우
				var reconnMsg = {
					'msgType' : 'signaling',
					'cmd' : 'reconnected'
				};
				my.sendMsg(reconnMsg);
			}
		}
		sendMsg();
	};
	
	
	this.gotError = function(e) {
		console.log('%cWebSocket ERROR !!', 'color:red;font-size:large');
	};
	
	this.gotClose = function(e) {
		console.log('%cWebSocket CLOSE !!', 'color:red;font-size:large');
	};
	
	this.initMedia = function() {
		console.log(">>>>>>>>>>>>> isCordova() :: " + isCordova());
		
		my.localVideo = document.getElementById(my.localVideoID);
		my.remoteVideo = document.getElementById(my.remoteVideoID);
		
		// 볼륨 최대치로
		my.localVideo.volume = 1;
		my.remoteVideo.volume = 1;
		var obj = document.createElement('audio');
		obj.volume = 1;
		
		console.log('>>>>>>>> initMedia -   my.localVideo : ' + my.localVideo + ', id : ' + my.localVideoID);
		console.log('>>>>>>>> initMedia -  my.remoteVideo : ' + my.remoteVideo + ', id : ' + my.remoteVideoID);
		
		// user : 전면, environment : 후면
		var constraints = null;
		if (isIphone()  || isAndroid()) {
			// 모바일 이면 전면 카메라
			constraints = {
				audio: true,
				video: {
					facingMode : "user"
				}
			};
		} else {
			// PC면 그냥 틀어
			constraints = {
					audio: true,
					video: true
				};
		}
		
		if(navigator.mediaDevices.getUserMedia) {
			console.log(">>>>>>> CALL : navigator.mediaDevices.getUserMedia()");
			navigator.mediaDevices.getUserMedia(constraints).then(my.getUserMediaSuccess).catch(my.errorHandler);
		} else {
			alert('Your browser does not support getUserMedia API');
		}
	};
	
	this.getUserMediaSuccess = function (stream) {
		console.log(">>>>>>> METHOD : getUserMediaSuccess() %o", stream);
		my.localStream = stream;
		try {
			my.localVideo.src = window.URL.createObjectURL(stream);
		} catch (e) {
			console.log(">>>>> createObjectURL ERROR : %o", e);
			my.localVideo.srcObject = stream;
		}
		
		my.localVideo.play();
		
		try {
			// safari
			window.stream = stream;
		} catch (e) {}
	};
	
	this.start = function (isCaller) {
		isCaller = false;   // 고객은 무조건 false
		console.log(">>>>>>> METHOD : start("+isCaller+")");
		
		if (my.localStream == null) {
			setTimeout(function () {
				my.start(isCaller);
			}, 500);
		}
		
		if (typeof RTCPeerConnection != 'undefined') {
			my.peerConnection = new RTCPeerConnection(my.peerConnectionConfig);
		} else {
			my.peerConnection = new webkitRTCPeerConnection(my.peerConnectionConfig);
		}
		
		my.peerConnection.onicecandidate = my.gotIceCandidate;
		// my.peerConnection.ontrack = my.gotRemoteStream;
		my.peerConnection.onaddstream = my.gotRemoteStream;
		my.peerConnection.oniceconnectionstatechange = my.changePeerConnection;
		
		try {my.peerConnection.addStream(my.localStream);} catch (e) {}
		/*
		if (my.peerConnection.addTrack != undefined) {
			try {
				my.audioSender = my.peerConnection.addTrack(my.localStream.getAudioTracks()[0], my.localStream);
				my.videoSender = my.peerConnection.addTrack(my.localStream.getVideoTracks()[0], my.localStream);
			} catch (e) {
				// 안드로이드 코도바에서 오류나서 이렇게 처리
				try {my.peerConnection.addStream(my.localStream);} catch (e) {}
			}
		} else {
			try {my.peerConnection.addStream(my.localStream);} catch (e) {}
		}
		*/
		
		if (isCordova()) {
			// 코도바의 경우 아래 코드로 실행해야함
			my.peerConnection.addEventListener('addstream', function (e) {
				console.log('>>>>>>>>>>>>> addstream : %o', e);
				my.gotRemoteStream.call(my, e);
			});
		}
		
		my.serverConnection.send(JSON.stringify({'type': 'start_ok', 'uuid': my.uuid}));
	};
	
	this.changePeerConnection = function(e) {
		// 상태 값 : new, checking, connected, completed, disconnected, failed, closed
		// console.log('%c[peerStateChange] STAT : ' + e.target.iceConnectionState, 'color:red; font-size:large');
		console.log('%c[peerStateChange] STAT : %o', 'color:red; font-size:large', e);
		
		var ststus = null;
		if (e.target) {
			ststus = e.target.iceConnectionState;
		} else if (e._target) {
			ststus = e._target.iceConnectionState;
		} else {
			console.log('%c[peerStateChange] ERROR Not supported API !!! : '+ststus, 'color:red; font-size:large');
		}
		
		console.log('%c[peerStateChange] STAT : '+ststus, 'color:green; font-size:large');
		
		if (ststus == 'connected' || ststus == 'completed') {
			if (isCordova() && isIphone() && my.localVideo) {
				$('html, body').css('background-color', 'transparent');
				my.localVideo.style.display = 'none';
			}
			console.log("[PEER] connected : " + my.onstartmovie);
			if (my.onstartmovie) {
				my.onstartmovie();
			}
			my.sendStartMovie();
		} else if (ststus == 'disconnected' || ststus == 'failed' || ststus == 'close') {
			console.log("[PEER] disconnected : " + my.onendmovie);
			if (my.onendmovie) {
				my.onendmovie();
			}
		}
		
		my.beforPeerStatus = ststus;
	};
	
	this.gotMessageFromServer = function (message) {
		console.log('>>>>> gotMessageFromServer() : ' + message.data);

		var signal = JSON.parse(message.data);
		
		if (signal.type) {
			if (signal.type == 'send_msg') {
				if (!my.onmessage) {
					alert('onmessage 콜백 메소드가 등록되어 있지 않습니다.');
					return;
				}
				var retData = null;
				if (signal.datatype == 'json') {
					retData = JSON.parse(signal.data);
				} else {
					retData = signal.data;
				}
				my.onmessage(retData);
			} else if (signal.type == 'start') {
				// 영상 시작
				if (my.oncalling) {
					my.oncalling();
				} else {
					my.start(false);
				}
			} else if (signal.type == 'end') {
				// 영상 끊길경우 릴리즈
				my.peerConnection.close();
				my.peerConnection = null;
			} else if (signal.type == 'onstartmovie') {
				console.log(">>>> onstartmovie >>>> beofor status : " + my.beforPeerStatus);
				if (my.beforPeerStatus == 'failed') {
					// 다시 연결됬는데 이벤트가 발생하지 않아 상담원에서 다시 연결됬다고 보내옴
					if (my.onstartmovie) {
						my.onstartmovie();
					}
				}
			} else if (signal.type == 'ping') {
				// 무의미한 정보 살아 있는지 확인용
				console.log("... ping을 수신함 !! ");
			}
		} else if(signal.sdp) {
			console.log('>>>>> peerConnection.setRemoteDescription()');
			my.peerConnection.setRemoteDescription(new RTCSessionDescription(signal.sdp)).then(function() {
				// Only create answers in response to offers
				if(signal.sdp.type == 'offer') {
					console.log(">>>>>>> CALL : peerConnection.createAnswer()");
					my.peerConnection.createAnswer().then(my.createdDescription).catch(my.errorHandler);
				}
			}).catch(my.errorHandler);
		} else if(signal.ice) {
			console.log(">>>>>>> CALL : peerConnection.addIceCandidate()");
			my.peerConnection.addIceCandidate(new RTCIceCandidate(signal.ice)).catch(my.errorHandler);
		} 
	};
	
	this.gotIceCandidate = function (event) {
		if(event.candidate != null) {
			console.log(">>>>>>> SEND : ICE");
			my.serverConnection.send(JSON.stringify({'ice': event.candidate, 'uuid': my.uuid}));
		}
	};
	
	this.createdDescription = function (description) {
		console.log(">>>>>>> METHOD : createdDescription : " + description);
		console.log(">>>>>>> CALL : peerConnection.setLocalDescription()");
		my.peerConnection.setLocalDescription(description).then(function() {
			console.log(">>>>>>> SEND : SDP");
			my.serverConnection.send(JSON.stringify({'sdp': my.peerConnection.localDescription, 'uuid': my.uuid}));
		}).catch(my.errorHandler);
	};
	
	this.gotRemoteStream = function (event) {
		console.log(">>>>>>> METHOD : gotRemoteStream() 2222 : %o", event);
		
		var stream = null;
		if (event.stream) {
			stream = event.stream;
		} else {
			stream = event.streams[0];
		}
		
		if (my.remoteVideo != null) {
			try {
				my.remoteVideo.srcObject = stream;
			} catch (e) {
				console.log(">>>>> createObjectURL ERROR : %o", e);
				my.remoteVideo.src = window.URL.createObjectURL(stream);
			}
			my.remoteVideo.play();
		}
	};
	
	this.errorHandler = function (error) {
		console.log(error);
	};
	
	this.sendMsg = function (data) {
		if (my.serverConnection.readyState !== WebSocket.OPEN) {
			return;
		}
		
		var sendData = null;
		var dataType = 'string';
        if(typeof data == 'object') {
        	sendData = JSON.stringify(data);
        	dataType = 'json';
        } else {
        	sendData = data;
        }
        
        sendData = {'type' : 'send_msg', 'datatype' : dataType, 'data' : sendData, 'uuid': my.uuid};
        my.serverConnection.send(JSON.stringify(sendData));
	};
	
	this.sendPing = function() {
		if (my.serverConnection.readyState !== WebSocket.OPEN) {
			return;
		}
		my.serverConnection.send(JSON.stringify({'type': 'ping', 'uuid': my.uuid}));
	}
	
	this.sendStartMovie = function() {
		if (my.serverConnection.readyState !== WebSocket.OPEN) {
			return;
		}
		my.serverConnection.send(JSON.stringify({'type': 'onstartmovie', 'uuid': my.uuid}));
	}
	
	this.close = function (callBack) {
		if (my.localStream != null) {
			try {
				my.localStream.getTracks().forEach(function (t) { t.stop();});
			} catch (e) {
				console.log('localStream release error : %o', e);
			}
		}
		
		if (my.serverConnection) {
			my.serverConnection.close();
			my.serverConnection = null;
		}
		
		if (my.peerConnection) {
			my.peerConnection.close();
			my.peerConnection = null;
		}
		
		if (my.webSocketInterval) {
			clearInterval(my.webSocketInterval);
			my.webSocketInterval = null;
		}
		
		if (callBack) {
			callBack.call(my);
		}
	}
}