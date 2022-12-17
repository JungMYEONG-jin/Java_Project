/**
 * 영상 통화 시 상담사 화면에서 사용되는 영상 라이브러리
 *
 * @auth ShinhanDS (kswksk)
 * @date 2018.09.07
 */
function SPassConult(reqKey, videoLocalID, videoRemoteID, lInfo, videoCavasID) {
	var my = this;

	this.uuid = reqKey;
	this.loginInfo = lInfo;
	this.cavasVideoID = videoCavasID
	this.localVideoID = videoLocalID;
	this.remoteVideoID = videoRemoteID;
	this.localVideo = null;
	this.remoteVideo = null;
	this.canvasVideo = null;

	this.localStream = null;
	this.peerConnection = null;
	this.serverConnection = null;

	// 이벤트 관련
	this.onmessage = null;
	this.onstartmovie = null;
	this.onendmovie = null;
	this.ondisconnected = null;

	// 영상정보 저장용
	this.recordBlobs= null;
	this.recodeStream = null;
	this.cavasStream = null;
	this.recorder = null;
	this.remoteStream = null;
	this.mixedAudioStream = null;
	this.custAudio = null;
	this.consultAudio = null;
	this.audioContext = null;
	this.isRecording = false;
	this.isUpload = false;
	this.isCheckDevice = false;
	this.beforPeerStatus = null;

	this.turnPort = '19100';
	this.wssPort = '19200';

	this.videoSender = null;
	this.audioSender = null;

	// 영상 업로드
	this.recodingTimeout = null;

	if (window.location.hostname == 'noface.shinhansys.co.kr') {
		// 외부에서 테스트 할경우
		this.turnPort = '9100';
		this.wssPort = '9200';
	}
	
	this.peerConnectionConfig = {
	  'iceServers': [
	    {'urls': 'turn:'+window.location.hostname+':'+this.turnPort+'?transport=tcp', username:'spass', credential:'spass'}
	  ]
	};

	this.startCall = function() {
		my.serverConnection = new WebSocket('wss://' + window.location.hostname + ':'+this.wssPort);
		my.serverConnection.onmessage = my.gotMessageFromServer;
		my.serverConnection.onopen = my.gotOnOpen;
		my.serverConnection.onerror = my.errorHandler;
		my.serverConnection.onclose = my.oncloseHandler;
	};
	
	// 단말에 캠 및 오디오/마이크 확인
	this.checkDevice = function() {
		var isVideo = false;
		var isAudio = false;

		var device_check = function(srcs) {
			if(srcs.length == 0) {
				return;
			}
			for(i = 0; i < srcs.length; i++) {
				var src = srcs[i];
				if (src.kind.indexOf('audio') != -1) {
					isAudio = true;
				} else if (src.kind.indexOf('video') != -1) {
					isVideo = true;
				}
			}
			
			console.log(" >>> isVideo : " + isVideo + ", isAudio : " + isAudio);
			
			var msg = "";
			if (isVideo && isAudio) {
				my.isCheckDevice = true;
				my.initMedia();
				return;
			} else if (isVideo == false && isAudio == false) {
				msg = "캠 및 해드셋 장치를 찾을 수 없습니다.";
			} else if (isVideo == false) {
				msg = "캠 장치를 찾을 수 없습니다.";
			} else if (isAudio == false) {
				msg = "해드셋 장치를 찾을 수 없습니다.";
			}
			alert(msg);
			// 클라이언트 취소 처리
			var msg = {'msgType':'signaling','cmd':'ended'};
			my.sendMsg(msg);
			// 상담원 이전 처리
			if (my.onmessage) {
				var msg = {'msgType':'client','cmd':'exit'};
				my.onmessage(msg);
			}
		} ;
		
		if (MediaStreamTrack.getSources != undefined) {
			MediaStreamTrack.getSources(device_check);
		} else {
			navigator.mediaDevices.enumerateDevices().then(device_check);
		}
	};

	this.initMedia = function() {
		my.localVideo = document.getElementById(my.localVideoID);
		my.remoteVideo = document.getElementById(my.remoteVideoID);
		my.canvasVideo = document.getElementById(my.cavasVideoID);
		
		// 캠 및 오디오/마이크가 탑재되어 있는지를 확인한다.
		if (my.isCheckDevice == false) {
			my.checkDevice();
			return;
		}
		
		// 볼륨 최대치로
		my.localVideo.volume = 1;
		my.remoteVideo.volume = 1;
		var obj = document.createElement('audio');
		obj.volume = 1;
		
		var constraints = {
			video: true,
			audio: true
		};

		navigator.nativeGetUserMedia = (navigator.webkitGetUserMedia || navigator.mozGetUserMedia);

		if(navigator.mediaDevices.getUserMedia) {
			console.log(">>>>>>> CALL : navigator.mediaDevices.getUserMedia()");
			navigator.mediaDevices.getUserMedia(constraints).then(my.getUserMediaSuccess).catch(my.errorHandler);
		} else {
			alert('Your browser does not support getUserMedia API');
		}
	};

	this.start = function(isCaller) {
		console.log(">>>>>>> METHOD : start("+isCaller+") :: localstream : %o", my.localStream);
		my.peerConnection = new RTCPeerConnection(my.peerConnectionConfig);
		my.peerConnection.onicecandidate = my.gotIceCandidate;
		// onaddstream으로 변경
		// my.peerConnection.ontrack = my.gotRemoteStream;
		my.peerConnection.onaddstream = my.gotRemoteStream;

		// try{my.peerConnection.addStream(my.localStream);} catch (e) {}
		if (my.peerConnection.addTrack) {
			my.audioSender = my.peerConnection.addTrack(my.localStream.getAudioTracks()[0], my.localStream);
			my.videoSender = my.peerConnection.addTrack(my.localStream.getVideoTracks()[0], my.localStream);
			
			// 로컬영상을 끔 - 국내 비대면은 필요가 없어서
			// my.videoSender.track.enabled = false;
		} else {
			try{my.peerConnection.addStream(my.localStream);} catch (e) {}
		}

		my.peerConnection.oniceconnectionstatechange = my.changePeerConnection;

		if(isCaller) {
			console.log(">>>>>>> CALL : peerConnection.createOffer()");
			// 사용자 화면에서 start_ok를 보내면 오퍼를 날림
			// my.peerConnection.createOffer().then(my.createdDescription).catch(my.errorHandler);
			my.serverConnection.send(JSON.stringify({'type': 'start', 'uuid': my.uuid}));
		}
	};

	this.gotOnOpen = function (event) {
		my.serverConnection.send(JSON.stringify({'cmd': 'myInfo', 'uuid': my.uuid, 'myType' : 'consult'}));
		my.initMedia();
	};

	this.getUserMediaSuccess = function (stream) {
	  console.log(">>>>>>> METHOD : getUserMediaSuccess() : %o", stream);
	  my.localStream = stream;
	  try {
	      my.localVideo.src = window.URL.createObjectURL(stream);
	  } catch (e) {
		  console.log(">>>>> createObjectURL ERROR : %o", e);
		  my.localVideo.srcObject = stream;
	  }

	  try {
		  // safari
		  window.stream = stream;
	  } catch (e) {}

	  my.start(true);
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

				if (signal.datatype == 'json') {
					if (retData.msgType && retData.msgType == 'signaling' && retData.cmd == 'reconnected') {
						// 재연결이 들어옴
						my.start(true);
					}
				}

				my.onmessage(retData);
			} else if (signal.type == 'start_ok') {
				// 클라이언트가 준비가 완료되면 오퍼를 날림
				my.peerConnection.createOffer().then(my.createdDescription).catch(my.errorHandler);
			} else if (signal.type == 'ping') {
				console.log("... ping을 수신함 !! ");
			} else if (signal.type == 'onstartmovie') {
				console.log(">>>> onstartmovie >>>> beofor status : " + my.beforPeerStatus);
				if (my.beforPeerStatus == 'failed') {
					// 다시 연결됬는데 이벤트가 발생하지 않아 상담원에서 다시 연결됬다고 보내옴
					if (my.onstartmovie) {
						my.onstartmovie();
					}
				}
			}
		} else  if(signal.sdp) {
			my.peerConnection.setRemoteDescription(new RTCSessionDescription(signal.sdp)).then(function() {
				// Only create answers in response to offers
				if(signal.sdp.type == 'offer') {
					console.log(">>>>>>> CALL : peerConnection.createAnswer()");
					my.peerConnection.createAnswer().then(my.createdDescription).catch(my.errorHandler);
				}
			}).catch(my.errorHandler);
		} else if(signal.ice) {
			console.log(">>>>>>> CALL : peerConnection.addIceCandidate()");
			if (signal.ice.sdpMid != undefined) {
				my.peerConnection.addIceCandidate(new RTCIceCandidate(signal.ice)).catch(my.errorHandler);
			}
		}
	};

	this.gotIceCandidate = function(event) {
		if(event.candidate != null) {
			console.log(">>>>>>> SEND : ICE");
			my.serverConnection.send(JSON.stringify({'ice': event.candidate, 'uuid': my.uuid}));
		}
	};

	this.createdDescription = function(description) {
		console.log(">>>>>>> METHOD : createdDescription : " + description);
		console.log(">>>>>>> CALL : peerConnection.setLocalDescription()");

		my.peerConnection.setLocalDescription(description).then(function() {
			console.log(">>>>>>> SEND : SDP");
			my.serverConnection.send(JSON.stringify({'sdp': my.peerConnection.localDescription, 'uuid': my.uuid}));
		}).catch(my.errorHandler);
	};

	this.gotRemoteStream = function(event) {
		console.log(">>>>>>> METHOD : gotRemoteStream() :: %o", event);

		var stream = null;
		if (event.stream) {
			stream = event.stream;
		} else {
			stream = event.streams[0];
		}

		try {
			my.remoteVideo.src = window.URL.createObjectURL(stream);
		} catch (e) {
			console.log(">>>>> createObjectURL ERROR : %o", e);
			my.remoteVideo.srcObject = stream;
		}
		
		my.remoteStream = stream;

		if (my.canvasVideo) {
			// 캔바스에 영상을 그린다.
			my.drawToCanvas();
		}
	};
	
	this.drawToCanvas = function () {
		var ctx2d = my.canvasVideo.getContext('2d');
		var canvasWidth = my.canvasVideo.width;
		var canvasHeight =  my.canvasVideo.height;
		var videoWidth = my.remoteVideo.videoWidth;
		var videoHeight = my.remoteVideo.videoHeight;

		// #354356 (백그라운드색상)
		ctx2d.beginPath();
		ctx2d.rect(0, 0, canvasWidth, canvasHeight);
		ctx2d.fillStyle = "#354356";
		ctx2d.fill();
		
		if (videoWidth > 0 && videoHeight > 0) {
			var x = 0;
			var y = 0;
			var w = 0;
			var h = 0;
			
			// 높이를 기준으로 맞춘다.
			var maxHeight = canvasHeight;
			var rateY = maxHeight / videoHeight;
			w = videoWidth * rateY; 
			h = videoHeight * rateY;
			x = (canvasWidth - w) / 2;
			ctx2d.drawImage(my.remoteVideo, 0, 0, videoWidth, videoHeight, x, y, w, h);
			
			// 고객 화면을 넣어본다.
			/*
			videoWidth = my.localVideo.videoWidth;
			videoHeight = my.localVideo.videoHeight;
			var customerWidth = 100;
			var customerHeight = videoHeight * (customerWidth/videoWidth);
			var offset = 15;
			ctx2d.drawImage(my.localVideo, 0, 0, videoWidth, videoHeight, x+offset, y+offset, customerWidth, customerHeight);
			*/
		}
		
		window.requestAnimationFrame(my.drawToCanvas);
	}

	this.errorHandler = function (error) {
		console.log('%cWebSocket ERROR !!', 'color:red;font-size:large');
	};

	this.oncloseHandler = function() {
		console.log('%cWebSocket CLOSE !!', 'color:red;font-size:large');
		// 웹소켓 끊어짐
		/*
		var data = {
			'succ_yn' : 'I',
			'movie_data' : null
		};
		this.stopRecording(data, function(){});
		*/
	}

	this.changePeerConnection = function (e) {
		// 상태 값 : new, checking, connected, completed, disconnected, failed, closed
		console.log('[peerStateChange] STAT : ' + e.target.iceConnectionState);

		if (e.target.iceConnectionState == 'connected' || e.target.iceConnectionState == 'completed') {
			if (my.onstartmovie) {
				my.onstartmovie();
			}
			
			if (e.target.iceConnectionState == 'connected') {
				my.startRecording();
			}

			my.sendStartMovie();
		} else if (/*e.target.iceConnectionState == 'disconnected'  || */e.target.iceConnectionState == 'failed' || e.target.iceConnectionState == 'close') {
			if (my.ondisconnected) {
				my.ondisconnected();
			}

			if (my.onendmovie) {
				my.onendmovie();
			}
			
			if (e.target.iceConnectionState == 'close') {
				var data = {
					'succ_yn' : 'I',
					'movie_data' : null
				};
				my.stopRecording(data, function(){});
			}
		}
		
		my.beforPeerStatus = e.target.iceConnectionState;
	}

	this.sendMsg = function (data) {
		if (my.serverConnection.readyState !== WebSocket.OPEN) {
			alert("WebSocket not opened !");
			return;
		}

		var sendData = null;
		var dataType = 'string';
        if(typeof obj == 'object') {
        	sendData = JSON.stringify(data);
        	dataType = 'json';
        } else {
        	sendData = data;
        }

        sendData = {'type' : 'send_msg', 'datatype' : dataType, 'data' : sendData, 'uuid': my.uuid};
        my.serverConnection.send(JSON.stringify(sendData));
	}

	/**************************************************************/
	// 영상녹화 관련 메소드
	/**************************************************************/
	// 상담원의 오디오를 캡처한다.
	var audioCapture = function() {
		var constraints = {};
		constraints.audio = {
			    optional: [
			        { googEchoCancellation:true },
			        { googEchoCancellation2:true },
			        { googAutoGainControl:false },
			        { googAutoGainControl2:true },
			        { googNoiseSuppression:true },
			        { googNoiseSuppression2:true },
			        { googHighpassFilter:true },
			        { googAudioMirroring:true },
			        { googDucking:true }/*,
			        { sourceId:audio_id }*/
				]
			};
		navigator.nativeGetUserMedia(constraints, function(stream) {
			my.recodeStream = stream;
			if (my.canvasVideo) {
				my.cavasStream = my.canvasVideo.captureStream();
			}
		}, function(error) {
		    console.error("Consultant Mic Capture Error");
		    my.recodeStream = null;
		});
	}

	this.isRecording = false;
	this.startRecording = function() {
		if (my.isRecording) {
			return;
		}

		if (my.recodingTimeout) {
			clearTimeout(my.recodingTimeout);
			my.recodingTimeout = null;
		}

		// 용량이 커지면 업로드 시간이 길어져 주기적으로 업로드한
		my.recodingTimeout = setTimeout(function() {
			var data = {
				'succ_yn' : 'I',
				'movie_data' : null
			};
			my.stopRecording(data, function(){});
			my.startRecording();
		}, 1000*60*5);
		// 1000*60*10

		audioCapture();
		my.recordBlobs = [];
		try {
			if (my.recodeStream == null || my.remoteStream == null) {
				setTimeout(function() {
					my.startRecording();
				}, 500);
				return;
			}
			my.isRecording = true;

			console.log(" >>>>>>> remote : %o", my.remoteStream);
			console.log(" >>>>>>> recode : %o", my.recodeStream);

			// 상담원 마이크 녹화 실패했을때, 고객 비디오만 녹화
			if(my.recodeStream == null) {
				my.recodeStream = my.remoteStream;
				// 상담원 마이크 녹화 성공 했을때
			} else {
				my.audioContext = new AudioContext();
	    	    my.mixedAudioStream = my.audioContext.createMediaStreamDestination();
	    	    my.consultAudio = my.audioContext.createMediaStreamSource(my.recodeStream);
	    	    my.custAudio = my.audioContext.createMediaStreamSource(my.remoteStream);

	    	    my.consultAudio.connect(my.mixedAudioStream);
	    	    my.custAudio.connect(my.mixedAudioStream);

	    	    if (my.cavasStream) {
	    	    	my.cavasStream.addTrack(my.mixedAudioStream.stream.getAudioTracks()[0]);
	    	    	my.cavasStream.addTrack(my.remoteStream.getVideoTracks()[0]);
	    	    } else {
	    	    	my.recodeStream.removeTrack(my.recodeStream.getAudioTracks()[0]);
	    	    	my.recodeStream.addTrack(my.mixedAudioStream.stream.getAudioTracks()[0]);
	    	    	my.recodeStream.addTrack(my.remoteStream.getVideoTracks()[0]);
	    	    }
			}

			if (my.cavasStream) {
				// my.recorder = new MediaRecorder(my.cavasStream, {mimeType: 'video/webm; codecs=vp8', bitsPerSecond: 20000});
				// 화질을 일단 좋게 만듬 (
				my.recorder = new MediaRecorder(my.cavasStream, {mimeType: 'video/webm; codecs=vp8', bitsPerSecond: 300 * 1000}); // 볼만한화질
				//my.recorder = new MediaRecorder(my.cavasStream, {mimeType: 'video/webm; codecs=vp8'}); // 최대화질
			} else {
				my.recorder = new MediaRecorder(my.recodeStream, {mimeType: 'video/webm; codecs=vp8', bitsPerSecond: 20000});
			}
		    
		    my.recorder.onstop = my.stopRecordingCallback;
		    my.recorder.ondataavailable = my.dataAvailableCallback;
		    my.recorder.start(10);
		} catch(e) {
		    console.error("Startg Recording Error");
		    console.error(e.message);
		}
	}

	/**
	 * @ desc : 녹화 종료 메소드
	 */
	this.stopRecording = function(data, callback) {
		console.log('STOP RECORDING !!!! ' + JSON.stringify(data));
		if (my.peerConnection == null) {
			// close 호출로 인한 스탑리코딩이 옴
			return;
		}
		
		if (my.isUpload) {
			console.log('STOP RECORDING UPLOADING WAIT !!!! ' + JSON.stringify(data));
			setTimeout(function() {
				my.stopRecording(data, callback);
			}, 1000);
			return;
		}
		
		//console.log("consult_movie_data: [" + data.consult_movie_data + "]");
		if (my.isRecording == false) {
			console.log('>>>>>> NO Recording !!!!');
			var json = {error_code : '9999', error_msg : '영상정보가 존재하지 않습니다.'};
			callback.call(this, json);
			return;
		}

		my.isRecording = false;
	    var blob, url, a;
		try {
			if (!my.recorder) {
				return;
			}
			my.recorder.stop();
			blob = new Blob(my.recordBlobs, {type: 'video/webm;codecs="vp8"'});
			/*
			url = window.URL.createObjectURL(blob);
			a = document.createElement('a');
			a.style.display = "none";
			a.href = url;
			a.download = "record_file.webm";
			document.body.appendChild(a);
			a.click();
			*/
			data.movie_data = blob;

			my.uploadMovieFile( data , callback );

			my.recorder = null;
			my.recordBlobs = null;
			my.recodeStream = null;
			my.audioContext = null;
			my.mixedAudioStream = null;
			// mixedMediaStream = null;
			my.consultAudio = null;
			my.custAudio = null;
			my.isRecording = false;

			if (my.recodingTimeout) {
				clearTimeout(my.recodingTimeout);
				my.recodingTimeout = null;
			}
		} catch(e) {
		    console.error("Stop Recording Error : " + e);
			data.movie_data = blob;
			my.uploadMovieFile( data , callback );
		}
	}

	/**
	 * @ desc : MediaRecorder 데이터 생성 시작 콜백 메소드
	 */
	this.dataAvailableCallback = function(event) {
		my.isRecording = true;
		if(event.data && event.data.size > 0 && my.recordBlobs != null) {
			my.recordBlobs.push(event.data);
		}
	}

	/**
	 * @ desc : MediaRecorder 종료 콜백 메소드
	 */
	this.stopRecordingCallback = function(e) {
		my.isRecording = false;
	}
	
	this.sendEndMovie = function () {
		my.serverConnection.send(JSON.stringify({'type': 'end', 'uuid': my.uuid}));
		my.peerConnection.close();
		my.peerConnection = null;
	}
	
	this.sendStartMovie = function() {
		my.serverConnection.send(JSON.stringify({'type': 'onstartmovie', 'uuid': my.uuid}));
	}

	/**
	 * 영상을 업로드 한다.
	 */
	this.uploadMovieFile = function(params, callback) {
		my.isUpload = true;
		console.log(params);
		console.log(new File([params.movie_data], "record.webm"));
		try{
			if( params.succ_yn == 'I' && params.movie_data == null ){
				if(typeof(callback) === "function") {
					callback.call(this);
				}
				return;
			}

			var formdata = new FormData();
			formdata.append("cert_no", my.uuid);
			formdata.append("movie_data", (params.movie_data !=null) ? new File([params.movie_data], "record.webm") : null );
			formdata.append("hwnno", my.loginInfo.hwnno);
			formdata.append("hwnname", my.loginInfo.hwnname);
			formdata.append("complete_cd", params.succ_yn);
			// formdata.append("idcard_msg", params.idcard_msg);

			$.ajax({
				type		: "post",
				url			: "/noface/NF1070.do",
				data		: formdata,
				async		: true,
				processData	: false,
				contentType	: false,
				dataType	: 'json',
				success		: function(json){
					my.isUpload = false;
					console.log('/noface/NF1070.do complete! result.');
					console.log(json);

					if ( json['error_code'] == "0000"){
						console.log("success");
						if(typeof(callback) === "function") {
							callback.call(this, json);
						}
						
						// 종료 이벤트를 발생시킨다.
						if (params.succ_yn && (params.succ_yn != 'I')) {
							my.sendEndMovie();
						}
					} else {
						console.log("UPLOAD ERROR 1 : " + JSON.stringify(json));
						
						params.retry_count = (!params.retry_count) ? 1 : (params.retry_count)++ ;

						if( params.retry_count < 2 ){
							console.log('fail to upload video. retry upload video info.');
							setTimeout( function(){my.uploadMovieFile( params.retry_count , callback );} , 2000 );
						}else{
							// alert('영상 업로드 중 오류가 발생하였습니다.[NF1070] code='+json['error_code']);
							callback.call(this, json);

							// 종료 이벤트를 발생시킨다.
							if (params.succ_yn && (params.succ_yn != 'I')) {
								my.sendEndMovie();
							}
						}
					}
				},
				error: function(error) {
					my.isUpload = false;
					console.log(" UPLOAD ERRRO 2 : %o", error);
					alert('영상 업로드 중 오류가 발생하였습니다.[NF1070] code=' + JSON.stringify(error));
					
					// 종료 이벤트를 발생시킨다.
					if (params.succ_yn && (params.succ_yn != 'I')) {
						my.sendEndMovie();
					}
				}
			});
		}catch(e) {
			console.log(" UPLOAD ERRRO 3 : %o", e);
			alert('영상 업로드 중 오류가 발생하였습니다.[NF1070]');

			// 종료 이벤트를 발생시킨다.
			if (params.succ_yn && (params.succ_yn != 'I')) {
				my.sendEndMovie();
			}
		}
	};
};

