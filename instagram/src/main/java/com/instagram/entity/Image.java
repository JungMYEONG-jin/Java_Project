package com.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonIgnoreProperties("images")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

    @JsonIgnoreProperties("image")
    @OneToMany(mappedBy = "image")
    private List<Likes> likes = new ArrayList<>();

    @Transient // DB에 칼럼 X
    private boolean likeState;

    @Transient // DB 칼럼 매핑 X
    private int likeCnt;

    @JsonIgnoreProperties("image")
    @OneToMany(mappedBy = "image")
    private List<Comment> comments = new ArrayList<>();

}
