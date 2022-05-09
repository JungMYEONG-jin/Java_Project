// 은행 선택 화면 전용 
var bankImg = {
				"088"		: "img_bank1.png",
				"035"		: "img_bank2.png",
				"004"		: "img_bank3.png",
				"011"		: "img_bank4.png",
				"020"		: "img_bank5.png",
				"081"		: "img_bank6.png",
				"005"		: "img_bank7.png",
				"071"		: "img_bank8.png",
				"003"		: "img_bank9.png",
				"023"		: "img_bank10.png",
				"053"		: "img_bank11.png",
				"061"		: "img_bank12.png",
				"007"		: "img_bank13.png",
				"002"		: "img_bank14.png",
				"055"		: "img_bank15.png",
				"050"		: "img_bank16.png", 
				"012"		: "img_bank17.png", 
				"048"		: "img_bank18.png", 
				"045"		: "img_bank19.png", 
				"039"		: "img_bank20.png", 
				"037"		: "img_bank21.png", 
				"034"		: "img_bank22.png", 
				"032"		: "img_bank23.png", 
				"031"		: "img_bank24.png",
				"054"		: "img_bank25.png", 
				"057"		: "img_bank26.png", 
				"060"		: "img_bank27.png", 
				"064"		: "img_bank28.png", 
				"062"		: "img_bank29.png", 
				"089"		: "img_bank30.png", 
				"090"		: "img_bank31.png", 
				"056"		: "img_bank32.png", 
				"027"		: "img_bank11.png",
				
				"278"		: "img_stock1.png",
				"209"		: "img_stock2.png",
				"218"		: "img_stock3.png",
				"227"		: "img_stock4.png",
				"230"		: "img_stock5.png",
				"238"		: "img_stock6.png",
				"240"		: "img_stock7.png",
				"243"		: "img_stock8.png",
				"247"		: "img_stock9.png",
				"261"		: "img_stock10.png",
				"262"		: "img_stock11.png",
				"263"		: "img_stock12.png",
				"264"		: "img_stock13.png",
				"265"		: "img_stock14.png",
				"266"		: "img_stock15.png",
				"267"		: "img_stock16.png", 
				"268"		: "img_stock17.png", 
				"269"		: "img_stock18.png", 
				"270"		: "img_stock19.png", 
				"279"		: "img_stock20.png", 
				"280"		: "img_stock21.png", 
				"287"		: "img_stock22.png", 
				"290"		: "img_stock23.png", 
				"291"		: "img_stock24.png", 
				"292"		: "img_stock25.png", 
				"294"		: "img_stock26.png", 
				"289"		: "img_stock27.png"
			};

// 단순 로딩바 호출
// @param code : Y or N
function showLodingBar(isShowLoadingBar, callBackFunc){
	var params = {
			  "pluginId" : 'webCommon'
			, "method"   : 'progress'
			, "params"   : { "code" :  isShowLoadingBar}
	        , "callBack" : function(isOK) {
	        	// 프로그레스바 Y일떄 다음 동작 실행 
	        	if(isOK){
	        		
	        		if(typeof(callBackFunc) == 'function'){
	        			callBackFunc();	
	        		}
	        	}
	        }
		};		
	    SDSFrameWork.plugin.execute(params);
}

// 비대면 액티비티 종료
// @param flag : 액티비티를 몇개 닫을건지에 대한 구분값 ex) 영상통화 대기의 경우 화면을 하나 더 띠우는 구조이기 때문에 두개 닫아야함.
var tempParams = null;
function exitNofaceActivity(params){
	
	if(tempParams == null){ //  팝업이 닫혔는지 여부 판단
		tempParams = params;
		
		var popStr = "";
		popStr+='<div class=\"layerPopWrap hasDim\"id=\"confirmPop\" style=\"display : block;\">';
		popStr+='	<div class=\"popInner\">';
		popStr+='		<div class=\"layerCon\">';
		popStr+='			<div class=\"contentsWrap\">';
		popStr+='				<div class=\"contents full3\">';
		popStr+='					<p class=\"fontN2\">';
		popStr+='						비대면 실명 인증이 완료되지<br>않았습니다. 비대면 실명 인증을<br>종료하시겠습니까?';
		popStr+='					</p>';
		popStr+='				</div>';
		popStr+='			</div>';
		popStr+='		</div>';
		popStr+='		<div class=\"btnGroupBox floating\">';
		popStr+='			<a href=\"#\" onclick=\"javascript:confirmPopBtn(false);\" class=\"btnType btnBlack\"><span>취소</span></a>';
		popStr+='			<a href=\"#\" onclick=\"javascript:confirmPopBtn(true);\" class=\"btnType\"><span>확인</span></a>';
		popStr+='		</div>';
		popStr+='	</div>';
		popStr+='</div>';
			
		$("body").append(popStr);
	}
}

// 팝업 닫는 버튼
function confirmPopBtn(isOk){
	if(isOk){
		var params = {
				pluginId : 'webCommon' ,
				method   : 'endCertNoface' ,
				params   : tempParams
			};	
		tempParams = null;
		
		$("#confirmPop").attr("style", "display : none;");
		$("#confirmPop").remove();
		
		SDSFrameWork.plugin.execute(params);
	}else{
		
		tempParams = null;
		$("#confirmPop").attr("style", "display : none;");
		$("#confirmPop").remove();
	
	}
}

// 공통 레이어 팝업
/* json 객체 key : value  
 * {
 *    id         : 호출하는 화면에서 지정하는 id
 *    type       : alert, confirm, closeNoface(셋중의 사용 목적에 따라 선택)
 *    text       : 메시지 
 *    req_key    : req_key
 *    resultCode : resultCode
 * }
 */
function fnPopUp(json){
	var popStr = "";
	popStr += '<div class=\"layerPopWrap hasDim\" id=\"'+json.id+'\" style=\"display : block;\">';
	popStr += '	<div class=\"popInner\">';
	popStr += '		<div class=\"layerCon\">';
	popStr += '			<div class=\"contentsWrap\">';
	popStr += '				<div class=\"contents full3\">';
	popStr += '					<p class=\"fontN2\">';
	popStr += ''+ json.text;
	popStr += '					</p>';
	popStr += '				</div>';
	popStr += '			</div>';
	popStr += '		</div>';
	popStr += '		<div class=\"btnGroupBox floating\">';
	
	if(json.type == 'confirm'){
		popStr += '			<a href=\"#\" onclick=\"javascript:closeConfirmPopBtn(false);\" class=\"btnType btnBlack\"><span>취소</span></a>';
		popStr += '			<a href=\"#\" onclick=\"javascript:closeConfirmPopBtn(true);\" class=\"btnType\"><span>확인</span></a>';	
	}else if(json.type == 'alert'){
		popStr += '			<a href=\"#\" onclick=\"javascript:closeAlertPopBtn();\" class=\"btnType\"><span>확인</span></a>';
	}else if(json.type == 'closeNoface'){
		popStr += '			<a href=\"#\" onclick=\"javascript:closeNofaceBtn(\'' + json.req_key + '\',\'' + json.resultCode + '\');\" class=\"btnType\"><span>확인</span></a>';
	}
	
	popStr += '		</div>';
	popStr += '	</div>';
	popStr += '</div>';
	$("body").append(popStr);
	
}

// 은행목록 팝업 호출 함수 * bankList : 팝업 호출하는 화면에서 전문 호출하여 세팅해서 넘겨주는 문자열
function fnBankListPop(bankList){
	var popStr = "";
	popStr += '<div class=\"layerPopBottomWrap hasDim\" id=\"bankPopup\">';
	popStr += '	<div class=\"popInner\" style=\"display:block;\">';
	popStr += '		<div class=\"layerHeader\">';
	popStr += '			<p class=\"layerTit\">은행 선택</p>';
	popStr += '			<button onclick=\"javascript:bankPopClose();\" type=\"button\" class=\"btnPopClose\">팝업 닫기</button>';
	popStr += '		</div>';             
	popStr += '		<div class=\"layerCon full\">';
	popStr += '			<div class=\"conInner\">';
	popStr += '				<div class=\"tabViewWrap\">';
	popStr += '					<div class=\"tabView on\" id=\"tabBank1\">';
	popStr += '						<ul id=\"bank_list\" class=\"bankList\">';
	popStr +=  bankList;
	popStr += '						</ul>';
	popStr += '					</div>';
	popStr += '				</div>';
	popStr += '			</div>';
	popStr += '		</div>';
	popStr += '	</div>';
	popStr += '</div>';
	
	$("body").append(popStr);
	
	var interval = setInterval(function(){
		if($("#bank_list").html().length > 0){
			clearInterval(interval);
			$("#bankPopup").show();
		}
	}, 300);
	
}

// 은행목록 팝업 닫기
function bankPopClose(){
	contentsIframe.bankPopClose();
}

// 은행 선택시 발생 이벤트 함수. fnBankListPop 의 넘어오는 파라미터  bankList(문자열) 안에 onclick 이벤트로 설정 도어있음.
function selectBank(i){
	contentsIframe.selectBank(i);
}

// 계좌 목록 팝업 호출 함수 * accountList : 팝업 호출하는 화면에서 전문 호출하여 세팅해서 넘겨주는 문자열 
function fnAccountListPop(accountList){
	var popStr = "";
	popStr += '<div class=\"layerPopBottomWrap hasDim\" id=\"accountPopup\">';
	popStr += '	<div class=\"popInner\" style=\"display:block;\">';
	popStr += '		<div class=\"layerHeader\">';
	popStr += '			<p class=\"layerTit\">내계좌</p>';
	popStr += '			<button onclick=\"javascript:accountPopClose();\" type=\"button\" class=\"btnPopClose\">팝업 닫기</button>';
	popStr += '		</div>';
	popStr += '		<div class=\"layerCon full\">';
	popStr += '			<div class=\"conInner\">';
	popStr += '				<div class=\"listOneLineBox type1\">';
	popStr += '					<ul id=\"account_list\">';
	popStr += accountList;
	popStr += '					</ul>';
	popStr += '				</div>';
	popStr += '			</div>';
	popStr += '		</div>';
	popStr += '	</div>';
	popStr += '</div>';
	
	$("body").append(popStr);
	
	var interval = setInterval(function(){
		if($("#account_list").html().length > 0){
			clearInterval(interval);
			$("#accountPopup").show();
		}
	}, 300);
}

// 계좌목록 팝업 닫기
function accountPopClose(){
	contentsIframe.accountPopClose();
}

// 계좌 선택시 발생 이벤트 함수. fnAccountListPop 의 넘어오는 파라미터  accountList(문자열) 안에 onclick 이벤트로 설정 도어있음.
function selectAccount(i){
	contentsIframe.selectAccount(i);
}

//nativeAlertExit 대체
function closeNofaceBtn(req_key, resultCode){
	$("#closeNoface").remove();
	
	var params = {
			pluginId : 'webCommon' ,
			method   : 'endCertNoface' ,
			params   : {req_key : req_key , resultCode : resultCode} 
		};	
	SDSFrameWork.plugin.execute(params);
}
