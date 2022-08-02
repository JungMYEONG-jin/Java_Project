package com.market.base;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
    @CreatedDate
    private String regDt;

    @LastModifiedDate
    private String uptDt;

    @PrePersist
    public void onPrePersist(){

        this.regDt = getNow();
        this.uptDt = this.regDt;
    }

    private String getNow() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = formatter.format(now);
        return formattedDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.uptDt = getNow();
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getUptDt() {
        return uptDt;
    }

    public void setUptDt(String uptDt) {
        this.uptDt = uptDt;
    }
}
