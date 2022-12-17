/*********************************************************************
 * @제목 : S-Pass 판서 라이브러리
 * @설명 : 다자간 영상통화 기능으로 확장된 버전
 * @변경이력
 * 1. 2020.04.17 : 최초작성
 *
 * @author PJH
 * @date 2020.04.17
 ********************************************************************/
var penWidth = 1;
var penColor = "#000000";
var penEraserWidth = 20;
var isPenDraw = false;
var isPenEraser = false;

// 터치 이벤트
function DrawPenEvent(eventName, x, y, elementId) {
	this.x = x;
	this.y = y;
	this.elementId = elementId;
	this.eventName = eventName;

	// 포인트를 보내서 그리도록하기  위한 정보
	this.cavasWidth = 0;
	this.cavasHeight = 0;
	this.drawCallBack = null;

};

// 통신으로 보내기 위한 문자열 생성
DrawPenEvent.prototype.toJson = function () {
	return {
		x : this.x,
		y : this.y,
		elementId : this.elementId,
		eventName : this.eventName,
		cavasWidth : this.cavasWidth,
		cavasHeight : this.cavasHeight
	};
};


// 통신으로 받은 이벤트 생성
DrawPenEvent.prototype.setJson = function (json) {
	this.x = json.x;
	this.y = json.y;
	this.elementId = json.elementId;
	this.eventName = json.eventName;
	this.cavasWidth = json.cavasWidth;
	this.cavasHeight = json.cavasHeight; 
};

function DrawPen(canvas, sframeChat, isEnable) {
	this.canvas = canvas;
	this.ctx = this.canvas.getContext('2d');
	this.isMouseDraw = false;
	this.arrayEvent = [];
	this.sframeChat = sframeChat;

	// 터치 팬이벤트로 변환
	if (isEnable != false) {
		var createDrawPenEvent = function(eventName, e) {
			// try {e.preventDefault();} catch (e){}
			var penEvent = null;
			var x = 0;
			var y = 0;
			const margin = 14;

			if (e.touches.length > 0) {
				var touch = e.touches[0];
				var rect = $("#"+e.srcElement.id+"")[0].getBoundingClientRect();

				x = e.clientX - rect.left + (margin / 2);
				y = e.clientY - rect.top + (margin / 2);
			}

			var ev = new DrawPenEvent(eventName, x, y, e.srcElement.id);
			ev.cavasWidth = this.canvas.offsetWidth + margin;
			ev.cavasHeight = this.canvas.offsetHeight + margin;

			return ev;
		}.bind(this);

		this.canvas.addEventListener("touchstart", function(e) {
			this.isMouseDraw = true;
			this.touchstart(createDrawPenEvent("touchstart", e));
		}.bind(this), {passive: true});

		this.canvas.addEventListener("touchmove", function(e) {
			this.touchmove(createDrawPenEvent("touchmove", e));
		}.bind(this), {passive: true});
		this.canvas.addEventListener("touchend", function(e) {
			this.isMouseDraw = false;
			this.touchend(createDrawPenEvent("touchend", e));
		}.bind(this), {passive: true});


		var createDrawPenEventMouse = function (eventName, e) {
			var rect = $("#"+e.srcElement.id+"")[0].getBoundingClientRect();
			var x = 0;
			var y = 0;
			const margin = 14;

			x = e.clientX - rect.left + (margin / 2);
			y = e.clientY - rect.top + (margin / 2);

			var ev = new DrawPenEvent(eventName, x, y, e.srcElement.id);
			ev.cavasWidth = this.canvas.offsetWidth + margin;
			ev.cavasHeight = this.canvas.offsetHeight + margin;

			var canvas = document.getElementById(e.srcElement.id);

			var myWidth = Math.round(canvas.width);
			var myHeight = Math.round(canvas.height);

			// 비율 계산하기 넣어야됨
			var x2 = ev.x;
			var y2 = ev.y;

			if (myWidth != Math.round(ev.cavasWidth) || myHeight != Math.round(ev.cavasHeight)) {
				var rateX = myWidth / ev.cavasWidth;
				var rateY = myHeight / ev.cavasHeight;

				var ratePen = (rateX + rateY) / 2;
				x2 = x2 * rateX;
				y2 = y2 * rateY;
			}
			
			return new DrawPenEvent(eventName, x2, y2, e.srcElement.id);
		}.bind(this);

		this.canvas.addEventListener("mousedown", function(e) {
			this.isMouseDraw = true;
			this.touchstart(createDrawPenEventMouse("mousedown", e));
		}.bind(this));

		this.canvas.addEventListener("mousemove", function(e) {
			if (this.isMouseDraw) {
				this.touchmove(createDrawPenEventMouse("mousemove", e));
			}
		}.bind(this));
		this.canvas.addEventListener("mouseup",function(e) {
			this.isMouseDraw = false;
			this.touchend(createDrawPenEventMouse("mouseup", e));
		}.bind(this));
	}
};

/**
 * 화면터치
 */
DrawPen.prototype.touchstart = function(e) {
	this.arrayEvent = [];
	this.arrayEvent[this.arrayEvent.length] = e.toJson();

	this.ctx.beginPath();
	this.ctx.moveTo(e.x, e.y);
	this.touchmove(e);
};

/**
 * 터치이동
 */
DrawPen.prototype.touchmove = function(e) {
	if (!isPenEraser && isPenDraw && this.isMouseDraw) {
		this.arrayEvent[this.arrayEvent.length] = e.toJson();
		this.ctx.lineWidth = penWidth;
		this.ctx.lineCap = 'round';
		this.ctx.lineTo(e.x, e.y);
		this.ctx.moveTo(e.x, e.y);
		this.ctx.strokeStyle = penColor;
		this.ctx.closePath();
		this.ctx.stroke();
	}else if(isPenEraser && !isPenDraw && this.isMouseDraw) {
		this.arrayEvent[this.arrayEvent.length] = e.toJson();
		this.ctx.clearRect(e.x,e.y,penEraserWidth,penEraserWidth);
	}
};

DrawPen.prototype.touchend = function(e) {
	this.arrayEvent[this.arrayEvent.length] = e.toJson();
	this.sendDrawData();
};

/**
 * 판서색 수정
 * @param e
 */
DrawPen.prototype.setColor =  function(e) {
	e.parent().siblings().find("a").removeClass("on").find("em").remove();
	e.addClass("on").append("<em>선택</em>");
	$(".color").attr("class","color "+e.attr("class"));
	if(e.attr("class") == "black on"){
		penColor = "#000000";
	}else if(e.attr("class") == "white on"){
		penColor = "#FFFFFF";
	}else if(e.attr("class") == "red on"){
		penColor = "#FF0000";
	}else if(e.attr("class") == "yellow on"){
		penColor = "#FFFF00";
	}else if(e.attr("class") == "blue on"){
		penColor = "#0000FF";
	}else if(e.attr("class") == "aqua on"){
		penColor = "#00FFFF";
	}else if(e.attr("class") == "hotpink on"){
		penColor = "#FF69B4";
	}else if(e.attr("class") == "lightgreen on"){
		penColor = "#90e90e";
	}else if(e.attr("class") == "blueviolet on"){
		penColor = "#8a2be2";
	}else{
		penColor = "#20A447";
	}
	$(".subMemu.palette").slideUp(300);
};

/**
 * 판서 두께 수정
 * @param e
 */
DrawPen.prototype.setWidth = function(e){
	isPenDraw = true;
	isPenEraser = false;
	e.parent().siblings().find("a").removeClass("on");
	e.addClass("on");
	$(".width").attr("class","width "+e.attr("class"));
	if(e.attr("class") == "light on"){
		penWidth = 1;
	}else if(e.attr("class") == "normal on"){
		penWidth = 3;
	}else{
		penWidth = 5;
	}
	$(".subMemu.line").slideUp(300);
};


/**
 * 전체 지우기, 일부지우기 기능
 * @param flag
 */
DrawPen.prototype.remove = function(e,flag){
	if(flag == "All"){
		this.clearCanvas();
		isPenDraw = false;
		var msg = {
				"type" : "clearImg",
				"elementId" : this.canvas.id
		};

		if (sframeChat) {
			sframeChat.sendMsg(msg);
		}
	}else{
		if(!isPenEraser){
			isPenEraser = true;
			isPenDraw = false;
		}else{
			isPenEraser = false;
			isPenDraw = true;
		}
	}
};

DrawPen.prototype.clearCanvas = function() {
	this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
	this.ctx.beginPath();
};

/***********************************************
 * 네트워크 통신을 이용한 정보 그리기
 ***********************************************/
// 보내기
DrawPen.prototype.sendDrawData = function () {
	if (this.arrayEvent.length > 2) {
		var sendData = {
				penWidth : penWidth,
				penColor : penColor,
				penEraserYN : isPenEraser,
				eventList : this.arrayEvent,
				penEraserWidth : penEraserWidth
		};

		var msg = {
				"type" : "drawImg",
				"data" : sendData
		};

		if (sframeChat) {
			sframeChat.sendMsg(msg);
		}

		if (this.drawCallBack) {
			this.drawCallBack(sendData);
		}
	}

	this.arrayEvent = [];
};

// 그리기
DrawPen.prototype.drawNetworkPen = function (drawData) {
	// 실제 그리기
	for (var i = 0; i < drawData.eventList.length-1; i++) {
		var e = new DrawPenEvent();
		e.setJson(drawData.eventList[i]);

		var canvas = document.getElementById(e.elementId);

//		var myWidth = Math.round(canvas.width);
//		var myHeight = Math.round(canvas.height);

		// 비율 계산하기 넣어야됨
		var x = e.x;
		var y = e.y;
		var penEraserWidth = drawData.penEraserWidth;
		var penWidth = parseFloat(drawData.penWidth);
//
//		if (myWidth != Math.round(e.cavasWidth) || myHeight != Math.round(e.cavasHeight)) {
//			var rateX = myWidth / e.cavasWidth;
//			var rateY = myHeight / e.cavasHeight;
//
//			var ratePen = (rateX + rateY) / 2;
//			x = x * rateX;
//			y = y * rateY;
////			console.log("original penWidth:[" + penWidth + "], ratePen: [" + ratePen + "], adjustedPenWidth: [" + (ratePen * penWidth) + "]");
//			penWidth = ratePen * penWidth;
//			penEraserWidth = ratePen * penEraserWidth;
//		}
//
		ctx = canvas.getContext('2d');
//
//		// 점 그리기가 않되어 수정함
		if (i == 0) {
			ctx.beginPath();
		}

		if (drawData.penEraserYN) {
			ctx.clearRect(x,y,penEraserWidth,penEraserWidth);
		} else {
			ctx.lineWidth = penWidth;
			ctx.lineCap = 'round';
			ctx.lineTo(x, y);
			ctx.moveTo(x, y);
			ctx.strokeStyle = drawData.penColor;
			ctx.stroke();
		}
	}
};
