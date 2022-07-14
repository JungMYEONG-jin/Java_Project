package com.ecommerce.entity.order.dto;

import com.ecommerce.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {

    private Long id;
    private Product product;
    private LocalDateTime arrivedDate;
    private boolean show;
    private boolean arrive;
    private int count;
    private int price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
