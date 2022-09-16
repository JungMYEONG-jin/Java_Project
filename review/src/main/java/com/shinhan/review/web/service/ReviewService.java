package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.crawler.OS;
import com.shinhan.review.crawler.apple.AppleAppId;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.repository.ReviewRepository;
import com.shinhan.review.search.form.SearchForm;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void saveReviews(String appName, String osType){
        String packageName = getPackageName(appName, osType);
        List<JSONObject> reviewList = crawler.getReviewList(packageName, osType);
        List<Review> reviews = new ArrayList<>();
        if (!reviewList.isEmpty()) {
            for (JSONObject jsonObject : reviewList) {
                ReviewDto reviewDto = new ReviewDto(jsonObject);
                reviewDto.setAppPkg(appName);
                reviewDto.setOsType(osType);
                reviews.add(reviewDto.toEntity());
            }
        }
        reviewRepository.saveAll(reviews);
        logger.info("{} 크롤링 완료", packageName);
    }

    private String getPackageName(String packageName, String osType) {
        String pack = "";
        if (osType.equals("1")){
            for(GoogleAppId google : GoogleAppId.values()){
                if(google.name().equals(packageName))
                {
                    pack = google.getAppPkg();
                    break;
                }
            }
        }else if(osType.equals("2")){
            for(AppleAppId apple : AppleAppId.values()){
                if(apple.name().equals(packageName))
                {
                    pack = apple.getAppPkg();
                    break;
                }
            }
        }
        return pack;
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
        return reviewRepository.findAll(pageable);
    }

    public List<Review> findByAppPkg(String appPkg){
        return reviewRepository.findByAppPkg(appPkg);
    }

    public List<Review> findByType(String osType){
        return reviewRepository.findByOsType(osType);
    }

    public Page<Review> searchByCondition(Pageable pageable, SearchForm form){
        // 날짜 지정 안했을때
        if (form.getStart()==null && form.getEnd()==null)
        {
            if (form.getOs()==null)
                return findAll(pageable);
            else
                return searchByOsType(pageable, form);
        }

        // 시작일만 지정한경우
        if(form.getStart()!=null && form.getEnd()==null){
            if (form.getOs()==null)
                return searchByCreatedDateAfter(pageable, form);
            else
                return searchByCreatedDateAfterAndOSType(pageable, form);
        }

        // 종료일만 지정한 경우
        if(form.getStart()==null && form.getEnd()!=null){
            if (form.getOs()==null)
                return searchByCreatedDateBefore(pageable, form);
            else
                return searchByCreatedDateBeforeAndOSType(pageable, form);
        }

        // 시작, 종료 지정 했을때
        if (form.getStart()!=null && form.getEnd()!=null) {
            if (form.getOs()==null)
                return searchByDate(pageable, form);
            else
                return searchByDateAndOsType(pageable, form);
        }

        // 작동 안할시 전체 반환
        return findAll(pageable); //최악의 경우 전체 반환
    }

    public Page<Review> searchByOsType(Pageable pageable, SearchForm form){
        return reviewRepository.findByOsType(pageable, form.getOs().getNumber());
    }

    public Page<Review> searchByDate(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDate(pageable, start, end);
    }

    public Page<Review> searchByDateAndOsType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAndOsType(pageable, start, end, form.getOs().getNumber());
    }

    public Page<Review> searchByCreatedDateAfter(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfter(pageable, start);
    }

    public Page<Review> searchByCreatedDateBefore(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBefore(pageable, end);
    }

    public Page<Review> searchByCreatedDateAfterAndOSType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndOsType(pageable, start, form.getOs().getNumber());
    }

    public Page<Review> searchByCreatedDateBeforeAndOSType(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndOsType(pageable, end, form.getOs().getNumber());
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
