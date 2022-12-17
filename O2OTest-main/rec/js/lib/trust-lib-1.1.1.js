/********************************************************************* 
 * @제목 : S-Pass 영상통화 라이브러리 
 * @설명 : 다자간 영상통화 기능으로 확장된 버전
 * @변경이력
 * 1. 2020.02.27 : 최초작성
 * 2. 2020.05.15 : 영상녹화 기능 추가 및 기존 버전 대체
 * 
 * @author kswksk
 * @date 2020.02.27
 ********************************************************************/
// "use strict";
var _SPASS = null;
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
        log.log('Received Event: ' + id);

		var videos = document.getElementsByTagName('video');
        for(var i in videos) {
            videos[i].playsInline = true;
        }

        cordova.plugins.iosrtc.registerGlobals();
        cordova.plugins.iosrtc.debug.enable('iosrtc*');
        this.loadedComplete = true;

    	// spass 모듈 초기화 진행
    	if (this.spass && this.spass.isInit == false) {
    		log.log(">>>>>>>>>>>> initWebSocket call cordova APP !!! ");
    		this.spass.init();
    	}
    },

    setVideoSrcObject: function(video, stream) {
        try {
			video.src = window.URL.createObjectURL(stream);
		} catch (e) {
			video.srcObject = stream;
		}
    },

    loadedComplete: false,
    spass : null
};
/**
 * 앱 시동
 */
app.start();
/**
 * SPass 객체 생성자
 */
function SPass(sframeChat, turnUrl, callBack, aConstraints) {
	log.log("----------------------------------------");
	log.log(" SPass turnUrl : " + turnUrl);
	log.log("----------------------------------------");
	_SPASS = this;

	/** 상수 **/
	this.TYPE_LOCAL_STREAM 	= "LOCAL_STREAM";
	this.TYPE_REMOTE_STREAM = "REMOTE_STREAM";
	this.TYPE_PEER_EVENT 	= "PEER_EVENT";
	this.TYPE_LOCAL_SCREEN_STREAM = "LOCAL_SCREEN_STREAM";
	this.TYPE_REMOTE_SCREEN_STREAM = "REMOTE_SCREEN_STREAM";
	this.TYPE_RECORDING_EVENT 	= "RECORDING_EVENT";
	this.TYPE_ERR = "ERR";

	this.sframeChat = sframeChat;	// 시그럴링용
	this.sframeChatReadyCallback = null;
	this.callBack = callBack;		// 콜백함수
	this.turnUrl = turnUrl;			// 영상중계서버 주소
	this.movieInfo = null;
	this.isInit = false;
	
	// 영상 스트림
	this.localStream = null;
	this.screenStream = null;
	this.isScreenStream = false;
	this.userMap = new Map();
	// 구글턴서버 : stun:stun.l.google.com:19302, options: [{DtlsSrtpKeyAgreement:true}]
	this.peerConnectionConfig = {
	  'iceServers': [
//	    {'urls': this.turnUrl+'?transport=tcp', username:'spass', credential:'spass'}
//	    {'urls': this.turnUrl+'?transport=tcp', username:'m2soft', credential:'onechance'}
//		{'urls': ['stun:stun.l.google.com:19302']}
//	    {'urls': [ this.turnUrl+'?transport=udp'], username:'m2soft', credential:'onechance'}
	   {'urls': [ this.turnUrl+'?transport=tcp'], username:'m2soft', credential:'onechance'}
//	    {'urls': this.turnUrl, username:'m2soft', credential:'onechance'}
	  ]
	};

	this.constraints = null;
	if (aConstraints != null && aConstraints != undefined) {
		this.constraints = aConstraints;
	} else {
		if (this.isIphone()  || this.isAndroid()) {
			// 모바일 이면 전면 카메라
			this.constraints = {
				audio: true,
				video: {
					facingMode : "user"
				}
			};
		} else {
			// PC면 그냥 틀어
			this.constraints = {
					audio: true,
					video: true
				};
		}
	}
	// IP 적재
	//this.registIp();
	// 초기화 호출	
	log.log("%cSPass Init()", "color: blue, font-size: large");
	log.log("cordova----------------------------------------["+window.cordova+"]");
	if (window.cordova && app.loadedComplete == false) {
		log.log("swhan window.cordova1----------------------------------------["+window.cordova+"]");
		log.log("----------------------------------------");
		// ios Cordova 가 포함되어 있으면 초기화 보류
		app.spass = this;
		log.log("swhan window.cordova2----------------------------------------["+app.spass+"]");
	} else {
		log.log("swhan window.cordova3----------------------------------------[init]");
		this.init();
	}
	
	//this.init();

	// 녹화 관련
	this.recordUserId = "client";
	this.pcAudioStream = null;
	this.isRecord = false;
	this.isRecording = false;
	this.recordingTimeout = null;
	this.recordBlobs= null;
	this.recorder = null;
	this.mixedAudioStream = null;
	this.custAudio = null;
	this.consultAudio = null;
	this.audioContext = null;
	this.isUpload = false;
	this.remoteVideo = null;
	this.localVideo = null;
	this.canvasVideo = null;
	this.startRecordTryCnt = 0;
	
	this.drawVideo = null;	//판서녹화 테스트용
	this.drawStream = null;	//판서녹화 테스트용
	
	// 신규추가
	this.context2d = null;
	this.recordWidth = 640;			// 녹화영상 넓이
	this.recordHeight = 480;		// 녹화영상 높이
	this.canvasStream = null;
	this.recordPanCanvas = null;
	this.recordPanTimeout = null;
	this.isRecordPan = false;
	this.mouseState = 'up';
	this.upHandlerTimeout = false;
	
	this.drawCanvas = null; // 판서 녹화용 캔버스
	
	// 다시 그리기
	this.refreshVideoTimeout = null;
	this.refreshStrartTime = 0;
};

/**
 * 초기화
 */
SPass.prototype.init = function() {
	this.isInit = true;
	this.sframeChat.spassCallBack = this.chatCallBack.bind(this);
	// 영상통화 기기 확인
	this.checkDevice();
};

//iOS RTC일 경우 영상 위치 재조정 요청
SPass.prototype.refreshVideos = function (duration) {
	if (window.cordova) {
		if (!duration) {
			if (cordova.plugins && cordova.plugins.iosrtc) {
				cordova.plugins["iosrtc"].refreshVideos();
			}
		} else {
			if (this.refreshVideoTimeout) {
				clearTimeout(this.refreshVideoTimeout);
				this.refreshVideoTimeout = null;
			}
			
			this.refreshStrartTime = new Date().getTime();
			
			var fcRedrawVideo = function(dutime) {
				// 새로 그리기
				if (cordova.plugins && cordova.plugins.iosrtc) {
					cordova.plugins["iosrtc"].refreshVideos();
				}
				
				var currTime = new Date().getTime();
				var calcTime = currTime - this.refreshStrartTime;
				if (dutime > calcTime) {
					this.refreshVideoTimeout = setTimeout(fcRedrawVideo, 1000/60, dutime);
				} else {
					clearTimeout(this.refreshVideoTimeout);
					this.refreshVideoTimeout = null;
				}
			}.bind(this);
			
			// 애니메이션용
			fcRedrawVideo(duration*2);
		}
    }
}

/**
 * IP를 적재한다.
 */
SPass.prototype.registIp = function() {
	$.ajax({
		type: "post",
		url : "/noface/registTurnIP.do",
		dataType : "json",
		data : JSON.stringify({roomId:this.sframeChat.roomId}),
		contentType: "application/json; charset=utf-8",
		async : true,
		beforeSend: function() {
		},
		success : function(json){
			log.log(">>> REGIST TURN IP OK !!");
		},
		error : function(jqXHR, textStatus, errorThrown) {
			log.log("REGIST TURN IP FAIL !!");
			log.log("jqXHR.readyState : "+jqXHR.readyState);
			log.log("jqXHR.status     : "+jqXHR.status);
			log.log("jqXHR.statusText : "+jqXHR.statusText);
			log.log("textStatus : "+textStatus);
			log.debug(errorThrown);
		},
		complete : function(json) {
		}
	});
}
/**
 * 로컬미디어 초기화
 */
SPass.prototype.initMedia = function() {
	log.log(">>> initMedia : " + JSON.stringify(this.constraints));

	navigator.nativeGetUserMedia = (navigator.webkitGetUserMedia || navigator.mozGetUserMedia);

	// WebRTC 지원 여부 판단
	if (window.cordova) {
		navigator.getUserMedia(this.constraints).then(this.getUserMediaSuccess.bind(this)).catch(this.errorHandler.bind(this));
	} else if(navigator.mediaDevices.getUserMedia) {
		navigator.mediaDevices.getUserMedia(this.constraints).then(this.getUserMediaSuccess.bind(this)).catch(this.errorHandler.bind(this));
	} else {
		alert('영상통화가 지원되지 않는 브라우져 입니다.');
	}
};
/**
 * 마이크, 카메라 장치 확인
 */
SPass.prototype.checkDevice = function() {
	// 모바일 이면 체크 하지 않고 패스
	if (window.cordova) {
		this.initMedia();
		return;
	}

	// 단말에 캠 및 오디오/마이크 확인
	var isVideo = false;
	var isAudio = false;

	var device_check = function(srcs) {
		/*
		if(srcs.length == 0) {
			return;
		}
		*/
		for(var i = 0; i < srcs.length; i++) {
			var src = srcs[i];
			log.log("==========================device=============================");
			log.log(src);
			log.log("==========================device=============================");
			if (src.kind.indexOf('audio') != -1) {
				isAudio = true;
			} else if (src.kind.indexOf('video') != -1) {
				isVideo = true;
			}
		}

		var msg = "";
		if (isVideo && isAudio) {
			this.initMedia();
			return;
		} else if (isVideo == false && isAudio == false) {
			msg = "캠 및 해드셋 장치를 찾을 수 없습니다.";
		} else if (isVideo == false) {
			msg = "캠 장치를 찾을 수 없습니다.";
		} else if (isAudio == false) {
			msg = "해드셋 장치를 찾을 수 없습니다.";
		}
		this.callBack(this.TYPE_ERR, {"errMsg":msg}, this.sframeChat.userId);
	};

	if (MediaStreamTrack.getSources != undefined) {
		MediaStreamTrack.getSources(device_check.bind(this));
	} else {
		navigator.mediaDevices.enumerateDevices().then(device_check.bind(this));
	}
};
/**
 * 녹화를 수행한다.
 */
SPass.prototype.startRecording = function() {
	if (!this.isRecord || this.isRecording) {
		return;
	}
	
	if (this.canvasVideo == null) {
		this.canvasVideo = document.createElement("CANVAS");
		this.canvasVideo.width = this.recordWidth;
		this.canvasVideo.height = this.recordHeight;
		this.context2d = this.canvasVideo.getContext('2d');
		this.context2d.fillStyle = "#000000";
		this.context2d.fillRect(0, 0, this.recordWidth, this.recordHeight);
		//아래 주석은 반전목적으로 처리한것 삭제해도 무방.
		//this.context2d.translate(this.recordWidth, 0);
		//this.context2d.scale(-1, 1);
		this.canvasStream = this.canvasVideo.captureStream();
		this.canvasVideo.style.visibility = "hidden";
//		this.canvasVideo.style.visibility = "visible";
		this.canvasVideo.style.position = "fixed";
		this.canvasVideo.style.top = 0;
		this.canvasVideo.style.zIndex = 0;

		// 테스트용
		document.body.appendChild(this.canvasVideo);
	}

	var drawFunction = function() {
		// 녹화 중이 아닌데 호출되면 패스
		if (this.isRecording == false) return;
		// $(".drawingWrap")[0]
		var videoList = [$("#video_local")[0], $("#video_remote")[0]];
		
		var isShowPan = !$(".drawingWrap").parents("article").hasClass("dNone");
		if (isShowPan && !this.isRecordPan) {
			this.recordPanCanvas = this.drawCanvas;
		} else {
			this.recordPanCanvas = null;
		}

		// 행렬의 최대 크기 구하기
		var userCnt = 0;
		// 문서공유가 시작되면 1명을 더 추가
		if (this.recordPanCanvas) {
			userCnt++;
			// 판서 보여주고 있을때 고객쪽 캠 제거
			videoList.splice(1, 1);
		}
		userCnt += videoList.length;
		
		var colSize = 1;
		var rowSize = 1;
		while (true) {
			if (userCnt <= (colSize * rowSize)) {
				break;
			}

			if (colSize == rowSize) {
				colSize++;
			} else {
				rowSize++;
			}
		}

		// 그려봅시다.
		this.context2d.fillStyle = "#000000";
		this.context2d.fillRect(0, 0, this.recordWidth, this.recordHeight);

		for (var idx = 0; idx < userCnt; idx++) {
			var videoObj = null;
			if (this.recordPanCanvas && idx >= videoList.length) {
				videoObj = this.recordPanCanvas;
			} else if (idx < videoList.length) {
				videoObj = videoList[idx];
			}
			
			// 방어 코드 추가
			if (!videoObj) {
				continue;
			}
			
			var drawWidth = this.recordWidth / colSize;
			var drawHeight = this.recordHeight / rowSize;

			// 그려야하는 현재 위치 계산
			var rowIdx = Math.floor(idx / colSize);
			var colIdx = idx % colSize;

			// 좌우 반전으로 인해 덱스 값을 제조정한다.
			//colIdx = colSize - colIdx - 1;

			var videoWidth 	= 0;
			var videoHeight = 0;
			if ($(videoObj).prop("tagName") == "VIDEO") {
				videoWidth 	= videoObj.videoWidth;
				videoHeight = videoObj.videoHeight;
			} else {
				videoWidth 	= $(videoObj).attr("naturalWidth");
				videoHeight = $(videoObj).attr("naturalHeight");
			}
			
			var rate = 1.0;
			// 높이 기준으로 줄임
			rate = drawHeight / videoHeight;
			var w = videoWidth * rate;
			var h = videoHeight * rate;

			// 넓이 기준으로 줄임
			if (w > drawWidth) {
				rate = drawWidth / w;
				w = w * rate;
				h = h * rate;
			}
			// 좌표 계산
			var x = colIdx * drawWidth + ((drawWidth - w) / 2);
			var y = rowIdx * drawHeight + ((drawHeight - h) / 2);

			this.context2d.drawImage(videoObj, 0, 0, videoWidth, videoHeight, x, y, w, h);
		}

		if (this.isRecording) {
			requestAnimationFrame(drawFunction);
		}
	}.bind(this);

	// 실제 녹화 처리
	var recordProcess = function(stream) {
		this.pcAudioStream = stream.clone();
		// 음성만 남기고 삭제
		this.pcAudioStream.removeTrack(this.pcAudioStream.getVideoTracks()[0]);
		this.recordBlobs = [];

		try {
			var recUser = this.getUser(this.recordUserId);

			this.isRecording = true;
			drawFunction();

			// 상담원 마이크 녹화 실패했을때, 고객 비디오만 녹화
			if (this.audioContext == null) {
				this.audioContext = new AudioContext();
			}
			log.log("swhan startRecoding")
			log.log(this.pcAudioStream.getAudioTracks())
			log.log(recUser.remoteStream.getAudioTracks())
			this.mixedAudioStream = this.audioContext.createMediaStreamDestination();
			this.consultAudio = this.audioContext.createMediaStreamSource(this.pcAudioStream);
			this.custAudio = this.audioContext.createMediaStreamSource(recUser.remoteStream);
			this.consultAudio.connect(this.mixedAudioStream);
			this.custAudio.connect(this.mixedAudioStream);

	    	this.pcAudioStream.removeTrack(this.pcAudioStream.getAudioTracks()[0]);
	    	this.pcAudioStream.addTrack(this.mixedAudioStream.stream.getAudioTracks()[0]);
	    	this.pcAudioStream.addTrack(this.canvasStream.getVideoTracks()[0]);

	    	this.recorder = new MediaRecorder(this.pcAudioStream, {mimeType: 'video/webm; codecs=vp8', bitsPerSecond: 300 * 1000}); // 볼만한화질
	    	// this.recorder = new MediaRecorder(this.pcAudioStream, {mimeType: 'video/webm; codecs=vp8', bitsPerSecond: 20000}); // 상급
		    this.recorder.onstop = this.stopRecordingCallback.bind(this);
			this.recorder.ondataavailable = this.dataAvailableCallback.bind(this);
			this.recorder.start(10);
			log.log("%cStart Recording OK", "color: blue; font-size: large");
			this.callBack(this.TYPE_RECORDING_EVENT, "start", this.sframeChat.userId);
		} catch(e) {
			log.error("%cStart Recording Error", "color: red; font-size: large");
			log.debug(e.stack);
		    this.isRecording = false;
		    var msgObjct = {"msgType": "client", "cmd": "errorExit", "contents": "영상 통화 오류 발생"};
			sframeChat.sendMsg(msgObjct);
			this.callBack(this.TYPE_ERR, {"errMsg":"영상통화 연결에 오류가 발생하였습니다.", "errorCd" : "1009"}, this.sframeChat.userId);
		}
	}.bind(this);

	if (this.recordingTimeout) {
		clearTimeout(this.recordingTimeout);
		this.recordingTimeout = null;
	}

	// 용량이 커지면 업로드 시간이 길어져 주기적으로 업로드한 (5분 가격)
	this.recordingTimeout = setTimeout(function() {
		var data = {
			'succ_yn' : 'I',
			'movie_data' : null
		};
		this.stopRecording(data, function(){});
		this.startRecording();
	}.bind(this), 1000*60*5);

	recordProcess(this.localStream);
};

// 녹화 종료 메소드
SPass.prototype.stopRecording = function(data, callback) {
	if (!this.isRecord) {
		if (callback)
			callback.call(this, {error_code : '0000', error_msg : '정상적으로 처리하였습니다.'});
	};

	log.log('STOP RECORDING !!!! ' + JSON.stringify(data));
	var recUser = this.getUser(this.recordUserId);
	if (!recUser || !recUser.peerConnection) {
		// close 호출로 인한 스탑리코딩이 옴
		if (callback)
			callback.call(this, json);
		return;
	}

	if (this.isUpload) {
		log.log('STOP RECORDING UPLOADING WAIT !!!! ' + JSON.stringify(data));
		setTimeout(function() {
			this.stopRecording(data, callback);
		}.bind(this), 1000);
		return;
	}

	//log.log("consult_movie_data: [" + data.consult_movie_data + "]");
	if (this.isRecording == false) {
		log.log('>>>>>> NO Recording !!!!');
		var json = {error_code : '9999', error_msg : '영상정보가 존재하지 않습니다.'};
		if (callback)
			callback.call(this, json);
		return;
	}

	this.isRecording = false;
	if (this.recorder) {
		// 데이터가 겹처 재생이 않되는 문제로 인해 아래와 같이 강제로 이벤트 인터셉터함
		this.recorder.ondataavailable = function(event) {};
	}
	var blob, url, a;

	if (!this.recorder) {
		return;
	}
	this.recorder.stop();
	blob = new Blob(this.recordBlobs, {type: 'video/webm;codecs="vp8"'});
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
	this.isUpload = true;
	this.uploadMovieFile( data , function(json) {
		this.isUpload = false;
		// 종료 이벤트를 발생시킨다.
		if (data.succ_yn && (data.succ_yn != 'I')) {
			this.sendEndMovie();
		}

		if (callback) {
			callback(json);
		}
	});

	this.recorder = null;
	this.recordBlobs = null;
	this.mixedAudioStream = null;
	this.consultAudio = null;
	this.custAudio = null;
	this.pcAudioStream = null;

	if (this.recordingTimeout) {
		clearTimeout(this.recordingTimeout);
		this.recordingTimeout = null;
	}
}

// 녹화 중지
SPass.prototype.stopRecordingCallback = function(e) {
//	this.isRecording = false;
};

// MediaRecorder 데이터 생성 시작 콜백 메소드
SPass.prototype.dataAvailableCallback = function(event) {
//	this.isRecording = true;
	if(event.data && event.data.size > 0 && this.recordBlobs != null) {
		this.recordBlobs.push(event.data);
	}
}

// 영상을 업로드 한다.
SPass.prototype.uploadMovieFile = function(params, callback) {
	log.log(">>>>>>>>>> [uploadMovieFile] <<<<<<<<<<<<");
	log.debug(params.movie_data);
	log.debug(new File([params.movie_data], "record.webm"));

	if( params.succ_yn == 'I' && params.movie_data == null ){
		if(callback) {
			callback.call(this);
		}
		return;
	}

	var formdata = new FormData();
	// movieInfo 영역
	formdata.append("req_key", this.movieInfo.reqKey);
	formdata.append("movie_data", (params.movie_data !=null) ? new File([params.movie_data], "record.webm") : null );
	formdata.append("hwnno", this.movieInfo.hwnNo);
	formdata.append("hwnname", this.movieInfo.hwnName);
	formdata.append("succ_yn", params.succ_yn);
	formdata.append("etc3", params.report_yn);
	formdata.append("etc4", params.report_reason);
	formdata.forEach(function(datas, kyes){log.log(kyes + ", " + datas);});
	if(typeof params.movie_title == "undefined")
	{
		params.movie_title = "녹화영상";
	}
	formdata.append("product_type", params.movie_title);
	
	if(params.happy_call == 'Y') {
		formdata.append('happy_call', 'Y');
	}

	$.ajax({
		type		: "post",
		url			: "/noface/NF1071.do",
		data		: formdata,
		async		: (params.succ_yn == 'I'),
		processData	: false,
		contentType	: false,
		dataType	: 'json',
		success		: function(json){
			log.log('%cUpload MovieFile(/noface/NF1071.do) Success! result.', "color: blue");
			log.log(json);
			if(callback) {
				callback.call(this, json);
			}
		}.bind(this),
		error: function(error) {
			//상담완료+승인처리 해야돼는데 에러난경우 다시 시도하게.. 
			if(params.movie_title.match("상담 완료")){
				setTimeout( function(){
					$.ajax({
						type		: "post",
						url			: "/noface/NF1071.do",
						data		: formdata,
						async		: false,
						processData	: false,
						contentType	: false,
						dataType	: 'json',
						success		: function(json){
							log.log('%cUpload MovieFile(/noface/NF1071.do) Success! result.', "color: blue");
							log.log(json);
							if(callback) {
								callback.call(this, json);
							}
						}.bind(this),
						error: function(error) {
							console.log('fail to upload video. retry upload video info.1111111111111');
							callback.call(this, {error_code:'9999', error_msg:'영상업로드에 실패하였습니다.'});
						}.bind(this)
					});
				}.bind(this) , 1000 );
			}else{
				console.log('fail to upload video. retry upload video info.2222222222222');
				callback.call(this, {error_code:'9999', error_msg:'영상업로드에 실패하였습니다.'});
			}
		}.bind(this)
	});
};
/**
 *
 */
// 영상이 종료 되었음을 알린다.
SPass.prototype.sendEndMovie = function () {
	this.sframeChat.sendSPassMsg({'subType': 'movieEnd'});
	this.close();
}

// 영상 시작을 알린다.
SPass.prototype.sendStartMovie = function(userId) {
	this.sframeChat.sendSPassMsg({'subType': 'movieStart'}, userId);
}

/**
 * 사용자의 ID 기준의 SPassUser를 가져온다.
 */
SPass.prototype.getUser = function(userId, isRecv) {
	var user = this.userMap.get(userId);
	if ((user == null || user == undefined) && isRecv != undefined) {
		user = new SPassUser(this, userId, isRecv);
		this.userMap.set(userId, user);
	}
	return user;
};

/**
 * 사용자의 ID 기준의 SPassUser를 삭제한다.
 */
SPass.prototype.removeUser = function(userId) {
	var user = this.getUser(userId);
	this.userMap.delete(userId);

	if (user) {
		if (user.remoteStream) {
			try {user.remoteStream.getTracks().forEach(function (t) { t.stop();});} catch (e) {}
			user.remoteStream = null;
		}

		if (user.peerConnection) {
			try{user.peerConnection.close();} catch (e) {}
			user.peerConnection = null;
		}
	}
};

/**
 * 화면공유 시작
 */
SPass.prototype.startSharedScreen = function() {
	if (navigator.getDisplayMedia) {
		navigator.getDisplayMedia({video:true}).then(this.getDisplayMediaSuccess.bind(this)).catch(this.errorDisplayHandler.bind(this));
	} else if (navigator.mediaDevices.getDisplayMedia) {
		navigator.mediaDevices.getDisplayMedia({video: true}).then(this.getDisplayMediaSuccess.bind(this)).catch(this.errorDisplayHandler.bind(this));
	} else {
		navigator.mediaDevices.getUserMedia({video: {mediaSource: 'screen'}}).then(this.getDisplayMediaSuccess.bind(this)).catch(this.errorDisplayHandler.bind(this));
	}
}

/**
 * 화면공유 중지
 */
SPass.prototype.stopSharedScreen = function() {
	this.callBack(this.TYPE_LOCAL_STREAM, this.localStream, this.sframeChat.userId);
	this.isScreenStream = false;

	this.userMap.forEach(function(spassUser, key, map) {
		if (this.screenStream) {
			spassUser.peerConnection.removeStream(this.screenStream);
			this.screenStream = null;
		}

		try {
			var trackList = this.localStream.getTracks();
			for (var i = 0; i < trackList.length; i++) {
				spassUser.peerConnection.addTrack(trackList[i], this.localStream);
			}
		} catch (e) {
			try{spassUser.peerConnection.addStream(this.localStream);} catch (e) {}
		}
		spassUser.createOffer();
	}.bind(this));
}

/***************************************************
 * 콜백함수 영역
 ***************************************************/
/**
 * 화면공유 콜백
 */
SPass.prototype.getDisplayMediaSuccess = function(stream) {
	this.screenStream = stream;
	this.callBack(this.TYPE_LOCAL_SCREEN_STREAM, stream, sframeChat.userId);

	this.isScreenStream = true;
	this.userMap.forEach(function(spassUser, key, map) {
		spassUser.peerConnection.removeStream(this.localStream);
		try {
			var trackList = this.screenStream.getTracks();
			trackList[0].onended = function () {
				this.stopSharedScreen();
			}.bind(this);

			for (var i = 0; i < trackList.length; i++) {
				spassUser.peerConnection.addTrack(trackList[i], this.screenStream);
			}
		} catch (e) {
			try{spassUser.peerConnection.addStream(this.screenStream);} catch (e) {}
		}
		spassUser.createOffer();
	}.bind(this));
}

/**
 * 화면공유 콜백
 */
SPass.prototype.errorDisplayHandler = function(error) {
	log.error(error);
	this.callBack(this.TYPE_ERR, {"errMsg":"화면공유 기능 최소 되었거나 권한 및 브라우져 지원 여부를 확인 하시기 바랍니다.", "errorCd" : "SCR_001"}, this.sframeChat.userId);
}

/**
 * 채팅메시지 콜백 (Signaling 용)
 */
SPass.prototype.chatCallBack = function(jsonResult) {
	// 시그널링 메시지 수신
	log.log("-----------------[chatCallBack]--------------------");
	log.debug(jsonResult);
	if (jsonResult.data.subType == "RESET_SDP") {
		log.debug(jsonResult);
		if (!jsonResult.data.isIphone && this.isIphone()) {
			this.sframeChat.sendSPassMsg({'subType': 'RESET_SDP', 'isIphone':this.isIphone(), "callee": "chatCallBack"}, jsonResult.userId);
		} else {
			if(this.getUser(jsonResult.userId, true).peerConnection.iceConnectionState != "connected")
			{
				this.removeUser(jsonResult.userId);
				var user = this.getUser(jsonResult.userId, true);
				user.createOffer();
			}
		}
	} else if (jsonResult.data.subType == "movieStart") {
		// 상대방이 영상을 시작함
	} else if (jsonResult.data.subType == "movieEnd") {
		// 상대반이 영상을 종료함
		this.removeUser(jsonResult.userId);
	} else if (jsonResult.data.subType == "SIGNALING") {
		// Offer가 오면 종료 시키고 다시 만듬
		log.log("offer1 jsonResult ↓↓↓↓");
		log.debug(jsonResult);
		if (jsonResult.data.sdp.type == "offer") {
			this.removeUser(jsonResult.userId);
		}
		log.log("offer2  jsonResult ↓↓↓↓");
		log.debug(jsonResult);
		var user = this.getUser(jsonResult.userId, false);
		if (user) {
			log.log("offer3  user ↓↓↓↓");
			log.debug(user);
			user.setRemoteDescription(jsonResult);
			user.isScreenStream = jsonResult.data.isScreenStream;
		}
	} else if (jsonResult.data.subType == "ICE") {
		var user = this.getUser(jsonResult.userId);
		if (user) {
			user.addIceCandidate(jsonResult);
		}
	} else if (jsonResult.data.subType == "CHAT_READY") {
		if (this.sframeChatReadyCallback) {
			this.sframeChatReadyCallback();
		}
	} else if (jsonResult.type == sframeChat.TYPE_CLOSE) {
		// 채팅 서버가 종료되면, 영상도 정리함!
		this.removeUser(jsonResult.userId);
	}
};

SPass.prototype.startOffer = function() {
	this.sframeChat.roomUserList.forEach(function (uId) {
		if (uId != this.sframeChat.userId) {
			setTimeout(function () {
				var user = this.getUser(uId, true);
				user.createOffer();
			}.bind(this), 1000);
		}
	}.bind(this));
};

/**
 * 로컬 미디어 생성 완료
 */
SPass.prototype.getUserMediaSuccess = function(stream) {
	// 화면을 그릴 수 있도록 이벤트 발생
	this.localStream = stream;
	this.callBack(this.TYPE_LOCAL_STREAM, stream, sframeChat.userId);


	log.log("%cLocal Stream", "color: blue; font-weight: bold;");
	log.log(stream);
	log.log(stream.getTracks());

	/*
	var startOffer = function() {
		// 통신 준비가 완료된 상태일 경우
		if (this.sframeChat.roomUserList.length > 1) {
			// 이미 접속된 사용자가 존재함! 시그럴링 시작
			this.sframeChat.roomUserList.forEach(function (uId) {
				if (uId != this.sframeChat.userId) {
					if (this.isIphone()) {
						// 원인을 알수 없는 버그가 있어서 사파리 일때는 오퍼를 하지 않음
						this.sframeChat.sendSPassMsg({'subType': 'RESET_SDP', 'isIphone':this.isIphone(), "callee": "getUserMediaSuccess"}, uId);
					} else {
						var user = this.getUser(uId, true);
						user.createOffer();
					}
				}
			}.bind(this));
		}

		// 다시 Offer를 보내지 않도록 처리
		this.sframeChatReadyCallback = null;
	}.bind(this);

	// 시그럴링 시작
	if (this.sframeChat.isConnected) {
		startOffer();
	} else {
		this.sframeChatReadyCallback = startOffer;
	}
	*/
};

/**
 * 미디어 생성 중 오류
 */
SPass.prototype.errorHandler = function(error) {
	log.error(error);
	this.callBack(this.TYPE_ERR, {"errMsg":"카메라 및 오디오 사용 권한이 필요합니다. 확인 후 다시 이용하시기 바랍니다."}, this.sframeChat.userId);
};

// 자원반납처리
SPass.prototype.close = function(callBack) {
	if (this.sframeChat.roomUserList.length > 1) {
		// 이미 접속된 사용자가 존재함! 시그럴링 시작
		this.sframeChat.roomUserList.forEach(function (uId) {
			this.removeUser(uId);
		}.bind(this));
	}

	// 로컬스트림 종료
	if (this.localStream) {
		try {
			this.localStream.getTracks().forEach(function (t) { t.stop();});
		} catch (e) {}
	}

	// 콜백 넣어줌
	if (callBack) {
		callBack();
	}
};
/***************************************************
 * 기능함수 영역
 ***************************************************/
/**
 * iPhone 여부 확인
 */
SPass.prototype.isIphone = function() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	if (userAgent.match('iphone')) {
		return true;
	} else {
		return false;
	}
};

/**
 * 사파리 브라우져 여부 확인
 */
SPass.prototype.isSafari = function() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	return userAgent.indexOf('macintosh') > -1;
};

/**
 * 안드로이드 여부 확인
 */
SPass.prototype.isAndroid = function() {
	var userAgent = navigator.userAgent.toLowerCase();   // android, iphone
	return !!userAgent.match('android');
};

/**
 * 녹화할 판서 캔버스를 셋한다
 */
SPass.prototype.setDrawCanvas = function(canvas) {
	this.drawCanvas = canvas;
};

/***************************************************
 * 다중사용자를 위한 PeerConnection 분리
 ***************************************************/
function SPassUser(spass, userId, isOffer) {
	this.spass = spass;
	this.userId = userId;
	this.peerConnection = null;
	this.isOffer = isOffer;
	this.remoteStream = null;
	this.isScreenStream = false;

	// 영상관련
	this.audioSender = null;
	this.videoSender = null;
	this.init();

	this.isAddStream = false;
	this.isOntrack = false;
};

/**
 * 초기화
 */
SPassUser.prototype.init = function() {
	if (RTCPeerConnection != undefined) {
		this.peerConnection = new RTCPeerConnection(this.spass.peerConnectionConfig);
	} else {
		this.peerConnection = new webkitRTCPeerConnection(this.spass.peerConnectionConfig);
	}

	this.peerConnection.onicecandidate = this.gotIceCandidate.bind(this);
	this.peerConnection.oniceconnectionstatechange = this.changePeerConnection.bind(this);
	this.peerConnection.onaddstream = function () {
		this.isAddStream = true;
	}.bind(this);
	this.peerConnection.ontrack = function () {
	    this.isOntrack = true;
	}.bind(this);

	var stream = this.spass.isScreenStream ? this.spass.screenStream : this.spass.localStream;
	if (this.peerConnection.addTrack) {
		this.peerConnection.ontrack = function(ev) {
			this.gotRemoteStream(ev);
		}.bind(this);

		// Safari 대응
		try {
			var trackList = stream.getTracks();
			for (var i = 0; i < trackList.length; i++) {
				this.peerConnection.addTrack(trackList[i], stream);
			}
		} catch (e) {
			// 안드로이드 코도바에서 오류나서 이렇게 처리
			try{this.peerConnection.addStream(stream);} catch (e) {}
		}
	} else {
		this.peerConnection.onaddstream = this.gotRemoteStream.bind(this);
		try{this.peerConnection.addStream(stream);} catch (e) {}
	}
};

/**
 * 오퍼를 생성해서 보낸다.
 */
SPassUser.prototype.createOffer = function() {
	log.log("swhan create offer생성 하여 발송1");
	log.debug("%cCreateOffer!!!!", "color:blue; font-size:large");
	this.peerConnection.createOffer().then(this.createdDescription.bind(this)).catch(this.errorHandler.bind(this));
};

/**
 * Offer 도착 -> Answer 생성 후 발송
 */
SPassUser.prototype.setRemoteDescription = function(jsonResult) {
	log.log("SPassUser.setRemoteDescription --> swhan answer 생성 하여 발송1 --> jsonResult");
	log.log(jsonResult);
	this.peerConnection.setRemoteDescription(new RTCSessionDescription(jsonResult.data.sdp)).then(function() {
		log.log("SPassUser.setRemoteDescription --> swhan answer 생성 하여 발송2 --> jsonResult.data.sdp.type: ["+jsonResult.data.sdp.type + "]");
		// Only create answers in response to offers
		if(jsonResult.data.sdp.type == 'offer') {
			log.log("SPassUser.setRemoteDescription --> swhan answer 생성 하여 발송3 --> jsonResult");
			log.log(jsonResult);
			this.peerConnection.createAnswer().then(this.createdDescription.bind(this)).catch(this.errorHandler.bind(this));
		}
	}.bind(this)).catch(this.errorHandler.bind(this));
};

/**
 * Candidate 도착 -> WebRTC에 셋팅
 */
SPassUser.prototype.addIceCandidate = function(jsonResult) {
	this.peerConnection.addIceCandidate(new RTCIceCandidate(jsonResult.data.ice)).catch(this.errorHandler.bind(this));
};

/**
 * ICE 정보 전송
 */
SPassUser.prototype.gotIceCandidate = function(event) {
	log.log("------------------ [gotIceCandidate] --------------------");
	log.debug(event);
	log.log("---------------------------------------------------------");
	if(event.candidate != null) {
		this.spass.sframeChat.sendSPassMsg({'subType': 'ICE', 'ice': event.candidate}, this.userId);
	}
};

/**
 * 원격지의 스트리밍 수신
 */
SPassUser.prototype.gotRemoteStream = function (event) {
	log.log("------------------ [gotRemoteStream] --------------------");
	log.debug(event);
	log.log("---------------------------------------------------------");

	var stream = null;
	if (event.stream) {
		stream = event.stream;
	} else {
		stream = event.streams[0];
	}

	this.remoteStream = stream;
	// 연결되면 화면을 그리도록 변경
	if (!this.spass.isIphone()) {
		if (this.isScreenStream) {
			this.spass.callBack(this.spass.TYPE_REMOTE_SCREEN_STREAM, stream, this.userId);
		} else {
			this.spass.callBack(this.spass.TYPE_REMOTE_STREAM, stream, this.userId);
		}
	}
	
	// 영상이 정상적으로 연결되면 녹화 시작
	if (this.spass.isRecord && this.userId == this.spass.recordUserId) {
		this.spass.startRecording();
	}
};
/**
 * PeerConnection 상태 변경
 */
SPassUser.prototype.changePeerConnection = function(e) {
	log.log("------------------ [changePeerConnection] --------------------");
	log.debug(e);
	log.log("--------------------------------------------------------------");

	var ststus = null;
	if (e.target) {
		ststus = e.target.iceConnectionState;
	} else if (e._target) {
		ststus = e._target.iceConnectionState;
	} else {
		log.debug('%c[peerStateChange] ERROR Not supported API !!! : '+ststus, 'color:red; font-size:large');
	}

	log.log('%c[peerStateChange] STAT : '+ststus, 'color:green; font-size:large');

	if (ststus == 'connected') {
		// iOS 일경우
		if (window.cordova) {
			$('html, body').css('background-color', 'transparent');
		}

		// 영상 시작되었을 경우
		if (this.isScreenStream) {
			this.spass.callBack(this.spass.TYPE_REMOTE_SCREEN_STREAM, this.remoteStream, this.userId);
		} else {
			this.spass.callBack(this.spass.TYPE_REMOTE_STREAM, this.remoteStream, this.userId);
		}

		if (window.spassLog) {
			window.spassLog.addPeer(this.peerConnection, {'local_userId' : this.spass.sframeChat.userId, 'remote_userId' : this.userId, 'roomId' : this.spass.sframeChat.roomId});
		}

		/* 버그로인해 주석으로 처리
		// 녹화 시작
		if (this.spass.isRecord && this.userId == this.spass.recordUserId) {
			this.spass.startRecording();
		}
		*/
	}else if (ststus == 'completed') {
		// 정상적으로 완전히 연결됨
	} else if (ststus == 'disconnected') {
		// 네트워크 문제로 끊어짐
	} else if (ststus == 'close') {
		// 완전종료 됨
		// 녹화 종료
		log.log("[REC] - STOP : " + this.userId + " / " + this.spass.recordUserId);
		if (this.spass.isRecord && this.userId == this.spass.recordUserId) {
			var data = {
				'succ_yn' : 'I',
				'movie_data' : null
			};
			this.stopRecording(data, function(){});
		}
	} else if (ststus == 'failed') {
		// 완전 종료
		this.spass.removeUser(this.userId);
		// this.spass.sframeChat.sendSPassMsg({'subType':'RESET_SDP', 'isIphone':this.spass.isIphone(), "callee": "changePeerConnection"}, this.userId);
	}

	this.spass.callBack(this.spass.TYPE_PEER_EVENT, ststus, this.userId);
};

/**
 * 미디어 생성 중 오류
 */
SPassUser.prototype.errorHandler = function(error) {
	log.error(error)
	this.spass.removeUser(this.userId);
	// this.spass.sframeChat.sendSPassMsg({'subType': 'RESET_SDP', 'isIphone':this.spass.isIphone(), "callee": "errorHandler"}, this.userId);
};

/**
 * 로컬 SDP 생성
 */
SPassUser.prototype.createdDescription = function (description) {
	log.log("------------------ [createdDescription] --------------------");
	log.debug(description);
	log.log("------------------------------------------------------------");

	this.peerConnection.setLocalDescription(description).then(function() {
		var strDesc = JSON.stringify(description);
		if (strDesc.indexOf("m=video") == -1 || strDesc.indexOf("m=audio") == -1) {
			// 만약 잘못된정보가 추출되면 다시 생성
			this.spass.startOffer();
			return;
		}

		this.spass.sframeChat.sendSPassMsg({'subType': 'SIGNALING', 'sdp': this.peerConnection.localDescription, 'isScreenStream':this.spass.isScreenStream}, this.userId);
	}.bind(this)).catch(this.errorHandler.bind(this));
};