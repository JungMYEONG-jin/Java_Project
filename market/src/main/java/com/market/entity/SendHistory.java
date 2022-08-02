package com.market.entity;

import javax.persistence.*;

@Entity
@Table(name = "MBM_MARKET_SEND_HISTORY")
public class SendHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    private String appId;
    private String sendStatus;
    @Column(length = 1000)
    private String errorMsg;
    private String userId;
    private String regDt;
    private String etc1;
    private String etc2;
    private String etc3;

    public SendHistory() {
    }

    public SendHistory(Long id, String appId, String sendStatus, String errorMsg, String userId, String regDt, String etc1, String etc2, String etc3) {
        this.id = id;
        this.appId = appId;
        this.sendStatus = sendStatus;
        this.errorMsg = errorMsg;
        this.userId = userId;
        this.regDt = regDt;
        this.etc1 = etc1;
        this.etc2 = etc2;
        this.etc3 = etc3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getEtc1() {
        return etc1;
    }

    public void setEtc1(String etc1) {
        this.etc1 = etc1;
    }

    public String getEtc2() {
        return etc2;
    }

    public void setEtc2(String etc2) {
        this.etc2 = etc2;
    }

    public String getEtc3() {
        return etc3;
    }

    public void setEtc3(String etc3) {
        this.etc3 = etc3;
    }
}
