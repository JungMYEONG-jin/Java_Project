package com.market.entity;

import com.market.base.BaseTime;

import javax.persistence.*;

@Entity
@Table(name = "MBM_MARKET_SEND_INFO")
public class Send extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    private String appId;
    private String sendStatus;
    @Column(length = 1000)
    private String errorMsg;
    private String userId;


    // 원래는 안좋은 방법인데 그냥 사용
    public SendHistory of(){
        SendHistory sendHistory = new SendHistory();
        sendHistory.setId(this.id);
        sendHistory.setAppId(this.appId);
        sendHistory.setSendStatus(this.sendStatus);
        sendHistory.setErrorMsg(this.errorMsg);
        sendHistory.setUserId(this.userId);
        sendHistory.setRegDt(this.getRegDt());
        return sendHistory;
    }

    public Send() {
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

    public Send(Long id, String appId, String sendStatus, String errorMsg, String userId) {
        this.id = id;
        this.appId = appId;
        this.sendStatus = sendStatus;
        this.errorMsg = errorMsg;
        this.userId = userId;
    }
}
