package com.ecommerce.entity.order;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.product.ProductOption;
import com.ecommerce.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private ProductOption productOption;

    private String color;
    private String size;
    private boolean selected;
    private int count;

    public void addCount(int num){
        this.count += num;
    }
}
