/*********************************************************************
 * @제목 : S-Pass 영상통화 로그 추출 라이브러리
 * @설명 : 다자간 영상통화 기능으로 확장된 버전 
 * @변경이력
 * 1. 2020.06.16 : 최초작성
 * 
 * @author kswksk
 * @date 2020.06.15
 ********************************************************************/
function SPassLog() {
	window.spassLog = this;
	
	// 상수 정의
	this.KEY_SLOG_ENCRYTION 	= "암호화알고리즘";
	this.KEY_SLOG_AVA_BANDWITH 	= "전송가능대역폭(byte)";
	this.KEY_SLOG_BANDWITH_SPEED= "전송속도(byte)";
	this.KEY_SLOG_LOCAL_CODEC	= "로컬코덱";
	this.KEY_SLOG_REMOTE_CODEC	= "원격코덱";
	this.KEY_SLOG_LOCAL_IP 		= "로컬IP";
	this.KEY_SLOG_REMOTE_IP 	= "원격IP";
	this.KEY_SLOG_PROTOCOL 		= "전송프로토콜";
	this.KEY_SLOG_LOCAL_SCREEN 	= "로컬해상도";
	this.KEY_SLOG_REMOTE_SCREEN = "원격해상도";
	this.KEY_SLOG_AUDIO_SEND_DATA 	= "음성보낸데이터량(byte)";
	this.KEY_SLOG_AUDIO_RECV_DATA 	= "음성수신데이터량(byte)";
	this.KEY_SLOG_VIDEO_SEND_DATA 	= "영상보낸데이터량(byte)";
	this.KEY_SLOG_VIDEO_RECV_DATA 	= "영상수신데이터량(byte)";
	this.KEY_SLOG_AUDIO_LATENCY	= "음성처리속도(ms)";
	this.KEY_SLOG_VIDIO_LATENCY	= "영상처리속도(ms)";
	this.KEY_SLOG_AUDIO_LOSS	= "음성유실량(byte)";
	this.KEY_SLOG_VIDIO_LOSS	= "영상유실량(byte)";
	
	// 로그를 출력하기 위한 정보 저장 
	this.peerList = [];
	
	this.callBack = null;
	this.milisec = 0;
	this.isStart = false;
}

// 추가
SPassLog.prototype.addPeer = function(peerConnection, userInfo) {
	userInfo.isStartLog = false;
	peerConnection._userInfo = userInfo;
	this.peerList[this.peerList.length] = peerConnection;
	
	if (this.isStart) {
		userInfo.isStartLog = true;
		this._startStatus(peerConnection);
	}
}

// 로깅 시작
SPassLog.prototype.start = function(_callBack, miliSec) {
	this.callBack = _callBack;
	this.milisec = miliSec;
	this.isStart = true;
	for (var i = 0; i < this.peerList.length; i++) {
		if (this.peerList[i]._userInfo.isStartLog == false) {
			this.peerList[i]._userInfo.isStartLog = true;
			this._startStatus(this.peerList[i]);
		}
	}
}

// 내부 함수
SPassLog.prototype._startStatus = function(peerConnection) {
	// 로그 출력
	getStats(peerConnection, function(stats) {
		if (this.callBack) {
			var result = {};
			
			try{result[this.KEY_SLOG_ENCRYTION] 		= stats.encryption; /*암호화알고리즘*/} catch (e) {}
			try{result[this.KEY_SLOG_AVA_BANDWITH] 		= stats.bandwidth.availableSendBandwidth; /*전송가능대역폭*/} catch (e) {}
			try{result[this.KEY_SLOG_BANDWITH_SPEED]	= stats.bandwidth.speed; /*실제 속도*/} catch (e) {}
			try{result[this.KEY_SLOG_LOCAL_CODEC]		= stats.audio.send.codecs[0] + ", " +  stats.video.send.codecs[0]; /*로컬코덱*/} catch (e) {}
			try{result[this.KEY_SLOG_REMOTE_CODEC]		= stats.audio.recv.codecs[0] + ", " +  stats.video.recv.codecs[0]; /*원격코덱*/} catch (e) {}
			try{result[this.KEY_SLOG_LOCAL_IP] 			= stats.connectionType.local.ipAddress[0]; /*로컬IP*/} catch (e) {}
			try{result[this.KEY_SLOG_REMOTE_IP] 		= stats.connectionType.remote.ipAddress[0]; /*원격IP*/} catch (e) {}
			try{result[this.KEY_SLOG_PROTOCOL] 			= stats.connectionType.transport; /*전송방식*/} catch (e) {}
			try{result[this.KEY_SLOG_LOCAL_SCREEN] 		= stats.resolutions.send.width + "x" + stats.resolutions.send.height; /*로컬해상도*/} catch (e) {}
			try{result[this.KEY_SLOG_REMOTE_SCREEN] 	= stats.resolutions.recv.width + "x" + stats.resolutions.recv.height; /*원격해상도*/} catch (e) {}
			try{result[this.KEY_SLOG_AUDIO_SEND_DATA] 	= stats.audio.bytesSent; /*음성보낸데이터량*/} catch (e) {}
			try{result[this.KEY_SLOG_AUDIO_RECV_DATA] 	= stats.audio.bytesReceived; /*음성수신데이터량*/} catch (e) {}
			try{result[this.KEY_SLOG_VIDEO_SEND_DATA] 	= stats.video.bytesSent; /*영상보낸데이터량*/} catch (e) {}
			try{result[this.KEY_SLOG_VIDEO_RECV_DATA] 	= stats.video.bytesReceived; /*영상수신데이터량*/} catch (e) {}
			try{result[this.KEY_SLOG_AUDIO_LATENCY]		= stats.audio.latency; /*음성처리속도*/} catch (e) {}
			try{result[this.KEY_SLOG_VIDIO_LATENCY]		= stats.video.latency; /*영상처리속도*/} catch (e) {}
			try{result[this.KEY_SLOG_AUDIO_LOSS]		= stats.audio.packetsLost; /*음성유실량*/} catch (e) {}
			try{result[this.KEY_SLOG_VIDIO_LOSS]		= stats.video.packetsLost; /*영상유실량*/} catch (e) {}
			
			result["로컬사용자ID"] = peerConnection._userInfo.local_userId;
			result["원격사용자ID"] = peerConnection._userInfo.remote_userId;
			result["개설방ID"] = peerConnection._userInfo.roomId;
			
			this.callBack(result);
		}
	}.bind(this), this.milisec);
}
