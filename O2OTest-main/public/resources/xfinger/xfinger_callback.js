var documentWin , whiteboardWin;
window.onunload = window.onbeforeunload = function() {
    if(getConfType == TYPE_PEER_TO_PEER_CONFERENCE) {
	    updateExitingInfo( 1 , currentUser.length );
    }
	releaseCall();
	releaseRegister();
	
	//문서공유 분리된 팝업창
	if( documentWin != null ){
		documentWin.close();
	}
	//화이트보드 분리된 팝업창
	if( whiteboardWin != null ){
		whiteboardWin.close();
	}
	//메일을 통해서 참여하였을 경우 (팝업으로 로그인하여 회의에 참여한다.)
	if( getEnteredByPopup == "1"){
        location.href = '/member/logout?logout=1';
	}
	return;
};

function funcExit() {
	// for iPad - jmkim 
	/*
	if( getOCAppType() == OC_IOS_APP ) {
		if(getConfType == TYPE_PEER_TO_PEER_CONFERENCE) {
			updateExitingInfo( 1 , currentUser.length );
		}
		isDisconnectedByMyself = true;
		xfinger.disconnectCallRequest();
	}
	updateExitingInfo( 1 , currentUser.length );
	location.href = '/main/form';
    var userGrade = ParticipantType[ParticipantUri.indexOf(getUserSIPID)];
    if( userGrade == "2" ) { // Guest 의 경우 로그아웃 페이지이동
        location.href = '/member/logout?logout=1';
    } 
    else { // 일반 사용자일 경우 
        if( getDeviceType() == "PC" ){
            //PC : 목록페이지로 
            location.href="/main/form";
        }
        else{
            //모바일 - 목록상세페이지로 
            window.history.back();
        }
    }
	*/
    window.history.back();
	return;
}

function funcNoRefresh() {
	/* CTRL + N키 막음. */
	if ((event.keyCode == 78) && (event.ctrlKey == true)) {
	    event.keyCode = 0;
	    return false;
	}
	/* F5 번키 막음. */
	if(event.keyCode == 116) {
	    event.keyCode = 0;
	    return false;
	}
}

function isFullScreen() {
	if(document.fullscreen || document.mozFullScreen || document.webkitIsFullScreen || document.msFullscreenElement) { 
		return true;
	} else { 
		return false;
    }
}

// 레지스터 타이머
var Timer = null;
function RegConnectTimeout(flag) {
	var time = 1000 * 60 // 60sec

	/** Timer Start **/
	if(flag == true) {
		Timer = setTimeout(function() {
			onDisconnectedCall(null, 'ServerError');
		}, time);
	/** Timer Stop **/
	} else if(flag == false) {
		if(Timer != null) {
			clearTimeout(Timer);
			Timer = null;
		}	
	}
}

// ICE 타이머
var Timer2 = null;
function ICEConnectTimeout(flag) {
	var time = 1000 * 60 // 60sec

	/** Timer Start **/
	if(flag == true) {
		Timer2 = setTimeout(function() {
			onDisconnectedCall(null, 'ICEError');
		}, time);
	/** Timer Stop **/
	} else if(flag == false) {
		if(Timer2 != null) {
			clearTimeout(Timer2);
			Timer2 = null;
		}	
	}
}

// 문서공유 타이머
var Timer3 = null;
function DocumentTimeout(flag) {
	var time = 1000 * 30; // 30sec

	/** Timer Start **/
	if(flag == true) {
		Timer3 = setTimeout(function() {
			// 에러처리
			funcCloseDialog();
			funcDocumentStop();
			onClickCallback('btn_media');
			data = { title : i18n('msg.text225'), mesg : i18n('msg.text313') };
			funcLoadDialog("infoDialog", data);
			funcSetDialogCloseTimer('infoDialog');
			DocumentConversion = false;
		}, time);
	/** Timer Stop **/
	} else if(flag == false) {
		if(Timer3 != null) {
			clearTimeout(Timer3);
			Timer3 = null;
		}
	}
}

var receiveHistory = false;

function funcCMPassPermission() {
	var index = SelectUserIndex;
	Log('info', '[Function Call] : funcCMPassPermission');
	Log('info', 'Target : ' + ParticipantUri[index]);
	if(ParticipantUri[index] === getUserSIPID) {
		//("본인에게 위임할 수 없습니다.");
		return;
	}
	var json = { type : 'LeaderRequest',  agent : getUserID, target : ParticipantName[index], action : 'PassLeader', agentsipid : getUserSIPID, targetsipid : ParticipantUri[index] };
	Log('info', 'Send Request Message : LeaderReques');
	try {
		ChatConnection.send(JSON.stringify(json));
	} catch(e) {
		Log('error', ' + ChatServer Communication Error : ' + e);
		RetryConnectChat();	
	}
}

function funcSendLeaderInfoMesg() {
	var data = {
		"owner" : "m2soft",
		"command" : "becomes_leader",
		"sip_uri" : getUserSIPID + "@" + getUserRealm
	}
	sendInfoMessage(data);
}

function funcCMKickout() {
	var index = SelectUserIndex;
	Log('info', '[Function Call] : funcCMKickout');
	Log('info', 'Target : ' + ParticipantName[index]);
	if(ParticipantUri[index] === getUserSIPID) {
		//("본인을 강제퇴장 시킬 수 없습니다.");
		return;
	}

	var data = {
		"owner" : "m2soft",
		"command" : "kickout",
		"sip_uri" : ParticipantUri[index] + "@" + getUserRealm,
		"reason" : i18n('msg.text288')
	}
	sendInfoMessage(data);
}

function funcCMmuteSound() {
	var index = SelectUserIndex;
	Log('info', '[Function Call] : funcCMmuteSound');
	Log('info', 'Target : ' + ParticipantName[index]);
	// Info 메시지를 통한 사용자 Mute
	// Log('info', '[Function Call] : funcCMmuteSound');
	// var uri = ParticipantUri[SelectUserIndex];
 //    Log('info', 'Target : ' + uri);
	// data = {
	// 	"owner" : "m2soft",
	// 	"command" : "audio_toggle",
	// 	"sip_uri" : uri + "@" + getUserRealm,
	// }
	// sendInfoMessage(data);

	// 채팅 메시지를 통한 사용자 Mute 기능 
	var json = { type : 'LeaderRequest',  agent : getUserID, target : ParticipantName[index], action : 'UserMute', targetsipid : ParticipantUri[index] };
	Log('info', 'Send Request Message : ' + json.type);
	try {
		ChatConnection.send(JSON.stringify(json));
	} catch(e) {
		Log('error', ' + ChatServer Communication Error : ' + e);
		RetryConnectChat();	
	}
}

function funcCMSetLayout(mode) {
	var data = {
		"owner" : "m2soft",
		"command" : "change_layout",
		"layout" : mode,
	}
	sendInfoMessage(data);
}

function funcCMPositionChange(original, change) {
	var data = {
		"owner" : "m2soft",
		"command" : "change_position",
		"original" : original,
		"change" : change
	}
	sendInfoMessage(data);
}

var keepAlive_timer = null;
function funcKeepAliveMesgStart() {
	console.info("%c [Function Call] : funcKeepAliveMesgStart", "color:blue; font-weight:bold;");
	var time = 60; // 60 sec
	var data = {
		"owner" : "m2soft",
		"command" : "keep_alive",
		"sip_uri" : getUserSIPID + "@m2soft.co.kr" 
	}
	keepAlive_timer = setInterval(function() {
		sendInfoMessage(data);
	}, 1000 * time);
}

function funcKeepAliveMesgStop() {
	try {
		clearInterval(keepAlive_timer);
	} catch(e) {
		Log('error', 'funcKeepAliveMesgStop Error : ' + e);
	}
}

var flag_mic = false;
var flag_video = false;
function funcMuteMicophone() {
	data = {
		"owner" : "m2soft",
		"command" : "mute_toggle"
	}
	sendInfoMessage(data);
}

// Callback Methods
function onInitialized() {
	Log('flow', '[ onInitialized ]');
	makeRegister({
		server: getUserRealm,
		id: getUserSIPID,
		password: getUserSIPPW});
}

function onReceivingCall(e) {
	Log('flow', '[ onReceivingCall ]' + e);
	console.log(e);

	console.log(e.peernumber);

	acceptCall();
}

var Registered = false;
function onRegistered(e) {
	Log('flow', '[ onRegistered : ' + e.description + ']');

	if(e.description == 'Connected') {
		Registered = true;
	}

	RegConnectTimeout(false);
	
    var call_type;	
	if( getConfType != TYPE_PEER_TO_PEER_CONFERENCE ) { // N:N or Audio Conference, Webinar

		if( getConfType == TYPE_AUDIO_CONFERENCE ) { // Audio Conference
			call_type = 'audio';
			mediaConstraints.video = false; // Media Constraints Video Not Use
		} else { // N:N Conference, Webinar
			call_type = 'audiovideo';
		}
		
	    makeCall({
            peerid: getPeerSIPID,
			media_type: call_type
		});
	} else { // 1:1 Conference
		//ConnectChat(); -- 20160812
		//ConnectFile(); -- 20160812
		funcSettingDisplayState('1:1wait');
	}
}

function onConnectedCall(e) {
	Log('flow', '[ onConnectedCall ]');
	switch ( getConfType * 1 ) {
		case TYPE_AUDIO_CONFERENCE :
		case TYPE_CONFERENCE:
		{
			ConnectChat();
			ConnectFile();
	        funcKeepAliveMesgStart();
		    break;
		}
		case TYPE_WEB_SEMINAR:
		{
			if(isConferenceManager == false) { // 웹세미나이고 일반참여자이면 오디오 마이크 mute
				muteMicrophone(true);
				muteWebCam(true);
			}
			ConnectFile();
	        funcKeepAliveMesgStart();
		    break;
		}
		case TYPE_PEER_TO_PEER_CONFERENCE:
		{
			if(dt == DEVICE_TABLET_IOS) {
				xfinger.resizeCameraView(true);
				xfinger.showCameraView();
			} else {
			funcSettingDisplayState( '1:1start' );
			}
			isDisconnected = false; // 종료되었다가 다시 시작되는 사용자를 위한 세팅 
		    break;
		}
	}
	funcCloseDialog();
	funcUseRecording(IsRec); // 레코딩 여부 
	updateJoinUserDeviceInfo(); //회의 접속 기기 정보 업데이트 
}

function onMediaTransferStarted() {
	Log('flow', '[ onMediaTransferStarted ]');
	funcCloseDialog();
	ICEConnectTimeout(false); // Ice Gathering State Completed 된 후 타임아웃 종료 처리 
}

function onDisconnectedCall(e, reason) {
	/** 이미 진행 되었다면 return **/
	if(isDisconnected) {
		return;
	}
	
	/** onDisconnectedCall Flow 더이상 발생 하지 않도록 세팅 **/		
	isDisconnected = true;

	if(e == null) {
		Log('flow', '[ onDisconnectedCall ] : ' + reason);
	} else {
		Log('flow', '[ onDisconnectedCall : ' + e.description + '] : ' + reason);
	}

	// 1:1 에서 타 사용자가 나갔을때, 남은 사용자는 종료처리하지않고 대기상태로 전환 
	if( (getConfType == TYPE_PEER_TO_PEER_CONFERENCE) && (reason === 'BYE') ) {
		if(dt == DEVICE_TABLET_IOS && isDisconnectedByMyself == false) {
		    console.log("Peer Disconnected!!");
			xfinger.removeVideoView();
			initialize();
			addVideoView('video_remote');
			xfinger.startPreview();
			xfinger.showCameraView();
			return;
		} else {
		console.log("Peer Disconnected!!");
        funcSettingDisplayState('1:1wait');
		if(WebBreakerMode) {
			WebBreakerMode = false;
			oSipSessionRegister.register();
		}
		return;
	}
	}

	releaseRegister();
	if(ChatConnection != null) {
		ChatConnection.close();
	}
	if(FileConnection != null) {
		FileConnection.close();
	}
	funcKeepAliveMesgStop();
	
	var result, data;
	var title = i18n('msg.text225');
	switch(reason) {
		case 'NoMedia' : 
		{
			//result = "미디어 디바이스를 찾을 수 없습니다. 확인 후 다시 연결해주세요.";
			data = { title : title, mesg : i18n('msg.text289') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'MediaReject' :
		{
			//result = "미디어 디바이스 사용을 거절하셨습니다. 허용 후 사용해 주세요.";
			data = { title : title, mesg : i18n('msg.text290') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'ConnectError' :
		{
			//result = "서버와의 연결이 원활하지 않습니다.확인 후 다시연결해주세요.";
			data = { title : title, mesg : i18n('msg.text291') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'Unauthorized' :
		{
			//result = "사용자 등록이 되어있지않습니다. 관리자에게 문의해주세요.";
			data = { title : title, mesg : i18n('msg.text292') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'ServerError' :
		{
			//result = "서버상태를 점검해주세요.";
			data = { title : title, mesg : i18n('msg.text293') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'ICEError' :
		{
			//result = "연결이 지연되어 종료됩니다. 브라우저를 다시 실행하여 접속해주세요.";
			data = { title : title, mesg : i18n('msg.text189') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'EndSession' :
		{
			//result = '세션이 종료되어 연결이 종료됩니다.';
			data = { title : title, mesg : i18n('msg.text294') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
			break;
		}
		case 'BYE' :
		{
			//result = '회의가 종료됩니다.';
			if(recvState == INFO_MESG_NORMAL_STATE) {
				if(getConfType == 1) { // 웨비나 종료 시
					data = { title : title, mesg : i18n('msg.text413') };
				} else {
					data = { title : title, mesg : i18n('msg.text404') };
				}
            	funcLoadDialog('closeDialog', data);
				funcSetDialogCloseTimer('closeDialog');
			} else {
				var check = setInterval(function() {
					Log('info', '[Wait for Info Message finished] : ' + recvState);
					if(recvState = INFO_MESG_FINISH_STATE) {
						if(ByeReason == 'KickOut'){ // 강제퇴장
							data = { title : title, mesg : i18n('msg.text288') };
						} else { // 종료
							if(getConfType == 1) { // 웨비나 종료 시
								data = { title : title, mesg : i18n('msg.text413') };
							} else {
								data = { title : title, mesg : i18n('msg.text404') };
							}
						}
						funcLoadDialog('closeDialog', data);
						funcSetDialogCloseTimer('closeDialog');
						recvState = INFO_MESG_NORMAL_STATE;
						clearInterval(check);
					}
				}, 1000 * 1);
            }
				
			break;
		}
		case 'UserClose' :
		{
			break;
		}
		default :
	  	{
			//result = '알수없는 문제로 종료됩니다. 관리자에게 문의해주세요.';	
			data = { title : title, mesg : i18n('msg.text295') };
			funcLoadDialog('closeDialog', data);
			funcSetDialogCloseTimer('closeDialog');
		}
	}

}

function onStopped() {
	Log('flow', '[ onStopped ]');
	if(Registered == false) {
		setTimeout(postInit(), 1000);
	}
}

/*참여자의 정보를 넣는 배열을 모두 초기화하는 함수*/
function initailzingJoinUserInfoArr(){
	onUserIndexArray = new Array(); // 로그인 사용자 
	offUserIndexArray = new Array(); // 전체 사용자 
	ParticipantName = new Array(); // 유저 이름
	ParticipantUri = new Array(); // 유저 Sip ID
	ParticipantId = new Array();
	ParticipantType = new Array(); // 유저 Type
	ParticipantImgPath = new Array(); // 유저 이미지 경로
	ParticipantInfo = new Array();
}

/**yujin*/
function funcToggleVideoDialog( video_type ){
	var header = '';
	var isInvisible;
	var dialog;
	console.log('funcToggleVideoDialog( '+video_type+' ) 실행..');
	switch ( video_type ) {
		case 'miniVideo':
		{
	$("#miniConfBtn").toggleClass("invisible");
	isInvisible = $("#miniConfBtn").hasClass("invisible");
	
	if(isInvisible){
				console.log('miniVideo dialog 생성');
		//video dialog html 
		header += '<img src="/newOnechance_pc/assets/img/video_icon.png" style="height: 16px; margin-left:10px;">';
		header += '<div class="table-cell" style="text-align: right; padding-right: 40px; min-height: 14px;">';
		header += '		<div id="opacitySlider" title="' + i18n('msg.text447') + '" class="tooltip_element"></div>';
		header += '<div>';
		//dialog 생성 
		dialog = $.videoDialog(header,'<video id="miniVideo">');
	}
}
		break;
		case 'previewVideo':
		{
			$("#previewBtn").toggleClass("invisible");
			isInvisible = $("#previewBtn").hasClass("invisible");

			if(isInvisible){
				console.log('funcToggleVideoDialog() - previewVideo dialog 생성');
				dialog = funcCreatePreviewDialog();
			}
		}
		break;
	}
}
//영상회의 버튼 설정
function funcSettingVideoButton( type ){
	var confDialogButton = $("#miniConfBtn");
	var previewButton = $("#previewBtn");
	var miniVideo = $("#miniVideo")
	   ,previewVideo = $("#previewVideo");
	
	switch ( type ) {
	case '1':
		//영상회의 dialog
		confDialogButton.css("display","none");
		miniVideo.parent().parent().dialog("close");
		//preview dialog
		previewVideo.parent().parent().removeClass('invisible');
		break;
	case '2':
		confDialogButton.css("display","table");
		previewButton.css("display","none");
		previewVideo.parent().parent().addClass('invisible');
		break;
	case '3':
		confDialogButton.css("display","table");
		previewButton.css("display","none");
		previewVideo.parent().parent().addClass('invisible');
		break;
	}
	
	switch ( getConfType*1 ) {
		case TYPE_CONFERENCE:
			funcSettingDisplayState('not1:1');
		break;
		case TYPE_WEB_SEMINAR:
			funcSettingDisplayState('not1:1');
		break;
		case TYPE_PEER_TO_PEER_CONFERENCE:
		break;
}
}
//분리창 여는 함수 
function funcOpenWindow( type ){
	var popup_name = "popup_"+type;
	switch ( type ) {
	case "DOC":
		console.log(type+"창열기");
		documentWin = window.open('/conferenceView/document',popup_name,'width=960,height=800,  resizable=yes, scrollbars=no, status=no');
		if( documentWin != null ){
			onClickCallback('btn_media');
			documentWin.onbeforeunload = function(){
				//창종료
				funcSettingTabsInMainWindow( type );
			};
		}
		break;
	case "WB":
		console.log(type+"창열기");
		whiteboardWin = window.open('/conferenceView/whiteboard',popup_name,'width=960,height=800,  resizable=yes, scrollbars=no, status=no');
		if(whiteboardWin != null){
			onClickCallback('btn_media');
			whiteboardWin.onbeforeunload = function(){
				//창종료
				funcSettingTabsInMainWindow( type );
			};
		}
		break;
	}
}
//분리된 창에서 다시 메인창으로 돌아올 때, 초기화해주는 함수
function funcSettingTabsInMainWindow( type ){
	switch (type) {
		case 'DOC':
		{
				console.log("[Info] close doc window");
				// 공유 문서 번호
				doc_cur_page = documentWin.doc_cur_page;
				doc_end_page = documentWin.doc_end_page;
				
				PT_page = documentWin.PT_page;
				PT_totalpage = documentWin.PT_totalpage;
				PT_name = documentWin.PT_name;
				PT_type = documentWin.PT_type;
				PT_host = documentWin.PT_host;

				tempEl = document.getElementById("doc_temp_canvas");
				viewEl = document.getElementById("doc_view_canvas");
				pointerEl = document.getElementById("doc_pointer_canvas");
				DOCpaintManager.Init(tempEl, viewEl, pointerEl, $("#doc_undo"), $("#doc_redo"), 'doc');
				DOCpaintManager.UseTools();

				funcDocumentMove('goto',doc_cur_page);
				// 선택되어져 있는 툴 활성화
				var move_tool_class = documentWin.document.getElementsByClassName('conf_doc_tool').item(0).className;
				if( move_tool_class.indexOf('active') != -1 ){
					funcDocToolChange( 'move' );
				}else{
					funcDocToolChange( DOCpaintManager.getTool().name );
				}
				documentWin=null;
		}
		break;
		case 'WB':
		{

				console.log("[Info] close whiteboard window");
				console.log("[Info] init whiteboard history!!");
				console.log( whiteboardWin.wbHistory);
				console.log( wbHistory);

				// 화이트보드 
				tempEl = document.getElementById("wb_temp_canvas");
				viewEl = document.getElementById("wb_view_canvas");
				pointerEl = document.getElementById("wb_pointer_canvas");
				WBpaintManager.Init(tempEl, viewEl, pointerEl, $("#wb_undo"), $("#wb_redo"), 'wb');
				WBpaintManager.UseTools();
				
				WBpaintManager.Refresh();
				wbHistory = whiteboardWin.wbHistory;
				WhiteboardHistoryLoad(false);
				// 선택되어져 있는 툴 활성화
				funcWBToolChange( WBpaintManager.getTool().name );
				whiteboardWin = null;
				console.log("[Info] setting whiteboardWin = null");
		}
		break;
	}
}
//화이트보드 또는 문서공유 분리창이 존재하는지 확인하는 함수
function isOpenWindow( type ){
	// type('wb' or 'doc')
	var result = true;
	switch ( type.toLowerCase() ) {
	case 'doc':
		if(documentWin == null){
			result = false;
		}
		break;
	case 'wb':
		if(whiteboardWin == null){
			result = false;
		}
		break;
	}
	return result;
}

//프리뷰 다이얼로그 만드는 함수
function funcCreatePreviewDialog(){
	var html = '';
	var header = '';
	var dialog;

	//video dialog html 
	header += '<div class="table-cell" style="text-align: right; padding-right: 40px; min-height: 14px;">';
	header += '		<div id="opacitySlider" title="'+i18n('msg.text447')+'" class="tooltip_element"></div>';
	header += '<div>';
	//dialog 생성 
	dialog = $.videoDialog(header,'<video id="previewVideo" autoplay muted/>');
	
	return dialog;
}

function funcSettingDisplayState( state_type ){
	var active_tab_num, video_tag_id;
	active_tab_num = funcGetActiveTabNum();
	switch ( state_type ) {
		case '1:1wait':
		{
			//영상회의탭 preview영상
			$("#previewVideo").parent().parent().dialog("close");
			$("#previewBtn").addClass("invisible");
			//문서공유&화이트보드의 영상dialog
			$("#miniVideo").parent().parent().dialog("close");
			$("#miniConfBtn").addClass("invisible");
			//화면공유 아이콘
			$("#screenshare_toggle_img").addClass("invisible");
			// 메인 Video 에 내 영상 띄움
			funcPreviewStreamLoad(videoRemote); 
			// 프리뷰로드 시 좌우반전 CSS 메인 Video 적용
			$("#video_remote").css({"transform":"scaleX(-1)", "object-fit":"cover"});
			//레이아웃 변경 버튼
			$("#layout_change_btn").remove();
			if( $("#video_mute_btn").attr("src").indexOf("off")> -1 ){
				$(".mute").toggleClass("invisible");
			}
			funcGetActiveTabNum();

			if(document.querySelector("#video_screen") != null) { // 상대방이 화면공유중에 종료했을때 
				removeScreenShareVideo();
			}
			toogleScreenShareBtn('media'); // 화면 공유 UI 제거 
		    break;
		}
		case '1:1start':
		{
			active_tab_num = funcGetActiveTabNum();
			//영상회의탭 preview영상
			$("#previewBtn").removeClass("invisible");
			$("#previewBtn").click();
			if(active_tab_num != 1){
				$("#previewVideo").parent().parent().addClass("invisible");
			}
			//화면공유 아이콘
			$("#screenshare_toggle_img").removeClass("invisible");
			//문서공유&화이트보드의 영상dialog
			$("#miniConfBtn").removeClass("invisible");
			// 메인 Video 에 상대 영상 띄워지고, 프리뷰 Video 에 내 영상 띄움
			funcPreviewStreamLoad(document.getElementById('previewVideo')); 
			// 프리뷰로드 시 좌우반전 CSS 메인 Video 해제
			$("#video_remote").css({"transform":"", "object-fit":"cover"}); 
			// 프리뷰 video 좌우 반전 CSS 적용 
			$("#previewVideo").css("transform", "scaleX(-1)"); 
			if( $("#video_mute_btn").attr("src").indexOf("off") > -1 ){
				muteWebCam(true); // 시작전에 mute 중이였는지 확인후 mute 
			}
		    break;
		}
		case 'not1:1':
		{
			$("#previewBtn").remove();
		    break;
		}
	}
}

function funcChangeMediaSource( selector, source_id ){
	$(selector).attr( "src" , source_id );
}

function funcGetActiveTabNum(){
	console.log( $("img[id^='menu_img']") );
	var active_tab_object = $.grep($(".pc_conf_main_menu img[id^='menu_img']"),function(e){
		console.log(e);
		return e.src.indexOf("on");
	});
	var tab_num;
	if( active_tab_object != null && active_tab_object.length >=1 ){
		tab_num = active_tab_object[0].src.split('top_btn_')[1].split('_on.png')[0];
	}
	console.log('[Info] Active Tab!!! = '+tab_num);
	return tab_num;
}
