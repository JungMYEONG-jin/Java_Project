package com.shinhan.review.util;

import com.shinhan.review.entity.Review;
import com.shinhan.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateUtilTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    void transformDateYearToDay() {
        List<Review> all = reviewRepository.findAll();
        if (!all.isEmpty()){
            Review review = all.get(0);
            String createdDate = review.getCreatedDate();
            System.out.println("createdDate = " + createdDate);
            String transformDateYearToDay = new DateUtil().transformDateYearToDay(createdDate);
            System.out.println("transformDateYearToDay = " + transformDateYearToDay);
        }
    }

    @Test
    void stringToLocalDateTest() {
        List<Review> all = reviewRepository.findAll();
        if (!all.isEmpty()){
            Review review = all.get(0);
            String createdDate = review.getCreatedDate();
            System.out.println("createdDate = " + createdDate);
            String transformDateYearToDay = new DateUtil().transformDateYearToDay(createdDate);
            LocalDate localDate = new DateUtil().stringToLocalDate(transformDateYearToDay);
            System.out.println("localDate = " + localDate);
        }
    }
}