var sframeChat = null;
var spass = null;


function loadPage() {
    var wssUrl = "wss://spass.hancy.kr:8200";
    var turnUrl = "turn:spass.hancy.kr:8100";
    var today = new Date();
    sframeChat = new SFrameChat("test-room", "client", recvDataProcess);
    sframeChat.connect(wssUrl);
    // 마지막 인자가 전체 디바이스에서 가져올지 여부임
    spass = new SPass(sframeChat, turnUrl, onSpassCallBack);
}

function recvDataProcess(json) {
    console.log("------------- recvDataProcess -------------");
    console.log(json);
    if (json.type == "CLOSE") {
    isFirst = false;
    }
}


var isFirst = false;
function onSpassCallBack(type, data, userId, device) {
    console.log("------------- onSpassCallBack -------------");
    console.log(" >>   type : " + type);
    console.log(" >>   data : %o", data);
    console.log(" >> userId : " + userId);
    console.log(data);

    if (type == spass.TYPE_LOCAL_STREAM) {
        var video = $("#video_local")[0];
        try {
            video.srcObject = data;
        } catch (e) {
            video.src = window.URL.createObjectURL(data);
        }
        spass.videoObj = video;
    } else if (type == spass.TYPE_DISPLAY_STREAM) {
        var videoObj = $("#video_shared");
        var video = videoObj[0];
        try {
        video.srcObject = data;
    } catch (e) {
    video.src = window.URL.createObjectURL(data);
    }
    var user = spass.getUser(userId);
    user.videoObj = video;
    } else if (type == spass.TYPE_DISPLAY_CLOSE) {
        console.log("끈김!!!");
    } else if (type == spass.TYPE_REMOTE_STREAM) {
// 		var videoObj = null;

// 		if (!isFirst) {
// 			isFirst = true;
    var videoObj = $("#video_remote1");
// 		} else {
// 			videoObj = $("#video_remote2");
// 		}
    var video = videoObj[0];
    try {
        video.srcObject = data;
    } catch (e) {
        video.src = window.URL.createObjectURL(data);
    }
    var user = spass.getUser(userId);
    user.videoObj = video;

    console.log(">>>> device : %o", device);
    } else if (type == spass.TYPE_PEER_EVENT) {
    // 영상 상태에 대한 이벤트 발생
    if (data == "checking") {
    // 접속 체크
    } else if (data == "connected") {
    // 연결됨
    } else if (data == "disconnected") {
    // 끈김
    } else if (data == "failed" || data == 'close') {
    // 접속 실패
    }
    } else if (type == spass.TYPE_ERR) {
    // 캠 또는 마이크 없음음
    alert(data.errMsg);
    }
}
