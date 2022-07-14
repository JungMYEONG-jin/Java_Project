package com.ecommerce.entity.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long id;

    @NotNull(message = "장바구니에 담을 상품 코드는 필수입니다.")
    private Long productId;

    @Min(value=1, message = "최소 1개 이상 물품이 담겨야 합니다.") // 장바구니에 담으려면 1개 이상 선택되어야함.
    private int count;

    private Long optionId;
    private String size;
    private String color;
}
