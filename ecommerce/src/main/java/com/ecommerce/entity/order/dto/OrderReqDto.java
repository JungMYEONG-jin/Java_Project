package com.ecommerce.entity.order.dto;

import com.ecommerce.entity.user.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReqDto {

    private Long productId;
    private Long addressId;
    private int count;
    private String phone;
    private Place place;

}
