package com.instagram.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name="subscribeUK", columnNames = {"toUserID", "fromUserID"})
})
public class Subscribe extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "subscribeID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY) // 구독받는 사람
    @JoinColumn(name = "toUserID")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fromUserID") // 구독하는 사람
    private User fromUser;

}
