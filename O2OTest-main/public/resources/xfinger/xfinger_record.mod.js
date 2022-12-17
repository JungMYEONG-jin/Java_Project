/**
 * auth    : jmkim
 * file    : m2soft_record.js
 * create  : 2016-11-21
 * modify  : 2016-12-16
 * version : 1.0.4
 * desc    : Media Record API
 */

/**
 * @ desc : 상담원 마이크 스트림 캡쳐 메소드
 */
var ConsultantAudioCapture = function() {
    console.info('%c Consultant Mic Capture Start', 'color:blue; font-weight:bold;');
	navigator.nativeGetUserMedia(consultantMicmediaConstraints, function(stream) {
	    consultant_audio_stream = stream;	
        console.info('%c Consultant Mic Capture Start Success', 'color:gray; font-weight:bold;');
	}, function(error) {
	    console.error("Consultant Mic Capture Error");
	    consultant_audio_stream = null;
	});
}

/**
 * @ desc : 상담원 마이크(오디오) + 고객 영상(비디오/오디오) 녹화 시작 메소드
 */
var RecordingStart = function() {
    console.info('%c RecordingStart', 'color:blue; font-weight:bold;');
	return;
    /*
	console.log("local media stream");
	console.error(consultant_audio_stream);  // use audio
	console.log("remote media stream");
	console.error(xfinger_session.o_remote_stream); // use audio video
	*/

    // 상담원 마이크 녹화 실패했을때, 고객 비디오만 녹화
    if(consultant_audio_stream == null) {
    	try {
    	    record_blobs = [];
    	    recorder = new MediaRecorder(xfinger_session.o_remote_stream, {mimeType: 'video/webm; codecs=vp8'});
    	    recorder.onstop = RecordingDataStop;
    	    recorder.ondataavailable = RecordingDataAvailable;
    	    recorder.start();
            console.info('%c RecordingStart Success', 'color:gray; font-weight:bold;');
    	} catch(e) {
    	    console.error("Consultant Recording Start Error");
    	    console.error(e.message);
    	}
    	// 상담원 마이크 녹화 성공 했을때 
    } else {
    	var audioContext = new AudioContext();
    	var mixedAudioStream, mixedMediaStream;
    	var consultant_audio, customer_audio;

    	try {
    	    mixedAudioStream = audioContext.createMediaStreamDestination();
    	    consultant_audio = audioContext.createMediaStreamSource(consultant_audio_stream);
    	    customer_audio = audioContext.createMediaStreamSource(xfinger_session.o_remote_stream);

    	    consultant_audio.connect(mixedAudioStream);
    	    customer_audio.connect(mixedAudioStream);

    	    consultant_audio_stream.removeTrack(consultant_audio_stream.getAudioTracks()[0]);
    	    consultant_audio_stream.addTrack(mixedAudioStream.stream.getAudioTracks()[0]);
    	    consultant_audio_stream.addTrack(xfinger_session.o_remote_stream.getVideoTracks()[0]);

    	    record_blobs = [];
    	    recorder = new MediaRecorder(consultant_audio_stream, {mimeType: 'video/webm; codecs=vp8'});
    	    recorder.onstop = RecordingDataStop;
    	    recorder.ondataavailable = RecordingDataAvailable;
    	    recorder.start();
            console.info('%c RecordingStart Success', 'color:gray; font-weight:bold;');
    	} catch(e) {
    	    console.error("Consultant Recording Start Error");
    	    console.error(e.message);
    	}
    }
}

/**
 * @ desc : 녹화 종료 메소드
 */
var RecordingStop = function( data , callback ) {
    console.info('%c RecordingStop', 'color:blue; font-weight:bold;');
    console.log(data);

	data.movie_data = "";
	updateResultAndRecord( data , callback );
	return;
	var blob, url, a;
	try {
		recorder.stop();

		blob = new Blob(record_blobs, {type: 'video/webm'});

		/*
		url = window.URL.createObjectURL(blob);
		a = document.createElement('a');
		a.style.display = "none";
		a.href = url;
		a.download = "record_file.webm";
		document.body.appendChild(a);
		a.click();
		*/
		
		record_blobs = [];
        console.info('%c RecordingStop Success', 'color:gray; font-weight:bold;');
	} catch(e) {
	    console.error("Consultant Recording Stop Error");
		data.movie_data = blob;
		updateResultAndRecord( data , callback );
	}
}

/**
 * @ desc : MediaRecorder 데이터 생성 시작 콜백 메소드
 */
var RecordingDataAvailable = function(event) {
	if(event.data && event.data.size > 0) {
		record_blobs.push(event.data);
	}
}

/**
 * @ desc : MediaRecorder 종료 콜백 메소드
 */
var RecordingDataStop = function(e) {
	//record_blobs = [];
}

