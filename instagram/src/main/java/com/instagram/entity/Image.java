package com.instagram.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Image extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "imageID")
    private Long id;

    @Column
    private String caption;
    @Column
    private String postImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

}
