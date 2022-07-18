package com.market.entity;

import com.market.base.BaseTime;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
