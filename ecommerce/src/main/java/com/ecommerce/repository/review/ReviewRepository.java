package com.ecommerce.repository.review;

import com.ecommerce.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select count(r) from Review r where r.product.id = :productId")
    int productReviewCount(@Param("productId") Long productId);

    @Query("select AVG(r.star) from Review r where r.product.id = :productId")
    int productStarRate(@Param("productId") Long productId);

}
