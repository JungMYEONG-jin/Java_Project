package com.ecommerce.entity.product;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.user.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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
