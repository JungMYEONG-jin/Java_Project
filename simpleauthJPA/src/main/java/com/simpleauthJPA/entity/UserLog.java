package com.simpleauthJPA.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;

@Entity
@Table(name = "mbi_simpleauth_log")
@Getter
@Setter
public class UserLog {
    @Id
    @GeneratedValue
    private Long seq;

    @Column
    private String cusno;
    @Column
    private String id;
    @Column
    private String tag;
    @Column
    private String injson;
    @Column
    private String outjson;
    @Column
    private String msg;
    @Column
    private String code;
    @Column
    private String stacktrace;
    @Column
    private String timestamp;

    public UserLog() {
    }

    public UserLog(String cusno, String id, String tag, String injson, String outjson, String msg, String code, String stacktrace, String timestamp) {
        this.cusno = cusno;
        this.id = id;
        this.tag = tag;
        this.injson = injson;
        this.outjson = outjson;
        this.msg = msg;
        this.code = code;
        this.stacktrace = stacktrace;
        this.timestamp = timestamp;
    }
}
