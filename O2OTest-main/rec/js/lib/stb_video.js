/*********************************************************************
 * @제목 : WebRTC 샘플 화면 컨트롤
 * @설명 : 샘플 페이지 
 * @변경이력
 * 1. 2020.02.27 : 최초작성
 * 
 * @author kswksk
 * @date 2020.02.27
 ********************************************************************/
		
/**
 * 웹페이지 초기 로딩 처리
 * @returns
 */
var imgPenIndex = 0;
var muted = false;
var isPlay = true;
//모바일 컨텐츠 제어
var controlM = false;
var sframeChat = null;
var spass = null;
var drawpenList = [];
var imgList = new Array();
var userList = new Array();
var roomUserId = "";
var roomId = "";
var shareFileId = "";
var firstUser = null; 
var startTime = 0;
var fullUserId = "";
var fullUserChk = false;
var fullUserData = null;
var isBangJang = false;
var bangjangId = "";
var penDataList = {};
var movieRoomCnt = 5;

/**
 * 이미지 리사이징
 * @param img
 */
function imageOnload(img) {
	var image = $(img);
	var parent = $(img).parents("li");
	var canvas = $(parent).find("canvas");
	
	// 크기조절하기
	var imgWidth = image.width();
	var imgHeight = image.height();
	
	var rootWidth = parent.width();
	var rootHeight = parent.height();
	
	// 높이를 맞춤
	var calcHeight = rootHeight;
	var rate = calcHeight / imgHeight;
	imgWidth = imgWidth * rate;
	imgHeight = calcHeight;
	
	// 넓이를 마춤
	if (imgWidth > rootWidth) {
		// 넓이가 맞아짐
		var calcWidth = rootWidth;
		var rate = calcWidth / imgWidth;
		imgHeight = imgHeight * rate;
		imgWidth = calcWidth;
		$(img).parent().removeClass("center");
	} else {
		$(img).parent().addClass("center");
	}
	image.width(imgWidth).height(imgHeight);
	
	//canvas 리사이징
	canvas.width(imgWidth).height(imgHeight);
	canvas[0].width = imgWidth;
	canvas[0].height = imgHeight;
	
	setTimeout(function(){
		// 판서이미지 다시 그리기
		for (var i = 0; i < imgList.length; i++) {
			var penData = penDataList["key_"+i];
			if (penData) {
				for (var j = 0; j < penData.length; j++) {
					drawpenList[i].drawNetworkPen(penData[j]);
				}
			}
		}
	}, 500);
	
	$(".view ul").width(parent.width() *(imgList.length+1));
	$(".viewNum .total").text(imgList.length);
	// 이전다음
	var totalNum = $(".view ul li").length-1;
	$(".btnNext").off("click").on("click",function(){
		if(imgPenIndex < totalNum){
			imgPenIndex++;
			imgMove(imgPenIndex);
			var msg = {
					"type" : "movePage",
					"data" : imgPenIndex,
			};
			sframeChat.sendMsg(msg);
		}
	});
	
	$(".btnPrev").off("click").on("click",function(){
		if(imgPenIndex > 0){
			imgPenIndex--;
			imgMove(imgPenIndex);
			var msg = {
					"type" : "movePage",
					"data" : imgPenIndex,
			};
			sframeChat.sendMsg(msg);
		}
	});
}

// 판서를 유지하기위한 콜백
function savePen(data) {
	var penList = penDataList["key_"+imgPenIndex];
	if (!penList) {
		penList = [];
		penDataList["key_"+imgPenIndex] = penList;
	}
	penList[penList.length] = data;
}

//판서 다시 그리기
function reDrawPen() {
	// 기존 판서 초기화
	for (var i = 0; i < imgList.length; i++) {
		drawpenList[i].clearCanvas();
	}
	
	// 다시 그리기
	for (var i = 0; i < imgList.length; i++) {
		var penData = penDataList["key_"+i];
		if (penData) {
			for (var j = 0; j < penData.length; j++) {
				drawpenList[i].drawNetworkPen(penData[j]);
			}
		}
	}
}

$(document).ready(function() {
	$(".btnDraw").on("click",function(){
		if($(".drawBox").hasClass("on")){
			$(".drawBox").removeClass("on");
			$(".subMemu").slideUp(300);
		}else{
			$(".drawBox").addClass("on");
		}
	});
	//그리기툴 1차메뉴
	$(".drawTool>li>a").click(function(){
		if(!$(this).hasClass("on")){
			$(".subMemu").slideUp(300);
		}
		$(this).next().slideDown(300);
		$(this).parent().siblings().find(">a").removeClass("on");
		$(this).addClass("on");
	});
	//그리기툴 선굵기 선택
	$(".subMemu.line li a").on("click",function(){
		drawpenList[imgPenIndex].setWidth($(this));
	});
	//그리기툴 색상 선택
	$(".subMemu.palette li a").on("click",function(){
		drawpenList[imgPenIndex].setColor($(this));
	});
	
	$('.removeAll').click(function() {
		drawpenList[imgPenIndex].remove($(this),'All');
		// 저장했던 정보 삭제
		delete penDataList["key_"+imgPenIndex];
	});
	$('.remove').click(function() {
		drawpenList[imgPenIndex].remove($(this),'N');
	});
	// SFrameChat, SPass 초기화
	initSframeChat();
	$(".menuOpen").on("click",function(){
		if($("#header").hasClass("on")){
			$("#header").removeClass("on");
		}else{
			$("#header").addClass("on");
		}
		//canvas 리사이징
		$(".center").hide();
		$(".center").show(1);
		
		setTimeout(function() {
			var imageList = $(".view li img");
			for (var i = 0; i < imageList.length; i++) {
				imageOnload(imageList[i]);
			}
		}, 500);
		
		setVideoSize();
	});
	$(".btnOn").on("click",function(){
		if($(this).hasClass("on")){
			$(this).removeClass("on")
		}else{
			$(this).addClass("on")
		}
	});
	//상단메뉴 on/off
	$(".topMenu li a").on("click",function(){
		//off 클래스를 가진 메뉴만 on켜짐
		if($(this).parent().hasClass("on")){
			if($(this).parent().hasClass("on")){
				$(this).parent().removeClass("on");
				$(this).parent().addClass("off");
			}else{
				$(this).parent().addClass("on");
			}
		}else{
			$(this).parent().removeClass("off");
			$(this).parent().addClass("on");
		}
	});
});

/**
 * 채팅 서버를 초기화 한다.
 * @returns
 */

function initSframeChat() {
	startTime = new Date().getTime();
	var wssUrl = 'wss://'+window.location.hostname+':9200';
	var turnUrl = 'turn:'+window.location.hostname+':9100';
	if (config.isDev()) {
		wssUrl = 'wss://'+window.location.hostname+':19200';
		turnUrl = 'turn:'+window.location.hostname+':19100';
	}
	
	//room, user 정보 서버 호출
	getRoomInfo(function(response) {
		$(".txt").text(response.movieRoomInfo.movieRoomNm);
		roomId = response.movieRoomInfo.movieRoomId;
		bangjangId = response.movieRoomInfo.regHwnno;
		userList = response.userList;
		movieRoomCnt = new Number(response.movieRoomInfo.movieRoomCnt);
		
		for(var j=0;j<userList.length;j++){
			if(userList[j].selfG == 'Y'){
				if(userList[j].userG == 'C'){
					roomUserId = userList[j].cusNo;					
				}else{
					roomUserId = userList[j].hwnno;
				}
			}
		}
		shareFileId = response.movieRoomInfo.fileId;
		isBangJang = response.movieRoomInfo.regHwnno == roomUserId;
		
		var uhtml = "";
		for(var k=0;k<userList.length;k++){
			uhtml += "<tr>";
			if(userList[k].cusNo == ""){
				uhtml += "	<th scope\"row\">직원(성명/부서/직급)</ht>";
				uhtml += "	<td>"+userList[k].hwnnm+"/  "+userList[k].brnm+"/  "+userList[k].jkgpnm+"</td>";			
			}else{
				uhtml += "	<th scope\"row\">고객(성명)</ht>";
				uhtml += "	<td>"+userList[k].cusNn+"</td>";			
			}
			uhtml += "</tr>";
		}
		$("#tableUserList").append(uhtml);
		sframeChat = new SFrameChat(roomId, roomUserId, onChatMessage);
		sframeChat.connect(wssUrl);
		
		// 로그 적재
		/*
		var spassLog = new SPassLog();
		spassLog.start(function(result) {
			console.log(result);
			console.log(">>>> result : " + JSON.stringify(result));
		}, 3000);
		*/

		/*
		var constraints = null;
		if (isIphone()  || isAndroid()) {
			// 모바일 이면 전면 카메라
			constraints = {
				audio: true,
				video: {
					facingMode : "user",
					frameRate : {min:10, ideal:30, max:60},
					width: {min:240, ideal:320, max:320},
					height: {min:240, ideal:240, max:320}
				}
			};
		} else {
			// PC면 그냥 틀어
			constraints = {
					audio: true,
					video: {
						frameRate : {min:10, ideal:30, max:60},
						width: {min:240, ideal:320, max:320},
						height: {min:240, ideal:240, max:320}
					}
				};
		}
		spass = new SPass(sframeChat, turnUrl, onSpassCallBack, constraints);
		*/
		spass = new SPass(sframeChat, turnUrl, onSpassCallBack, null);
		setInterval(startTimeInterval, 1000);
		//서버에서 이미지 다운로드 
		getImageList();
		
	});
}

// 비디오 사이지 조정
function setVideoSize() {
	setTimeout(function() {
		var videoList = $("video");
		for (var i = 0; i < videoList.length; i++) {
			var parent = $(videoList[i]).parents("li");
			$(videoList[i]).width(parent.width()).height(parent.height());
			videoList[i].width = parent.width();
			videoList[i].height = parent.height();
		}
	}, 500);
}

//타이머 구동
function startTimeInterval() {
	currTime = new Date().getTime();
	var calcTime = currTime - startTime;
	var min = Math.floor((calcTime / 1000) / 60);
	var sec = Math.floor((calcTime / 1000) % 60);
	
	$(".time").html(getTwoNumber(min)+":"+getTwoNumber(sec));
}

function onSpassCallBack(type, data, userId) {
	
	if (type == spass.TYPE_LOCAL_STREAM || type == spass.TYPE_LOCAL_SCREEN_STREAM) {
		// 로컬 미디어 
		var video = $("#video_local")[0];

		try {
			video.src = window.URL.createObjectURL(data);
		} catch (e) {
			video.srcObject = data;
		}
		
		if (type == spass.TYPE_LOCAL_SCREEN_STREAM) {
			video.style.transform = "scaleX(1)";
		} else {
			video.style.transform = "scaleX(-1)";
		}
		
//		$("#dfVideo").text(userName); //이름 제거
	} else if (type == spass.TYPE_REMOTE_STREAM || type == spass.TYPE_REMOTE_SCREEN_STREAM) {
		var currUser = null;
		var userName = "";
		for (var i = 0; i < userList.length; i++) {
			var row = userList[i];
			if (row.cusNo == userId) {
				currUser = row;
				userName = row.cusNn;
			}else if(row.hwnno == userId){
				userName = row.hwnnm;				
			}
		}
		
		// 원격 미디어
		var videoId = "VD_"+userId;
		var imgId = "IMG_"+userId;
		var video = document.getElementById(videoId);
		var videoArea = $("#movList");
		if (video == null || video == undefined) {
			if(!isFile() && currUser != null && firstUser == null){
				firstUser = currUser;
				fullUserId = userId;
				fullUserData = data;
				fullUserChk = true;
				videoArea = $("#customVideo");
				videoArea.empty();
//				videoArea.append("<li id='DV_"+userId+"'><video id='"+videoId+"' class='tempVideo' autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"님<div></li>")
				videoArea.append("<li id='DV_"+userId+"'><img id=\""+imgId+"\" class=\"tempVideo\" src=\"/mov/stb/images/img_temp_video1.png\" style=\"display:none; height:100%; width:100%;\"><video id='"+videoId+"' class='tempVideo' autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"</div></li>")
			}else{
//				var strStyle = "";
//				if (videoArea.children().length > 0) {
//					strStyle = "margin-top:10px";
//				}
//				videoArea.append("<li id='DV_"+userId+"'><div  style='"+strStyle+"'><video id='"+videoId+"' class='tempVideo' autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"님</div><div></li>");
				videoArea.append("<li id='DV_"+userId+"'><img id=\""+imgId+"\" class=\"tempVideo\" src=\"/mov/stb/images/img_temp_video1.png\" style=\"display:none; height:100%; width:100%;\"><video id='"+videoId+"' class='tempVideo'  autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"</div></li>");
			}
			video = document.getElementById(videoId);
			
			setVideoSize();
		}
		
		try {
			video.src = window.URL.createObjectURL(data);
		} catch (e) {
			video.srcObject = data;  
		}
		
		if (type == spass.TYPE_REMOTE_SCREEN_STREAM) {
			video.style.transform = "scaleX(1)";
		} else {
			video.style.transform = "scaleX(-1)";
		}
		
		var msg = {
				"type" : "chckHideVideo"
			}
		sframeChat.sendMsg(msg, userId);
	} else if (type == spass.TYPE_PEER_EVENT) {
		// 영상 상태에 대한 이벤트 발생
		if (data == "checking") {
			// 접속 체크
		} else if (data == "connected") {
			// 연결됨
			if (window.cordova) {
				$("body")[0].style.display = 'none';
			}
		} else if (data == "disconnected") {
			// 끈김
			removeVideo(userId);
		} else if (data == "failed" || data == "close") {
			// 접속 실패
		}
	} else if (type == spass.TYPE_ERR) {
		// 캠 또는 마이크 없음
		alert(data.errMsg);
	}
	//영상 배치
	setVideoPlace();
}

function removeVideo(userId) {
	var videoId = "#DV_"+userId;
	var videoObj = $(videoId);
	if (videoObj) {
		videoObj.remove();
		firstUser = null;
		setVideoPlace();
		setVideoSize();
	}
}

/**
 * 채팅 메시지 수신 메소드
 * @param message
 * @returns
 */
function onChatMessage(result) {
	if (result.type == sframeChat.TYPE_ERR) {
		// 에러 발생 
		alert(result.errMsg+"["+result.errCode+"]");
		if(result.errCode == "9001"){
			closeOK();
		}
	} else if (result.type == sframeChat.TYPE_CLOSE) {
		// 사용자 접속 해제 (영상쪽에서는 완전끊어지지고 않고 지속적으로 재연결됨, 채팅서버와 끊어졌을 경우 완전 뺌)
		/* 실제 영상이 끊겼을 때 이벤트로 변경
		var videoId = "#DV_"+result.userId;
		$(videoId).remove();
		firstUser = null;
		setVideoPlace();
		setVideoSize();
		*/
	} else if (result.type == sframeChat.TYPE_CONN) {
		// 사용자 접속
		var isInitData = false;
		if (isBangJang) {
			// 내가 방장이면 초기 정보 보내줌
			isInitData = true;
		} else {
			// 방장이 있는지 확인
			var isConnectBangjang = false;
			for (var i = 0; i < sframeChat.roomUserList.length; i++) {
				if (sframeChat.roomUserList[i] == bangjangId) {
					isConnectBangjang = true;
					break;
				}
			}
			if (isConnectBangjang == false) {
				// 방장이 없으니 접속자 중 첫번째 있는 사람이 보냄
				isInitData = true;
			}
		}
		
		if (isInitData) {
			var initMessage = {
					"type" : "initData",
					"imgPenIndex" : imgPenIndex,
					"penData" : penDataList,
					"imgList" : imgList
			};
			sframeChat.sendMsg(initMessage, result.userId);
		}
	} else if (result.type == sframeChat.TYPE_MSG) {
		if(result.data.type == "drawImg") {
			drawpenList[imgPenIndex].drawNetworkPen(result.data.data);
		}else if (result.data.type == "clearImg") {
			for (var i = 0; i < drawpenList.length; i++) {
				if (drawpenList[i].canvas.id == result.data.elementId) {
					drawpenList[i].clearCanvas();
					
					// 저장했던 정보 삭제
					delete penDataList["key_"+i];
					
					return;
				}
			}
		}else if (result.data.type == "movePage") {
			imgPenIndex = result.data.data;
			imgMove(imgPenIndex);
		}else if(result.data.type == "closeChat"){
			alert("화상상담이 종료되었습니다.");
			window.mbs.close();
		}else if(result.data.type == "micMute"){
			var userName = "";
			for (var i = 0; i < userList.length; i++) {
				if (userList[i].custNo == result.userId || userList[i].hwnno == result.userId) {
					userName = userList[i].hwnnm != "" ? userList[i].hwnnm : userList[i].cusNn;
				}
			}
			mute('3');
			alert(userName+"님께서 마이크를 음소거 하였습니다.");
		}else if(result.data.type == "rNChange"){
			$(".txt").text(result.data.data.roomName);
			if(result.data.data.roomLock == "Y"){
				$("#chatBtn").addClass("on");
			}else{
				$("#chatBtn").removeClass("on");
			}
		}else if(result.data.type == "setNewImg"){
			getImageList();
		}else if(result.data.type == "setNewUser"){
			getResetUserList();
		}else if(result.data.type == "chckHideVideo"){
			var msg = {
					"type" : "hideVideo",
					"data" : isPlay
				}
				sframeChat.sendMsg(msg, result.userId);
		}else if(result.data.type == "hideVideo"){
			//영상 가리기
			if(!result.data.data){
				var imgId = "#IMG_"+result.userId;
				var videoId = "#VD_"+result.userId;
				$(imgId).show();
				$(videoId).hide();
			}else{
				var imgId = "#IMG_"+result.userId;
				var videoId = "#VD_"+result.userId;
				$(imgId).hide();
				$(videoId).show();
			}
		} else if (result.data.type == "initData") {
			// 초기 값 입력
			imgPenIndex = result.data.imgPenIndex;
			penDataList = result.data.penData;
			imgList = result.data.imgList;
			// imgMove(imgPenIndex);
			
			newLoadImage(imgList);
		}
	}
}
/**
 * 참가자정보 팝업
 */
function customerPop(){
	/*
	$(".m1").addClass("on");
	$("#invitePopup").attr("style","display:show;");
	*/
	spass.startSharedScreen();
}

function inviteCancel(){
	$(".m1").addClass("off");
	$("#invitePopup").attr("style","display:none;");
}
/**
 * 참가자 초대 완료 팝업
 */
function endAddUserPop(){
	getResetUserList();
	var msg = {
			"type" : "setNewUser"
	}
	sframeChat.sendMsg(msg);
}

function getResetUserList() {
	getRoomInfo(function(response) {
		for(var j=0;j<response.userList.length;j++){
			userList[j] = response.userList[j];
			if(response.userList[j].selfG == 'Y'){
				if(response.userList[j].userG == 'C'){
					roomUserId = response.userList[j].cusNo;					
				}else{
					roomUserId = response.userList[j].hwnno;
				}
			}
		}
		shareFileId = response.movieRoomInfo.fileId;
		movieRoomCnt = new Number(response.movieRoomInfo.movieRoomCnt);
		
		var uhtml = "";
		for(var k=0;k<userList.length;k++){
			uhtml += "<tr>";
			if(userList[k].cusNo == ""){
				uhtml += "	<th scope\"row\">직원(성명/부서/직급)</ht>";
				uhtml += "	<td>"+userList[k].hwnnm+"/  "+userList[k].brnm+"/  "+userList[k].jkgpnm+"</td>";			
			}else{
				uhtml += "	<th scope\"row\">고객(성명)</ht>";
				uhtml += "	<td>"+userList[k].cusNn+"</td>";			
			}
			uhtml += "</tr>";
		}
		$("#tableUserList").empty();
		$("#tableUserList").append(uhtml);
	});
}

/**
 * Contents 공유 팝업
 */
function contentPop(){
	/* 파일 업로드 기능을 대체하기 위해서 만듬
	getRoomInfo(function(response){
		var limitCnt = 10;
		var params = {"fileGroupId":response.movieRoomInfo.fileGroupId,"roomId":response.movieRoomInfo.movieRoomId,"fileId":shareFileId,"limitCnt": limitCnt};
		window.mbs.showSearchDocumentPopup(encodeURIComponent(JSON.stringify(params)));		
	});
	*/
	$("#doc_file").click();
}

/**
 * 파일 업로드
 */
function fileUPload() {
	var elFile = $("#doc_file");
	
	if (elFile.val() == null || elFile.val() == "") return;
	
	if (elFile.val().toLowerCase().endsWith("pdf") == false) {
		alert("pdf 파일만 선택 가능합니다.");
		elFile.val("");
		return;
	}
	
	$(".loading").show();
	$(".viewNum").hide();
	var formData = new FormData();
	formData.append("doc_file", elFile.prop("files")[0]);
	formData.append("roomId", sframeChat.roomId);
	
	$.ajax({
		type : "POST",
		url : "/noface/uploadDoc.do",
		data : formData,
		enctype: "multipart/form-data",
		processData: false,
		contentType: false,
		async: true,
		success : function(result) {
			$(".loading").hide();
			$(".viewNum").show();
			elFile.val("");
			console.log("----------- sucess -----------");
			console.log(result);
			// String -> JSON 변환
			var data = JSON.parse(result);
			
			imgPenIndex = 0;
			drawpenList = [];
			newLoadImage(data.list);
			
			var msg = {
					"type" : "reloadImage",
					"imgList" : imgList
			}
			sframeChat.sendMsg(msg);
		},
		error : function(err) {
			$(".loading").hide();
			$(".viewNum").show();
			shareFileId = null;
			
			elFile.val("");
			console.log("----------- error -----------");
			console.log(err);
			alert("문서변환에 실패하였습니다.");
		}
	});
}

function newLoadImage(imgs) {
	imgList = imgs;
	shareFileId = "암거나..";
	//공유 파일 없을경우
	$(".drawBox").attr("style","display:show;");
	$(".btnPrev").attr("style","display:show;");
	$(".btnNext").attr("style","display:show;");
	var html = "";
	for(var i=0;i<imgList.length;i++){
		html += "<li id=\"img"+i+"\">";
		html += "<span>"
		if (isMovTest()) {
			html += "	<canvas id=\"drawCanvas"+i+"\" name=\"drawCanvas\" width=\"716px\" height=\"603px\"></canvas>";
			html +=	"	<img id=\"docImg"+i+"\" src=\""+imgList[i]+"\" onload=\"imageOnload(this);\"/>";
		} else {
			html += "	<canvas id=\"drawCanvas"+i+"\" name=\"drawCanvas\" width=\"716px\" height=\"603px\"></canvas>";
			// html +=	"	<img id=\"docImg"+i+"\" src=\"/app/mov/movieFileDownLoad.do?fileName="+imgList[i]+"&movieRoomId="+sframeChat.roomId+"\" onload=\"imageOnload(this);\"/>";
			html +=	"	<img id=\"docImg"+i+"\" src=\""+imgList[i]+"\" onload=\"imageOnload(this);\"/>";
		}
		html += "</span>"
		html += "</li>";
	}
	$("#imgList").empty();
	$("#imgList").append(html);

	for(var i=0;i<imgList.length;i++){
		drawpenList[i] = new DrawPen($("#drawCanvas"+i+"")[0]);
		drawpenList[i].drawCallBack = savePen;
		$(".viewNum").show();
	}
	
	if (imgList != null && imgList.length > 0) {
		imgMove(imgPenIndex);
	}
	
	//이미지 없을때 full영상에 나오던 video 다시 그려주기
	if(fullUserChk){
		var userName = "";
		for (var i = 0; i < userList.length; i++) {
			var row = userList[i];
			if (row.cusNo == fullUserId) {
				userName = row.cusNn;
			}else if(row.hwnno == fullUserId){
				userName = row.hwnnm;				
			}
		}
		var vhtml = "";
		var videoArea = $("#movList");
		var videoId = "VD_"+fullUserId;
		var imgId = "IMG_"+fullUserId;
		fullUserChk = false;
		videoArea.append("<li id='DV_"+fullUserId+"'><img id=\""+imgId+"\" class=\"tempVideo\" src=\"/mov/stb/images/img_temp_video1.png\" style=\"display:none; height:100%; width:100%;\"><video id='"+videoId+"' class='tempVideo' autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"</div></li>");
		
		videoObj = $("#"+videoId);
		
		setVideoSize();
	
		var video = videoObj[0];
		try {
			video.srcObject = fullUserData;  
		} catch (e) {
			video.src = window.URL.createObjectURL(fullUserData);
		}
	}
	
	reDrawPen();
	imgMove(imgPenIndex);
}

/**
 * 네이티브에서 문서 서버에 저장 완료 콜백
 */
function endContentPop(){
	var msg = {
		"type" : "setNewImg"
	};
	sframeChat.sendMsg(msg);
	
	penDataList = {};
	reDrawPen();
	
	//페이지 자동 이동 초기화
	imgPenIndex = 0;
	getImageList();
}

/**
 * 고객용 화면 제어(콘텐츠 보기 닫기)
 */
function mobilePop(){
	var customUserId = "";
	var param = "";
	if($(".m21").hasClass("off")){
		$(".m21").removeClass("on");
		$("#controlTxt").text("고객문서 OFF");
		param = "on";
	}else{
		$(".m21").addClass("on");
		$("#controlTxt").text("고객문서 ON");
		param = "off"
	}
	var msg = {
			"type" : "controlMomile",
			"data" : param
	}
	sframeChat.sendMsg(msg);
}

/**
 * video, mic 음소거 설정
 *  * @param flag 1 : video, 2: mic(개인) 3: mic(방장 전체 끄기)
 */
function mute(flag){
	var videoObject = $(".userList ul li video");
	if (flag == '1') {
		if(isPlay == true){
			$("#img_local").attr("style","width:"+$("#video_local")[0].width+"px; height:"+$("#video_local")[0].height+"px;");
			isPlay = false;
			$("#img_local").show();
			$("#video_local").hide();
		} else {
			isPlay = true;
			$("#img_local").hide();
			$("#video_local").show();
		}
		var msg = {
			"type" : "hideVideo",
			"data" : isPlay
		}
		sframeChat.sendMsg(msg);
	} else if(flag == '2') {
		muted = !muted;
		spass.localStream.getAudioTracks()[0].enabled = !muted;
	} else {
		muted = true;
		$("#micBtn").removeClass("on");
		$(".m4").removeClass("on");
		$(".m4").addClass("off");
		spass.localStream.getAudioTracks()[0].enabled = !muted;
	}
}
/**
 * 사용자 초대
 * @param flag 1 : 직원 초대, 2 : 고객 초대
 */
function inviteChat(flag){
	var limitCnt = movieRoomCnt - userList.length; 
	var params = {'roomId':roomId,'limitCnt':limitCnt};
	$(".m1").removeClass("on");
	$("#invitePopup").attr("style","display:none;");
	if(limitCnt == 0){
		alert("방 인원수 초과로 더 이상 초대할 수 없습니다.");
		return;
	}
	if(flag == '1'){
		window.mbs.showSearchEmployeePopup(encodeURIComponent(JSON.stringify(params)));		
	}else{
		window.mbs.showSearchCustomerPopup(encodeURIComponent(JSON.stringify(params)));		
	}
}

/**
 * 환경설정 팝업
 */
function envSet(){
	$(".m5").addClass("on");
	$("#envPopup").attr("style","display:show;");
	$("#rnChange").val($(".txt").text());
}
/**
 * 환경설정 취소
 */
function envCancel(){
	$(".m5").removeClass("on");
	$("#envPopup").attr("style","display:none;");
}

/**
 * 환경설정 저장
 */
function envSave(){
	var roomLock = "Y";
	if($("#micBtn").hasClass("on")){
		var msg = {
				"type" : "micMute"
		}
		sframeChat.sendMsg(msg);
	}
	//상담실 방 잠금
	if($("#chatBtn").hasClass("on")){
		roomLock = "Y";
	}else{
		roomLock = "N";		
	}

	var params = {"movieRoomId":roomId,"movieRoomNm":$("#rnChange").val(),"openYn":roomLock}
	ajaxMbs('/app/mov/updateMovieRoomInfo.do', params, 
		function(response) {
			$(".txt").text($("#rnChange").val());
			var sendData = {
					roomName : $("#rnChange").val(),
					roomOpenYn : roomLock
			};
			var msg = {
					"type" : "rNChange",
					"data" : sendData
			}
			sframeChat.sendMsg(msg);
			$(".m5").removeClass("on");
			$("#envPopup").attr("style","display:none;");
		},
		function(e) {
			alert("네트워크 통신 중에 오류가 발생하였습니다. 다시 이용하시기 바랍니다.");
		}
	);
}
/**
 * 상담 종료 취소
 */
function closeChat(){
	$("#closePopup").attr("style","display:show;");
}
/**
 * 상담 종료 확인
 */
function closeCancel(){
	$("#closePopup").attr("style","display:none;");
}
/**
 * 상담종료 Function
 */
function closeOK(){
	/**
	 * 방장이 종료할 경우 다른사람들 전부 상담종료 기능
	 * 방폭은 보류 해달라는 요청으로 주석
	 
	if (isBangJang) {
		var params = {'movieRoomId':roomId,'movieState':"02"};
		$("#closePopup").attr("style","display:none;");
		
		ajaxMbs('/app/mov/movieEnd.do', params,
			function(response) {
				var msg = {
						"type" : "closeChat"
				};
				sframeChat.sendMsg(msg);
				window.mbs.close();
			}, function(e) {
				alert("네트워크 통신 중에 오류가 발생하였습니다. 다시 이용하시기 바랍니다.");
				window.mbs.close();
			}
		);
	} else {
		window.mbs.close();
	}
	*/
	var cntEmp = 0;
	//마지막 사용자 일경우 방폭
	for(var i=0;i<userList.length;i++){
		for(var j=0;j<sframeChat.roomUserList.length;j++){
			if(userList[i].userG == 'H' && userList[i].hwnno == sframeChat.roomUserList[j]){
				cntEmp++;
			}
		}
	}
	if(cntEmp == 1){
		var params = {'movieRoomId':roomId,'movieState':"02"};
		$("#closePopup").attr("style","display:none;");
		
		ajaxMbs('/app/mov/movieEnd.do', params,
			function(response) {
				var msg = {
						"type" : "closeChat"
				};
				sframeChat.sendMsg(msg);
				window.mbs.close();
			}, function(e) {
				alert("네트워크 통신 중에 오류가 발생하였습니다. 다시 이용하시기 바랍니다.");
				window.mbs.close();
			}
		);
	}else{
		window.mbs.close();
	}

}
/**
 * 파일 있는지 check function
 * @returns {Boolean}
 */
function isFile(){
	if(shareFileId == undefined || shareFileId == ""){
		return false;
	}else{
		return true;
	}
}
/**
 * room, user 정보
 */
function getRoomInfo(callback){
	var params = {'urlKey' : $("#k").val()};
	ajaxMbs('/app/mov/selectMovieRoomInfo.do', params,
		function(response) {
			if(response.movieRoomInfo == null || response.movieRoomInfo == undefined){
				alert("접속 정보가 유효하지 않습니다. 다음에 다시 이용하여 주십시오.");
				closeOK();
			}
			callback(response);
		}, function(e) {
			alert("네트워크 통신 중에 오류가 발생하였습니다. 다시 이용하시기 바랍니다.");
			closeOK();
		}
	);
}

/**
 * image 서버에서 다운
 */
function getImageList(){
	var params = {'urlKey':$("#k").val()};
	$(".viewNum").hide();
	$(".loading").show();
	
	ajaxMbs("/app/mov/selectMovieFileInfo.do", params,
		function(data) {
			if(data.movieRoomInfo == null || data.movieRoomInfo == undefined){
				alert("컨텐츠(파일) 정보 조회 중에 오류가 발생하였습니다. 관리자에게 문의 하시기 바랍니다.");
			}
			if(data.movieRoomInfo.fileChgYn == "N"){
				setTimeout(getImageList,2000);
				return;
			}else{
				imgList = data.fileNameList;
				shareFileId = data.movieRoomInfo.fileId;
				//공유 파일 없을경우
				if(!isFile()){
					$(".drawBox").attr("style","display:none;");
					$(".btnPrev").attr("style","display:none;");
					$(".btnNext").attr("style","display:none;");
					var html = "";
					html += "<li id=\"customVideo\">";
					html += "	<img id=\"defaultImg\" src=\"/mov/stb/images/img_temp_video1.png\" style=\"height:100%;\">";
					html += "</li>";
					$("#imgList").empty();
					$("#imgList").append(html);
				}else{
					$(".drawBox").attr("style","display:show;");
					$(".btnPrev").attr("style","display:show;");
					$(".btnNext").attr("style","display:show;");
					var html = "";
					for(var i=0;i<imgList.length;i++){
						html += "<li id=\"img"+i+"\">";
						html += "<span>"
						if (isMovTest()) {
							html += "	<canvas id=\"drawCanvas"+i+"\" name=\"drawCanvas\" width=\"716px\" height=\"603px\"></canvas>";
							html +=	"	<img id=\"docImg"+i+"\" src=\""+imgList[i]+"\" onload=\"imageOnload(this);\"/>";
						} else {
							html += "	<canvas id=\"drawCanvas"+i+"\" name=\"drawCanvas\" width=\"716px\" height=\"603px\"></canvas>";
							// html +=	"	<img id=\"docImg"+i+"\" src=\"/app/mov/movieFileDownLoad.do?fileName="+imgList[i]+"&movieRoomId="+sframeChat.roomId+"\" onload=\"imageOnload(this);\"/>";
							html +=	"	<img id=\"docImg"+i+"\" src=\""+imgList[i]+"\" onload=\"imageOnload(this);\"/>";
						}
						html += "</span>"
						html += "</li>";
					}
					$("#imgList").empty();
					$("#imgList").append(html);
					
					drawpenList = [];
					for(var i=0;i<imgList.length;i++){
						drawpenList[i] = new DrawPen($("#drawCanvas"+i+"")[0]);
						drawpenList[i].drawCallBack = savePen;
						$(".viewNum").show();
					}
					
					if (imgList != null && imgList.length > 0) {
						imgMove(imgPenIndex);
					}
					
					//이미지 없을때 full영상에 나오던 video 다시 그려주기
					if(fullUserChk){
						var userName = "";
						for (var i = 0; i < userList.length; i++) {
							var row = userList[i];
							if (row.cusNo == fullUserId) {
								userName = row.cusNn;
							}else if(row.hwnno == fullUserId){
								userName = row.hwnnm;				
							}
						}
						var vhtml = "";
						var videoArea = $("#movList");
						var videoId = "VD_"+fullUserId;
						var imgId = "IMG_"+fullUserId;
						fullUserChk = false;
						videoArea.append("<li id='DV_"+fullUserId+"'><img id=\""+imgId+"\" class=\"tempVideo\" src=\"/mov/stb/images/img_temp_video1.png\" style=\"display:none; height:100%; width:100%;\"><video id='"+videoId+"' class='tempVideo' autoplay playsinline' style='transform:scaleX(-1); object-fit:contain;'></video><div class='name'>"+userName+"</div></li>");
						
						videoObj = $("#"+videoId);
						
						setVideoSize();
					
						var video = videoObj[0];
						try {
							video.srcObject = fullUserData;  
						} catch (e) {
							video.src = window.URL.createObjectURL(fullUserData);
						}
						
						if (type == spass.isScreenStream) {
							video.style.transform = "scaleX(1)";
						} else {
							video.style.transform = "scaleX(-1)";
						}
					}
				}
				$(".loading").hide();
			}
		}, function(e) {
			alert("컨텐츠(파일) 정보 조회 중에 오류가 발생하였습니다. 관리자에게 문의 하시기 바랍니다.");
		}
	);
}

//이미지 이동(0부터 넘버링)
function imgMove(num){
	$(".view ul").css({"transform":"translateX(-"+($(".view").width()*num)+"px)"});
	$(".viewNum .num").text(num+1);
}

/**
 * 2자리로 마춤
 * @param intVal
 * @returns {String}
 */
function getTwoNumber(intVal) {
	var strVal = new String(intVal);
	if (strVal.length == 1) {
		return "0"+strVal;
	} else {
		return strVal;
	}
}

/**
 * 영상 개수에 따라 재배치
 */
function setVideoPlace(){
	var videoNum = $(".userList ul li").length;
	if (videoNum < 4){
		$("#movList").removeClass();
	}else if(videoNum == 4){
		$("#movList").attr("class","col4");
	}else{
		$("#movList").attr("class","col5");
	}
}

