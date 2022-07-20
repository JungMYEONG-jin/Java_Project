package com.market.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
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


}
