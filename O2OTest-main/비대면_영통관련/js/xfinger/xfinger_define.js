/**
 * auth    : jmkim
 * file    : m2soft_define.js
 * create  : 2016-11-15
 * modify  : 2017-01-16
 * version : 1.0.3
 * desc    : define 관리
 */

var SIP_REGISTER_EXPIRE_TIME = 360; // SIP 종료 시간 

// 응답코드 구분
var ERROR_CODE_1 = "0000"; // 정상적으로 처리하였습니다.
var ERROR_CODE_2 = "9999"; // 데이터 처리 중에 오류가 발생하였습니다.
var ERROR_CODE_3 = "0001"; // 검색자료가 존재 하지 않습니다.
var ERROR_CODE_4 = "9001"; // 입력 파라메터 오류 입니다.
var ERROR_CODE_5 = "1001"; // 타행 계좌 인증 실명증표 승인 상태
var ERROR_CODE_6 = "1002"; // 타행 계좌 인증 실명증표 미승인 상태
var ERROR_CODE_7 = "2001"; // 실명증표 확인 대기 상태

// 사용자 타입 구분
var USER_CONSULTANT = 0;    // 상담원
var USER_CUSTOMER   = 1;    // 고객
var USER_TYPE       = -1;

// SIP 상태 구분
var SIP_NONE        = -1000;
var SIP_STACK_START = SIP_NONE + 1;
var SIP_REGISTER    = SIP_NONE + 2;
var SIP_INVITE      = SIP_NONE + 3;
var SIP_ACCEPT      = SIP_NONE + 4;
var SIP_CANCEL      = SIP_NONE + 5;
var SIP_BYE         = SIP_NONE + 6;
var SIP_RELEASE     = SIP_NONE + 7;
var SIP_STATUS      = SIP_NONE;

// 영상 인증 진행 상태 구분
var VERIFY_STATE_1  = 1;  // 실명증표등록, 인증요청
var VERIFY_STATE_2  = 2;  // 실명증표확인중
var VERIFY_STATE_3  = 3;  // 실명증표완료
var VERIFY_STATE_4  = 4;  // 영상요청
var VERIFY_STATE_5  = 5;  // 영상인증중
var VERIFY_STATE_6  = 6;  // 연결해제
var VERIFY_STATE_10 = 10; // 승인
var VERIFY_STATE_11 = 11; // 실명증표확인실패
var VERIFY_STATE_12 = 12; // 상담종료
var VERIFY_STATE_13 = 13; // 미승인

// 상담 진행 상태 구분
var CONSULTING_STATE_1 = 1; // 영상통화시작
var CONSULTING_STATE_2 = 2; // 진행안내
var CONSULTING_STATE_3 = 3; // 고객정보확인
var CONSULTING_STATE_4 = 4; // 실명증표대조
var CONSULTING_STATE_5 = 5; // 대기
var CONSULTING_STATE_6 = 6; // 최종완료
 
/**
 * @ desc  : 에러코드 정보 리턴 메소드
 */
var getErrorCodeString = function(code) {
	var str = "";
	switch(code) {
		case ERROR_CODE_1 :
			str = "정상적으로 처리하였습니다.";
			break;
		case ERROR_CODE_2 :
			str = "데이터 처리 중에 오류가 발생하였습니다.";
			break;
		case ERROR_CODE_3 :
			str = "검색자료가 존재 하지 않습니다.";
			break;
		case ERROR_CODE_4 :
			str = "입력 파라메터 오류 입니다.";
			break;
		case ERROR_CODE_5 :
			str = "타행 계좌 인증 실명증표 승인 상태";
			break;
		case ERROR_CODE_6 :
			str = "타행 계좌 인증 실명증표 미승인 상태";
			break;
		case ERROR_CODE_7 :
			str = "실명증표 확인 대기 상태";
			break;
	}
	return str;
}

/**
 * @ desc  : SIP 상태 설정 메소드
 */
var setSIPStatus = function(status) {
    SIP_STATUS = status;
}

/**
 * @ desc  : SIP 상태 리턴 메소드
 */
var getSIPStatus = function() {
    return SIP_STATUS;
}
