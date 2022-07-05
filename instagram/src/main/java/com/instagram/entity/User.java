package com.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private Long id;
    @Column
    private String bio;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String gender;
    @Column(nullable = false)
    private String password; // 암호화해서 저장하자 sha256
    @Column(length = 20)
    private String phone;
    @Column
    private String role;
    @Column(length = 100)// nickname 중복은 불가함..
    private String username; // nickname
    @Column
    private String website;
    @Column(nullable = false)
    private String name; //사람 이름

    private String profileImageUrl;


    @OneToMany(mappedBy = "user") // user can have many likes
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();



}
