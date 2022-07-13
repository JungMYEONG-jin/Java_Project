package com.ecommerce.entity.product;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.user.Place;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compnay_id")
    private Company company;

    @OneToMany(mappedBy = "product")
    private Set<ProductOption> optionSet = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ClothesOption> clothesOptions = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private List<ProductImage> thumbnailUrls = new ArrayList<>();

    String title;
    Integer price;
    boolean goldBox;
    boolean rocketShipping; // rocket ㅂㅐ송  물품인지
    String detailsPageUrl;


}
