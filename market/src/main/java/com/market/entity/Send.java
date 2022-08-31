package com.market.entity;

import com.market.base.BaseTime;
import com.market.base.BaseTimeSend;

import javax.persistence.*;

@Entity
@Table(name = "MBM_MARKET_SEND_INFO")
public class Send extends BaseTimeSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    @Column(name = "APP_ID", length = 256, nullable = false)
    private String appId;
    @Column(name = "SEND_STATUS", length = 20)
    private String sendStatus;
    @Column(name = "ERROR_MSG", length = 1000)
    private String errorMsg;
    @Column(name = "REQ_USER_ID", length = 20)
    private String userId;

    @Column(name = "ETC1", length = 4000)
    private String etc1;
    @Column(name = "ETC2", length = 4000)
    private String etc2;
    @Column(name = "ETC3", length = 4000)
    private String etc3;


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

    public Send(Long id, String appId, String sendStatus, String errorMsg, String userId, String etc1, String etc2, String etc3) {
        this.id = id;
        this.appId = appId;
        this.sendStatus = sendStatus;
        this.errorMsg = errorMsg;
        this.userId = userId;
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
