package com.ecommerce.entity.product.dto;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.product.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"optionSet", "clothesOptions", "thumbnailUrls", "type", "company"})
public class ProductDetailDto {
    private Long id;
    private Category type;
    private Company company;
    private Set<ProductOption> optionSet = new HashSet<>();
    private Set<ClothesOption> clothesOptions = new HashSet<>();
    private Set<ProductImage> thumbnailUrls = new HashSet<>();
    private String title;
    private Integer price;
    private boolean goldBox;
    private boolean rocketShipping;
    private String detailsPageUrl;
    private int reviewNum;
    private int starRate; // 평점

    public static ProductDetailDto of(Product product){
        return ModelMapperUtils.getModelMapper().map(product, ProductDetailDto.class);
    }
}
