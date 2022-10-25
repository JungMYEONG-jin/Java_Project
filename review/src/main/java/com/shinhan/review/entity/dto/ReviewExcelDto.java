package com.shinhan.review.entity.dto;

import com.shinhan.review.entity.Review;
import com.shinhan.review.excel.ver2.DefaultBodyStyle;
import com.shinhan.review.excel.ver2.DefaultHeaderStyle;
import com.shinhan.review.excel.ver2.ExcelColumn;
import com.shinhan.review.excel.ver2.ExcelColumnStyle;
import com.shinhan.review.excel.ver2.style.DefaultExcelCellStyle;
import org.json.simple.JSONObject;

import java.time.LocalDate;

@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BLUE_HEADER"))
@DefaultBodyStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY"))
public class ReviewExcelDto {

    @ExcelColumn(headerName = "NO")
    private Long id;
    @ExcelColumn(headerName = "작성일")
    private String createdDate; // 리뷰 작성일
    @ExcelColumn(headerName = "이름")
    private String nickname;
    @ExcelColumn(headerName = "평점")
    private String rating;
    @ExcelColumn(headerName = "분류") // 아직 사용여부는 모름
    private String errorType;
    @ExcelColumn(headerName = "고객민원내용\n[리뷰 내용]")
    private String body; // 리뷰
    @ExcelColumn(headerName = "리뷰 답변")
    private String responseBody;
    @ExcelColumn(headerName = "버전")
    private String appVersion;
    @ExcelColumn(headerName = "기종")
    private String device;
//    @ExcelColumn(headerName = "답변일")
    private String answeredDate; // 답변일
    @ExcelColumn(headerName = "Os\nVer.")
    private String osVer;

    @ExcelColumn(headerName = "앱이름")
    private String appPkg;
//    @ExcelColumn(headerName = "OS")
    private String osType;
    // 기존 리뷰 작성 내용과 동일하게 하기 위해 template 추가

    @ExcelColumn(headerName = "담당자") // 아직 사용여부는 모름
    private String worker;
    @ExcelColumn(headerName = "조치예정일") // 아직 사용여부는 모름
    private String dueDate;
    @ExcelColumn(headerName = "조치완료일") // 아직 사용여부는 모름
    private String finishedDate;
    @ExcelColumn(headerName = "대응 내용\n[담당자작성 : 성명 / 이슈원인 / 조치내용 등]") // 아직 사용여부는 모름
    private String workBody;

    public ReviewExcelDto() {
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getWorkBody() {
        return workBody;
    }

    public void setWorkBody(String workBody) {
        this.workBody = workBody;
    }

    public ReviewExcelDto(Review review){
        this.setAppPkg(review.getAppPkg());
        this.setAppVersion(review.getAppVersion());
        this.setBody(review.getBody());
        this.setCreatedDate(getLocalDateToString(review.getCreatedDate()));
        this.setDevice(review.getDevice());
        this.setNickname(review.getNickname());
        this.setOsType(review.getOsType());
        this.setOsVer(review.getOsVer());
        this.setResponseBody(review.getResponseBody());
    }

    public ReviewExcelDto(Long idx, Review review){
        this.setId(idx);
        this.setAppPkg(review.getAppPkg());
        this.setAppVersion(review.getAppVersion());
        this.setBody(review.getBody());
        this.setCreatedDate(getLocalDateToString(review.getCreatedDate()));
        this.setDevice(review.getDevice());
        this.setNickname(review.getNickname());
        this.setOsType(review.getOsType());
        this.setOsVer(review.getOsVer());
        this.setResponseBody(review.getResponseBody());
        String excelRating = getStar(Integer.parseInt(review.getRating()));
        this.setRating(excelRating);
    }

    private String getStar(int stars) {
        String excelRating="";
        for(int i=0;i<stars;i++){
            excelRating+="★";
        }
        for(int i=0;i<5-stars;i++){
            excelRating+="☆";
        }
        return excelRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalDateToString(String value){
        return LocalDate.of(Integer.parseInt(value.substring(0, 4)), Integer.parseInt(value.substring(4, 6)), Integer.parseInt(value.substring(6, 8))).toString();
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(String answeredDate) {
        this.answeredDate = answeredDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
