package com.shinhan.review.repository;

import com.shinhan.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAppPkg(String appPkg);
    List<Review> findByOsType(String osType);
    Page<Review> findAll(Pageable pageable);
    @Query("select r from Review r where r.createdDate between :start and :end")
    Page<Review> searchByDate(Pageable pageable, @Param("start") String start, @Param("end") String end); // 날짜 기반 검색
    @Query("select r from Review r where r.createdDate between :start and :end and r.osType = :type")
    Page<Review> searchByDateAndOsType(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("type") String type); // 날짜 기반 검색
    @Query("select r from Review r where r.osType = :type")
    Page<Review> searchByOsType(Pageable pageable, @Param("type") String type);

}

