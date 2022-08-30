package com.simpleauthJPA.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long pk;

    @Column(name = "ID")
    private String id;
    @Column(name = "APP_ID")
    private String appid;
    @Column(name = "CUSNO")
    private String cusno;
    @Column(name = "UUID")
    private String uuid;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "PUBKEY")
    private String pubkey;
    @Column(name = "REG_DTTM")
    private String regdate;
    @Column(name = "DROP_DTTM")
    private String unregdate;
    @Column(name = "LAST_AUTH_DTTM")
    private String lastauthdate;

    public User() {
    }

    public User(String id, String appid, String cusno, String uuid, String type, String status, String pubkey, String regdate, String unregdate, String lastauthdate) {
        this.id = id;
        this.appid = appid;
        this.cusno = cusno;
        this.uuid = uuid;
        this.type = type;
        this.status = status;
        this.pubkey = pubkey;
        this.regdate = regdate;
        this.unregdate = unregdate;
        this.lastauthdate = lastauthdate;
    }

}
