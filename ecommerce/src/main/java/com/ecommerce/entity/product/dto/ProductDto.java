package com.ecommerce.entity.product.dto;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private Integer price;
    private boolean rocketShipping;
    private String thumbnailUrl;

    public static ProductDto of(Product product){
        ProductDto productDto = ModelMapperUtils.getModelMapper().map(product, ProductDto.class);
        productDto.setThumbnailUrl(product.getThumbnailUrls().get(0).getThumbnailUrl());
        return productDto;
    }

}
