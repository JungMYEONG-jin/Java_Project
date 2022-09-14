package com.shinhan.review.web.controller;

import com.shinhan.review.web.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/review/{appPkg}")
    public String getReviewByAppPkg(@PathVariable String appPkg){
        return "appPkg";
    }

    @GetMapping("/")
    public String goHome(){
        logger.info("home controller");
        return "home";
    }



}
