package com.ecommerce.entity.order;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.product.Product;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderDetails extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_details_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private boolean visible;
    private boolean arrive;
    private int price;
    private int count;
    private LocalDateTime arrivedDate;
}
