package com.ecommerce.entity.order;

import com.ecommerce.base.BaseTimeEntity;
import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.Place;
import com.ecommerce.entity.user.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private MemberAddress address;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Place place;

    @OneToMany(mappedBy = "order")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<OrderDetails> orderDetailsList = new HashSet<>();

    @Formula("(select sum(od.price) from order_details od where od.order_id = order_id")
    private int totalPrice;

    public void addDetails(OrderDetails orderDetails){
        this.orderDetailsList.add(orderDetails);
    }

}
