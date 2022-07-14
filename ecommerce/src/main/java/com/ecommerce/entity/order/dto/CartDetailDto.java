package com.ecommerce.entity.order.dto;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.order.Cart;
import com.ecommerce.entity.product.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDto {

    private Long cartId;
    private Long productId;
    private String productName;
    private int price;
    private int count;
    private String option;
    private boolean rocketShipping;
    private boolean goldBox;
    private boolean selected;
    private LocalDateTime expectedArriveDate;

    public static CartDetailDto of(Cart cart){
        CartDetailDto cartDetailDto = ModelMapperUtils.getModelMapper().map(cart, CartDetailDto.class);
        cartDetailDto.setCartId(cart.getId());
        cartDetailDto.setProductId(cart.getProduct().getId());
        cartDetailDto.setProductName(cart.getProduct().getTitle());
        cartDetailDto.setRocketShipping(cart.getProduct().isRocketShipping());
        cartDetailDto.setPrice(cart.getProduct().getPrice());
        cartDetailDto.setGoldBox(cart.getProduct().isGoldBox());
        ProductOption productOption = cart.getProductOption();

        if(productOption != null){
            cartDetailDto.setOption(productOption.getTitle());
            cartDetailDto.setPrice(productOption.getPrice());
        }else{
            cartDetailDto.setOption(cart.getColor() +" "+cart.getSize());
        }
        if (cartDetailDto.isRocketShipping()){
            cartDetailDto.setExpectedArriveDate(LocalDateTime.now().plusDays(1).withHour(6));
        }else{
            cartDetailDto.setExpectedArriveDate(LocalDateTime.now().plusDays(2));
        }
        return cartDetailDto;
    }


}
