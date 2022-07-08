package com.ecommerce.entity.coupon;

import com.ecommerce.base.BaseTimeEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Coupon {

    private Long id;
    private String name;
    private boolean condition;
    private double saleRate;
    private LocalDateTime expiredDate;


}
