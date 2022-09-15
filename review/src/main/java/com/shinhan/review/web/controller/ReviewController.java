package com.shinhan.review.web.controller;

import com.shinhan.review.entity.Review;
import com.shinhan.review.search.form.DateSearch;
import com.shinhan.review.web.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DateSearch form = new DateSearch(LocalDate.now(), LocalDate.now());

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

//    @GetMapping("/reviews")
//    public String getReviewList(Model model){
//        List<Review> reviews = reviewService.findAll();
//        model.addAttribute("reviews", reviews);
//        return "review/reviewList";
//    }

    @GetMapping("/reviews")
    public String getReviewList(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Review> reviews = reviewService.findAll(pageable);
        model.addAttribute("reviews", reviews);
        return "review/reviewList";
    }

    @GetMapping("/reviews/search")
    public String searchReviewListGet(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Review> reviews = reviewService.searchByDate(pageable, form); //처음만 init 하면
        model.addAttribute("dateSearch", form);
        model.addAttribute("reviews", reviews);
        return "review/searchPage";
    }

    @PostMapping("/reviews/search")
    public String searchReviewListPost(Model model, @ModelAttribute("dateSearch") DateSearch dateSearch, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        this.form = dateSearch;
        Page<Review> reviews = reviewService.searchByDate(pageable, form);
        model.addAttribute("reviews",reviews);
        return "review/searchPage";
    }

}
