package com.ecommerce.entity.order.dto;

import com.ecommerce.entity.order.OrderDetails;
import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Set<OrderDetailsDto> orderDetailsDtos = new HashSet<>();
    private int totalPrice;
    private String buyerPhone;
    private Place place;
    private MemberAddress address;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
