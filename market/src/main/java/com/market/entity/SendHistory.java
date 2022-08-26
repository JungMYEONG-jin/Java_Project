package com.market.entity;

import javax.persistence.*;

@Entity
@Table(name = "MBM_MARKET_SEND_HISTORY")
public class SendHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    @Column(name = "APP_ID", length = 256)
    private String appId;
    @Column(name = "SEND_STATUS", length = 20)
    private String sendStatus;
    @Column(name = "ERROR_MSG", length = 4000)
    private String errorMsg;
    @Column(name = "REQ_USER_ID", length = 20)
    private String userId;
    @Column(name = "REG_DT", length = 14)
    private String regDt;
    @Column(name = "ETC1", length = 4000)
    private String etc1;
    @Column(name = "ETC2", length = 4000)
    private String etc2;
    @Column(name = "ETC3", length = 4000)
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

    @Override
    public String toString() {
        return "SendHistory{" +
                "id=" + id +
                ", appId='" + appId + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", userId='" + userId + '\'' +
                ", regDt='" + regDt + '\'' +
                ", etc1='" + etc1 + '\'' +
                ", etc2='" + etc2 + '\'' +
                ", etc3='" + etc3 + '\'' +
                '}';
    }
}
