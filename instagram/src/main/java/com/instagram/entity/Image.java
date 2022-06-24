package com.instagram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

}
