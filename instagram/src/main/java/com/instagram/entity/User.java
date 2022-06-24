package com.instagram.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "userID")
    private Long id;
    @Column
    private String bio;
    @Column
    private LocalDateTime createdDate;
    @Column
    private String email;
    @Column
    private String gender;
    @Column
    private String password; // 암호화해서 저장하자 sha256
    @Column(length = 20)
    private String phone;
    @Column
    private String role;
    @Column(length = 100)
    private String username;
    @Column
    private String website;

    @OneToMany // 유저는 여러 게시물을 올릴수 있음
    private List<Subscribe> subs;
}
