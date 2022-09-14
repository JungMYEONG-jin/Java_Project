package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.entity.Review;
import com.shinhan.review.repository.ReviewRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ConcreteCrawler crawler;
    @Autowired
    ReviewRepository reviewRepository;

    public void saveAll(List<Review> reviews){
        reviewRepository.saveAll(reviews);
    }

    public void saveOne(Review review){
        reviewRepository.save(review);
    }

    public Review findOne(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findByAppPkg(String appPkg){
        return reviewRepository.findByAppPkg(appPkg);
    }

    public List<Review> findByType(String osType){
        return reviewRepository.findByOsType(osType);
    }

    public void clear(){
        reviewRepository.deleteAll();
    }

}
