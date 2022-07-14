package com.ecommerce.repository.order;

import com.ecommerce.entity.order.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 장바구니
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByProductIdAndUserId(Long productId, Long userId);

    @EntityGraph(attributePaths = {"product", "productOption"})
    List<Cart> findByUserId(Long userId);

    //삭제
    void deleteByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"product", "productOption"})
    List<Cart> findByUserIdAndSelected(Long userId, boolean selected);

}
