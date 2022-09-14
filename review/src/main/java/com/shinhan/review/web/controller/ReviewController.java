package com.shinhan.review.web.controller;

import com.shinhan.review.entity.Review;
import com.shinhan.review.web.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/reviews/{appPkg}")
    public String getReviewByAppPkg(Model model, @PathVariable(value = "appPkg") String appPkg){
        List<Review> reviews = reviewService.findByAppPkg(appPkg);
        model.addAttribute("reviews", reviews);
        return "review/reviewByPkg";
    }

    @GetMapping("/")
    public String goHome(){
        logger.info("home controller");
        return "home";
    }

    @GetMapping("/reviews")
    public String getReviewList(Model model){
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "review/reviewList";
    }



}
