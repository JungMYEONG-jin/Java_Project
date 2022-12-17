<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jspf"%>
<%@ include file="/WEB-INF/jsp/common/include/variables.jspf"%>
<%
long ts = System.currentTimeMillis();
%>
<c:set var="xfinger" value="/resources/xfinger" />
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>신한은행 비대면 실명인증  영상상담 시스템 │ 신한은행</title>

	<%@ include file="/WEB-INF/jsp/common/include/pc_common.jspf"%>
	<style type="text/css">
	#waiting{background: rgba(0, 0, 0, 0.75); color: white; display: table; width:486px; height:486px;}
	#waiting>div{display:table-cell; vertical-align:middle; text-align:center;}
	.waitloader{border:5px solid #f3f3f3; animation: spin 1s linear infinite; border-top:5px solid #555; border-radius:50%; width:50px; height:50px; margin: 0 auto; margin-bottom:20px;}
	@keyframes spin{
		0% {transform: rotate(0deg);}
		100% {transform: rotate(360deg);}
	}
	.disable{opacity:0.5;}
	</style>
	<%-- 자바스크립트 구문 --%>
	<script type="text/javascript">
	const reqKey = '${param.reqKey}';
	const hwnNo = '${param.hwnNo}';
	const hwnName = '${param.hwnName}';
	const userNo = '${param.userNo}';
	</script>
	<script src="/resources/spass_common/lib/adapter-latest.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/drawPen-lib-1.0.0.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/sframe-utils-1.0.0.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/sframe-chat-1.0.0.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/getStats.min.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/spass-log-lib-1.0.0.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/spass-lib-1.1.0.js?a=<%=ts%>"></script>
	<script src="/resources/spass_common/lib/html2canvas.js?a=<%=ts%>"/></script>
	<script src="/resources/spass_common/lib/html2canvas.min.js?a=<%=ts%>"/></script>
	<script src="/resources/pc/js/viewCam_new.js?a=<%=ts%>"></script>
</head>
<body class="counseling">
<div class="wrap">
	<header>
		<div>
			<h1><img src="${pc}/images/common/title02.png" alt="신한은행 비대면 실명인증 영상상담 시스템" /></h1>
			<div class="fr">
				<p class="lnb_logo"><img src="${pc}/images/common/img_logo.png" alt="신한은행 로고" /></p>
				<dl class="cont_profile">
					<dt>
						<span>
							<c:if test="${empty param.photo}"><img src="${pc}/images/counseling/titlephoto.png" alt="${param.hwnName}" /></c:if>
							<!-- 20-11-02 LSA 상담사 사진 쏠 캐릭터로 일괄 변경 --> 
							<%--<c:if test="${not empty param.photo}"><img id="consultantPhoto" src="data:image/png;base64,${param.photo}" alt="${param.hwnName}" style="width:100%" /></c:if> --%>
							<img id="consultantPhoto" src="${pc}/images/counseling/counceling_sol_img.png" alt="${param.hwnName}" style="width:100%" />
						</span>
						${param.hwnName} ${param.level}
					</dt>
					<dd>${param.team}<%-- 남문지점? --%></dd>
				</dl>
			</div>

		</div>
	</header>
	<div class="container">
		<section class="lnb">
			<div class="videocall">
				<div class="box_video">
					<div id="waiting"><div><div class="waitloader"></div><span>상담 화면 대기 중...</span></div></div>
					<img src="${pc}/images/counseling/img_video_blind.png" class="dis_n" alt="화면 가리기" />
					<video width="486" height="486" id="video_remote" autoplay>영상통화 화면</video>
					<video width="486" height="486" id="video_local" muted autoplay class="dis_n">영상통화 화면</video>
				</div>
				<dl>
					<dt><img src="${pc}/images/counseling/img_video_timetit.png" alt="상담시간" /></dt>
					<dd>
						<img src="${pc}/images/counseling/icon_rec.png" alt="REC" />
						<span id="callTimeSpan">00:00:00</span>
					</dd>
				</dl>
				<p class="box_btn"><a href="#" id="micMuteBtn" class="btn_mic_mute">마이크 OFF</a></p>
				<p class="box_btn"><a href="#" id="videoBlindBtn" class="btn_video_blind">화면가리기</a></p>
			</div><!--// videocall  -->
			<div class="table_box">
				<table class="t_list02">
					<caption>영상통화인증 대기목록</caption>
					<colgroup>
						<col style="width:109px;" />
						<col style="width:105px;" />
						<col style="width:109px;" />
						<col style="width:144px;" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="row">고객명</th>
							<td id="userName">${param.userName}</td>
							<th scope="row">연락처</th>
							<td id="userTel">${param.telNo}</td>
						</tr>
						<tr>
							<th scope="row">모바일 OS</th>
							<td id="userDevice"><c:if test="${param.deviceModel eq 'I'}">iOS</c:if><c:if test="${param.deviceModel eq 'A'}">Android</c:if></td>
							<th scope="row">업무구분</th>
							<td id="userMenu"><span class="f_dbule f_bold">${param.appName}</span><br/>${param.menuName}</td>
						</tr>
						<tr>
							<th scope="row">실명증표</th>
							<td colspan="3"><a href="#pop_detail" class="btn_veiw_detail btn_layer btn_click">자세히 보기</a>
							<a href="#pop_detail_screen" onClick="shoot();" class="btn_veiw_detail btn_layer">영상 캡처</a></td>
						</tr>
					</tbody>
				</table>
			</div><!--// table_box  -->
			<div class="pop_layer" id="pop_detail" style="display:none;">
				<img id="idCardImg" src="${pc}/images/thum_identification.png" alt="" />
				<a href="#none" class="btn_expand" id="btn_expand_img">확대</a>
				<a href="#none" class="btn_refresh_blue">새로고침</a>
				<a href="#none" class="btn_close">닫기</a>
			</div><!--// pop_layer -->
			<div class="pop_layer" id="pop_detail_screen" style="display:none; width:500px; height:439px; margin-left:511px; margin-bottom:10px;">
				<div id = "output"></div>
				<a href="#none" class="btn_expand" id="btn_expand_screen" style="right:43px;">확대</a>
				<a href="#none" class="btn_close">닫기</a>
			</div><!--// screenshot_layer -->
		</section><!--// lnb -->
		<section class="cont">
			<ul class="lst_progress">
				<li class="focus" data-step="1"><img src="${pc}/images/counseling/img_guide01.png" alt="영상통화"/></li>
				<li data-step="2"><img src="${pc}/images/counseling/img_guide02.png" alt="진행안내"/></li>
				<li data-step="3"><img src="${pc}/images/counseling/img_guide03.png" alt="고객정보 확인"/></li>
				<li data-step="4"><img src="${pc}/images/counseling/img_guide04.png" alt="실명증표대조"/></li>
				<li data-step="5"><img src="${pc}/images/counseling/img_guide05.png" alt="대기"/></li>
				<li data-step="6"><img src="${pc}/images/counseling/img_guide06.png" alt="최종완료"/></li>
			</ul>
			<h2><img src="${pc}/images/counseling/tit_progress.png" alt="진행 멘트 안내" /></h2>
			<div class="cont_progress">
				<img src="${pc}/images/counseling/img_ment01.png" alt="" data-step="1"/>
				<img src="${pc}/images/counseling/img_ment02.png" alt="" class="dis_n" data-step="2"/>
				<img src="${pc}/images/counseling/img_ment03.png" alt="" class="dis_n" data-step="3"/>
				<img src="${pc}/images/counseling/img_ment04.png" alt="" class="dis_n" data-step="4"/>
				<img src="${pc}/images/counseling/img_ment05.png" alt="" class="dis_n" data-step="5"/>
				<img src="${pc}/images/counseling/img_ment06.png" alt="" class="dis_n" data-step="6"/>
				<div class="btn_box dis_n">
					<a href="#pop_disapproval" class="btn_layer"><img src="${pc}/images/btn/btn_disapproval.png" alt="미승인" /></a>
					<a href="#pop_approval" class="btn_layer"><img src="${pc}/images/btn/btn_approval.png" alt="승인" /></a>
				</div>
			</div>
			<div class="btn_box cl">
				<p class="fl">
					<%-- <a href="#pop_end" class="btn_layer btn_exit disable"><img src="${pc}/images/btn/btn_end.png" alt="상담종료" /></a>  --%>
					<a class="btn_layer btn_exit" href="#pop_end_default"><img src="${pc}/images/btn/btn_end.png" alt="상담종료" /></a>
					<a class="btn_layer btn_declare disable"><img src="${pc}/images/btn/btn_declare.png" alt="신고" /></a>
					<a class="btn_layer btn_doubt disable"><img src="${pc}/images/btn/btn_doubt.png" alt="의심거래 등록" /></a>
<%-- 					<a class="btn_layer btn_send_guide" id="sendGuide"><img id="btn_send_guide" src="${pc}/images/btn/btn_sound.png"></a> --%>
				</p>
				<p class="fr">
					<a href="#none" id="prevStepBtn" class="btn_pre focus">이전</a>
					<a href="#none" id="nextStepBtn" class="btn_next focus">다음</a>
				</p>
			</div>
		</section><!-- cont -->
		<section class="popup_layer" id="pop_approval" style="display:none;">
			<div class="popup_cont">
				<p>
					<span>비대면 실명인증을<br/>최종 <em class="f_bule">승인</em>합니다.</span>
					<a href="#none" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:updateConsultResult( true );" class="btn_check" id="approvalBtn">확인</a>
				</div>
			</div>
		</section><!-- popup_layer -->
		<section class="popup_layer" id="pop_disapproval" style="display:none;">
			<div class="popup_cont">
				<p>
					<span>비대면 실명인증을<br/>최종 <em class="f_red">미 승인</em>합니다.</span>
					<a href="#none" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:updateConsultResult( false );" class="btn_check">확인</a>
				</div>
			</div>
		</section>
		<!-- popup_layer -->
		<section class="popup_layer" id="pop_end" style="display:none;">
			<div class="popup_cont">
				<p>
					<span>영상상담을<br/>종료하시겠습니까?</span>
					<a href="#none" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:forceExitConsultRoom('exitButton');" class="btn_check">확인</a>
				</div>
			</div>
		</section>
		<!-- popup_layer -->
		<section class="popup_layer" id="pop_end_default" style="display:none;">
			<div class="popup_cont">
				<p>
					<span>영상상담을<br/>종료하시겠습니까?</span>
					<a href="#none" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:forceExitConsultRoom('exitDefault');" class="btn_check">확인</a>
				</div>
			</div>
		</section>
		<!-- popup_layer -->
		<section class="popup_layer" id="pop_declare" style="display:none;">
			<div class="popup_cont">
				<p>
					<span>상담중인 고객을 신고하고<br/>종료하시겠습니까?</span>
					<a href="#none" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:forceExitConsultRoom('declareButton');" class="btn_check">확인</a>
				</div>
			</div>
		</section>
		<!-- popup_layer -->
		<section class="popup_layer" id="pop_exit" style="display:none;">
			<div class="popup_cont">
				<p>
					<span></span>
					<a href="javascript:forceExitConsultRoom('exception');" class="pop_btnclose"><img src="${pc}/images/common/pop_btnclose.png" alt="닫기" /></a>
				</p>
				<div class="pop_box_btn">
					<a href="javascript:forceExitConsultRoom('exception');" class="btn_check">확인</a>
				</div>
			</div>
		</section><!-- popup_layer -->
	</div><!-- container -->

	<!-- Loding Image layer -->
	<section class="loedingImagePlay">
		<div class="loadingIMG">
			<img src="${pc}/images/loding/img_loading01.png" class="focus" alt="">
			<img src="${pc}/images/loding/img_loading02.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading03.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading04.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading05.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading06.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading07.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading08.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading09.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading10.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading11.png" class="" alt="">
			<img src="${pc}/images/loding/img_loading12.png" class="" alt="">
		</div>
	</section>
</div><!-- wrap -->

<%-- Recording File Upload From --%>
<form id="RecordingFileUploadForm" name="FileUploadForm" enctype="multipart/form-data">
    <input type="hidden" name="req_key"/>
    <input type="file" name="movie_data" style="display:none;"/>
    <input type="hidden" name="hwnno"/>
    <input type="hidden" name="hwnname"/>
    <input type="hidden" name="succ_yn"/>
    <input type="hidden" name="etc3" id="etc3"/>
    <input type="hidden" name="etc4" id="etc4"/>
</form>
</body>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_define.js?a=<%=ts%>"></script> --%>
<script type="text/javascript" src="${xfinger}/xfinger_util.js?a=<%=ts%>"></script>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_sipevent.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_asevent.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_config.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_record.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_corelib.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_sipmain.js?a=<%=ts%>"></script> --%>
<%-- <script type="text/javascript" src="${xfinger}/xfinger_asmain.js?a=<%=ts%>"></script> --%>
</html>