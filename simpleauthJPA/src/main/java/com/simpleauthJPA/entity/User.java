package com.simpleauthJPA.entity;

import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long pk;

    @Column(name = "ID")
    private String Id;
    @Column(name = "APP_ID")
    private String AppId;
    @Column(name = "CUSNO")
    private String Cusno;
    @Column(name = "UUID")
    private String uuid;
    @Column(name = "TYPE")
    private String Type;
    @Column(name = "STATUS")
    private String Status;
    @Column(name = "PUBKEY")
    private String Pubkey;
    @Column(name = "REG_DTTM")
    private LocalDateTime RegDate;
    @Column(name = "DROP_DTTM")
    private LocalDateTime UnregDate;
    @Column(name = "LAST_AUTH_DTTM")
    private LocalDateTime LastAuthDate;

}
