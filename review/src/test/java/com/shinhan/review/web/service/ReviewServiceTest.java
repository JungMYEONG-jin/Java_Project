package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.search.form.SearchForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.time.LocalDate;
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

    @Test
    void searchFormByLambda() {
        SearchForm searchForm = new SearchForm();
        List<ReviewDto> reviewDtos = service.searchByCondition(searchForm);
        List<ReviewDto> reviewsForExcel = service.getReviewsForExcel();
        Assertions.assertThat(reviewDtos.size()).isEqualTo(reviewsForExcel.size());
    }

    @Test
    void byDate() {
        SearchForm searchForm = new SearchForm();
        searchForm.setStart(LocalDate.of(2022,9,1));
        searchForm.setEnd(LocalDate.of(2022,9,11));
        List<ReviewDto> reviewDtos = service.searchByCondition(searchForm);
        Assertions.assertThat(reviewDtos.size()).isEqualTo(39);
    }
}