package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.entity.Review;
import com.shinhan.review.repository.ReviewRepository;
import com.shinhan.review.search.form.DateSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ConcreteCrawler crawler;
    @Autowired
    ReviewRepository reviewRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void saveAll(List<Review> reviews){
        reviewRepository.saveAll(reviews);
    }

    public void saveOne(Review review){
        reviewRepository.save(review);
    }

    public Review findOne(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    public Page<Review> findAll(Pageable pageable){
//        int page =(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber()-1);
//        pageable = PageRequest.of(page, 10);
//        LoggerFactory.getLogger(this.getClass()).info("page {}", page);
        return reviewRepository.findAll(pageable);
    }

    public List<Review> findByAppPkg(String appPkg){
        return reviewRepository.findByAppPkg(appPkg);
    }

    public List<Review> findByType(String osType){
        return reviewRepository.findByOsType(osType);
    }

    public Page<Review> searchByDate(Pageable pageable, DateSearch form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDate(pageable, start, end);
    }

    private String getFormattedDate(String dates) {
        dates = dates.replaceAll("-","");
        dates = dates+"000000";
        return dates;
    }

    public void clear(){
        reviewRepository.deleteAll();
    }

}
