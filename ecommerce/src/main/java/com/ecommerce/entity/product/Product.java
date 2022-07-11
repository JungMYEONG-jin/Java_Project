package com.ecommerce.entity.product;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.user.Place;
import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "compnay_id")
    private Company company;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Place place;



}
