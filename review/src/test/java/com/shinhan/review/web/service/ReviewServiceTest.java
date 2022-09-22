package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.List;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService service;
    @Autowired
    ConcreteCrawler crawler;

//    @BeforeEach
//    void init(){
//        List<JSONObject> reviewList = crawler.getReviewList(AppleAppId.O2O.getAppPkg(), OS.IOS.getNumber());
//        List<Review> reviews = new ArrayList<>();
//        if (!reviewList.isEmpty()) {
//            for (JSONObject jsonObject : reviewList) {
//                ReviewDto reviewDto = new ReviewDto(jsonObject);
//                reviewDto.setAppPkg(AppleAppId.O2O.name());
//                reviewDto.setOsType(OS.IOS.getNumber());
//                reviews.add(reviewDto.toEntity());
//            }
//        }
////        reviewList = crawler.getReviewList(GoogleAppId.sbank.getAppPkg(), OS.AND.getNumber());
////        if (!reviewList.isEmpty()) {
////            for (JSONObject jsonObject : reviewList) {
////                ReviewDto reviewDto = new ReviewDto(jsonObject);
////                reviewDto.setAppPkg(GoogleAppId.sbank.name());
////                reviewDto.setOsType(OS.AND.getNumber());
////                reviews.add(reviewDto.toEntity());
////            }
////        }
//        service.saveAll(reviews);
//    }

    @Description("android")
    @Test
    void findByAppPkgTest1(){
        List<Review> byAppPkg = service.findByAppPkg(GoogleAppId.sbank.getAppPkg());
        if (byAppPkg!=null)
        {
            for (Review review : byAppPkg) {
                System.out.println("review = " + review);
            }
        }
    }


}