package com.market.entity;

import com.market.base.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MBM_MARKET_SEND_HISTORY")
public class SendHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    private String appId;
    private String sendStatus;
    private String errorMsg;
    private String userId;
    private String regDt;
    private String etc1;
    private String etc2;
    private String etc3;
}
