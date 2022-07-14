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
    private String errorMsg;
    private String userId;
}
