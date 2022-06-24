package com.instagram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Subscribe {

    @Id
    @GeneratedValue
    @Column(name = "subscribe_Id")
    private Long id;

    @Column
    private LocalDateTime createdDate;

}
