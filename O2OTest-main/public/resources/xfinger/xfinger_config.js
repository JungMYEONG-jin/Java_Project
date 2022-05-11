/**
 * auth    : jmkim
 * file    : m2soft_config.js
 * create  : 2016-11-15
 * modify  : 2016-12-27
 * version : 1.0.7
 * desc    : variable configure 관리
 */

var xfinger_session;
var consultant_audio_stream;
var recorder, record_blobs;
var audioContext;
var mixedAudioStream, mixedMediaStream;
var consultant_audio, customer_audio;
var periodic_stats;
var call_type = 'call-audiovideo' // ('audio' | 'audiovideo')
var video_remote, video_local; // video html element
var media_bitrate = {
	'video': 512,
	'audio': 64
}
var video_width = 640;
var video_height = 480;
var video_size = {
    'minWidth': video_width,
	'minHeight': video_height,
	'maxWidth': video_width,
	'maxHeight': video_height,
	'maxFrameRate': 5
};

var READYCALL_TIMEOUT = 30; // second
// video data condition send info
var last_send_bps = 0;
var last_recv_bps = 0;
var threshold_sec = 3; // 3 sec
var threshold_count = 0;
var getStatTime = 1000 * 1; // 1 sec
var network_health_check_time = 0;
var network_status_data = { 'send': {}, 'recv': {} };

// ApplicationServer Event Callback Object
var ConfigASCallback = {
    events: {
	    open: null,
		error: null,
		close: null,
		message: null
	}
};

// SIP Event Callback Object
var ConfigSipCallback = {
    success_callbacks: {
	    initialize: null,
		register: null,
		invite: null,
		accept: null,
		reject: null,
		bye: null
	},
	failed_callbacks: {
	    initialize: null,
		register: null,
		invite: null,
		accept: null,
		reject: null,
		bye: null
	},
	events: {
	    receiving_call: null,
		connected_call: null,
		disconnected_call: null,
		stopped_stack: null,
		authorize_register: null
	}
};

// SIP 스택 config
var oConfigStack = {
    realm: 'realm',
	impi: 'sip_id',
	impu: "sip:" + 'sip_id' + "@" + 'releam',
	password: 'sip_p/w',
	display_name: 'sip_id',
	websocket_proxy_url: 'webrtc g/w url',
	outbound_proxy_url: 'sip proxy url',
	ice_servers: '',
	bandwidth: media_bitrate,
	video_size: video_size,
	enable_early_ims: true,
	enable_rtcweb_breaker: false,
	enable_media_stream_cache: false,
	events_listener: { events: '*', listener: onSipEventStack },
	sip_headers: [
		{ name: 'User-Agent', value: 'shinhan / r1' }
	]
};
// SIP Register config
var oConfigRegister = {
	expires: SIP_REGISTER_EXPIRE_TIME,
	events_listener: { events: '*', listener: onSipEventSession },
	sip_caps: [
		{ name: '+audio', value: null }
	]
		
};
// SIP Invite config
var oConfigCall = {
    video_local: video_local,
	video_remote: video_remote,
	audio_local: "",
	audio_remote: "",
	bandwidth: media_bitrate,
	video_size: video_size,
	events_listener: { events: '*', listener: onSipEventSession },
	sip_caps: [
		{ name: '+sip.ice' } // rfc 5768?
	]

};
var mediaConstraints = {};
mediaConstraints.video = {
    mandatory: video_size,
	optional: [
		{ sourceId: "" }
	]
};


mediaConstraints.audio = {
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

var consultantMicmediaConstraints = {};
consultantMicmediaConstraints.audio = mediaConstraints.audio;
