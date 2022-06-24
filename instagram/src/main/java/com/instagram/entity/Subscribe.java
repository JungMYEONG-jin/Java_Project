package com.instagram.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Subscribe {

    @Id
    @GeneratedValue
    @Column(name = "subscribeID")
    private Long id;

    @Column
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY) // 게시글 1개는 유저 1명이 작성 가능.
    private User user;

}
