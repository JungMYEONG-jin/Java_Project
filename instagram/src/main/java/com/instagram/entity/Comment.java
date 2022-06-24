package com.instagram.entity;

import com.fasterxml.jackson.databind.ser.Serializers;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "commentID")
    private Long id;

    @Column(length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageID")
    private Image image;
}
