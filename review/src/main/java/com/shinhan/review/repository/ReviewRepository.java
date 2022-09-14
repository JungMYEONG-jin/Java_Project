package com.shinhan.review.repository;

import com.shinhan.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAppPkg(String appPkg);
    List<Review> findByOsType(String osType);
}

