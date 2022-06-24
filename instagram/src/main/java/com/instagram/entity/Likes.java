package com.instagram.entity;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;

@Entity
public class Likes extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "likeID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="imageID")
    private Image image;
}
