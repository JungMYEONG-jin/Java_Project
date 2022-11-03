package com.shinhan.review.web.service;

import com.shinhan.review.crawler.AppList;
import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.crawler.OS;
import com.shinhan.review.crawler.apple.AppleAppId;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.entity.dto.ReviewExcelDto;
import com.shinhan.review.repository.ReviewRepository;
import com.shinhan.review.search.form.SearchForm;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    ConcreteCrawler crawler;
    @Autowired
    ReviewRepository reviewRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Scheduled(cron = "0 30 0 * * *") // 0시 30분 크롤링 시작
    public void getAllReviews(){
        logger.info("정기 크롤링이 시작됩니다.");
        Arrays.stream(AppList.values()).forEach(app -> {
            saveReviews(app.getAppPkg(), OS.AND.getNumber()); // for and
            saveReviews(app.getAppPkg(), OS.IOS.getNumber()); // for iOS
        });
        logger.info("정기 크롤링이 완료되었습니다.");
    }

    @Transactional
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

    // for excel rendering
    @Transactional
    public List<ReviewDto> searchByCondition(SearchForm form){
        return getReviewsForExcel().stream().filter(review -> {
            if (form.getOs() == null)
                return true;
            return review.getOsType().equals(form.getOs().getNumber());
        }).filter(review -> {
            if (isEmpty(form.getAppPkg()))
                return true;
            return review.getAppPkg().equals(form.getAppPkg());
        }).filter(review -> {
            String createdDate = review.getCreatedDate(); // yyyyMMddHHmmss
            createdDate = createdDate.substring(0,4)+"-"+createdDate.substring(4,6)+"-"+createdDate.substring(6,8);
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getStart() == null)
                return true;
            else
                return (localDate.isAfter(form.getStart()) || localDate.isEqual(form.getStart()));
        }).filter(review->{
            String createdDate = review.getCreatedDate();
            createdDate = createdDate.substring(0,4)+"-"+createdDate.substring(4,6)+"-"+createdDate.substring(6,8);
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getEnd()==null)
                return true;
            return localDate.isBefore(form.getEnd());
        }).collect(Collectors.toList());
    }

    public List<ReviewDto> pageToList(Page<Review> reviews){
        return reviews.getContent().stream().map(review -> new ReviewDto(review.getAppVersion(), review.getCreatedDate(), review.getNickname(), review.getRating(), review.getBody(), review.getResponseBody(), review.getAnsweredDate(), review.getDevice(), review.getAppPkg(), review.getOsType())).collect(Collectors.toList());
    }

    public List<ReviewDto> listToDtoList(List<Review> reviews){
        return reviews.stream().map(review -> new ReviewDto(review.getAppVersion(), review.getCreatedDate(), review.getNickname(), review.getRating(), review.getBody(), review.getResponseBody(), review.getAnsweredDate(), review.getDevice(), review.getAppPkg(), review.getOsType())).collect(Collectors.toList());
    }

    public List<ReviewExcelDto> listToReviewExcel(List<Review> reviews){
       return reviews.stream().map(review -> new ReviewExcelDto(review)).collect(Collectors.toList());
    }

    /**
     * excel dto -> excel file
     * no queryDSL
     * @param form
     * @return
     */
    @Transactional
    public List<ReviewExcelDto> getExcelByCondition(SearchForm form){
        List<ReviewExcelDto> reviewExcelDtos = listToReviewExcel(findAll());
        List<ReviewExcelDto> result = reviewExcelDtos.stream().filter(review -> {
            if (form.getOs() == null)
                return true;
            return review.getOsType().equals(form.getOs().getNumber());
        }).filter(review -> {
            if (isEmpty(form.getAppPkg()))
                return true;
            return review.getAppPkg().equals(form.getAppPkg());
        }).filter(review -> {
            String createdDate = review.getCreatedDate(); // yyyy-MM-dd
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getStart() == null)
                return true;
            else
                return (localDate.isAfter(form.getStart()) || localDate.isEqual(form.getStart()));
        }).filter(review -> {
            String createdDate = review.getCreatedDate(); // yyyyMMddHHmmss
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getEnd() == null)
                return true;
            return localDate.isBefore(form.getEnd());
        }).collect(Collectors.toList());
        // for 순번...
        Long cnt = 1L;
        for (ReviewExcelDto reviewExcelDto : result) {
            reviewExcelDto.setId(cnt++);
            reviewExcelDto.upateAppPkgToExcelTemplate(); // excel 에 한글로 이름 보여주려고...
        }
        return result;
    }

    /**
     * excel by queryDSL
     * @param form
     * @return
     */
    @Transactional
    public List<ReviewExcelDto> getExcelByQueryDSL(SearchForm form){
        List<ReviewExcelDto> result = reviewRepository.getExcelBySearchForm(form);
        Long cnt = 1L;
        for (ReviewExcelDto reviewExcelDto : result) {
            reviewExcelDto.setId(cnt++);
            reviewExcelDto.upateAppPkgToExcelTemplate(); // excel 에 한글로 이름 보여주려고...
        }
        return result;
    }

    /**
     * 여기에 순번 추가 하면 될듯
     * @return
     */
    @Transactional
    public List<ReviewDto> getReviewsForExcel(){
        List<Review> all = reviewRepository.findAll();
        return all.stream().map(review -> new ReviewDto(review.getAppVersion(), review.getCreatedDate(), review.getNickname(), review.getRating(), review.getBody(), review.getResponseBody(), review.getAnsweredDate(), review.getDevice(), review.getAppPkg(), review.getOsType())).collect(Collectors.toList());
    }

    public String getMatchedName(String number){
        if (number.equals("1"))
            return OS.AND.name();
        else if (number.equals("2"))
            return OS.IOS.name();
        return "존재하지 않는 앱";
    }


    private String getPackageName(String packageName, String osType) {
        String pack = "";
        if (osType.equals("1")){
            GoogleAppId id = Arrays.stream(GoogleAppId.values()).filter(app -> app.name().equals(packageName)).findAny().orElse(null);
            pack = id.getAppPkg();
        }else if(osType.equals("2")){
            AppleAppId id = Arrays.stream(AppleAppId.values()).filter(app -> app.name().equals(packageName)).findAny().orElse(null);
            pack = id.getAppPkg();
        }
        return pack;
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    @Transactional
    public Page<ReviewDto> findAll(Pageable pageable){
        Page<ReviewDto> all = reviewRepository.findAll(pageable).map(review -> new ReviewDto(review));
        return all;
    }

    @Transactional
    public List<Review> findByAppPkg(String appPkg){
        return reviewRepository.findByAppPkg(appPkg);
    }

    @Transactional
    public List<Review> findByType(String osType){
        return reviewRepository.findByOsType(osType);
    }
    
    private boolean isEmpty(String text){
        if(text==null || text.isEmpty())
            return true;
        return false;
    }
    // for page rendering

    /**
     * queryDSL 되는 환경
     * @param searchForm
     * @param pageable
     * @return
     */
    @Transactional
    public Page<ReviewDto> searchByConditionQueryDSL(SearchForm searchForm, Pageable pageable){
        if (searchForm.getStart()!=null)
            searchForm.setStrStart(getFormattedDate(searchForm.getStart().toString())); // queryDSL 날짜비교 위해
        if (searchForm.getEnd()!=null)
            searchForm.setStrEnd(getFormattedDate(searchForm.getEnd().toString()));
        return reviewRepository.findAllBySearchForm(searchForm, pageable);
    }

    /**
     * queryDSL 안될때
     * @param pageable
     * @param form
     * @return
     */
    @Transactional
    public Page<ReviewDto> searchByCondition(Pageable pageable, SearchForm form){
        if (form.getStart()==null && form.getEnd()==null)
        {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return findAll(pageable);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByOsTypeAndAppPkg(pageable, form);
        }

        // 시작일만 지정한경우
        if(form.getStart()!=null && form.getEnd()==null){
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfter(pageable, form);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndOSType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndOsTypeAndAppPkg(pageable, form);
        }

        // 종료일만 지정한 경우
        if(form.getStart()==null && form.getEnd()!=null){
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateBefore(pageable, form);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndOSType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndOsTypeAndAppPkg(pageable, form);
        }

        // 시작, 종료 지정 했을때
        if (form.getStart()!=null && form.getEnd()!=null) {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByDate(pageable, form);
            else if(form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByDateAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByDateAndOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByDateAndOsTypeAndAppPkg(pageable, form);
        }

        throw new IllegalStateException("검색 조건이 잘못되었습니다...");
    }

    @Transactional
    public Page<ReviewDto> searchByOsType(Pageable pageable, SearchForm form){
        return reviewRepository.findByOsType(pageable, form.getOs().getNumber()).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByDate(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDate(pageable, start, end).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByDateAndOsType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAndOsType(pageable, start, end, form.getOs().getNumber()).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByCreatedDateAfter(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfter(pageable, start).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByCreatedDateBefore(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBefore(pageable, end).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByCreatedDateAfterAndOSType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndOsType(pageable, start, form.getOs().getNumber()).map(review -> new ReviewDto(review));
    }
    @Transactional
    public Page<ReviewDto> searchByCreatedDateBeforeAndOSType(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndOsType(pageable, end, form.getOs().getNumber()).map(review -> new ReviewDto(review));
    }

    // app
    @Transactional
    public Page<ReviewDto> searchByAppPkg(Pageable pageable, SearchForm form){
        return reviewRepository.findByAppPkg(pageable, form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // os app
    @Transactional
    public Page<ReviewDto> searchByOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        return reviewRepository.findByOsTypeAndAppPkg(pageable, form.getOs().getNumber(), form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // start app
    @Transactional
    public Page<ReviewDto> searchByCreatedDateBeforeAndAppPkg(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndAppPkg(pageable, end, form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // end app
    @Transactional
    public Page<ReviewDto> searchByCreatedDateAfterAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndAppPkg(pageable, start, form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // start end app
    @Transactional
    public Page<ReviewDto> searchByDateAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAndAppPkg(pageable, start, end, form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // start os app
    @Transactional
    public Page<ReviewDto> searchByCreatedDateAfterAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndOsTypeAndAppPkg(pageable, start, form.getOs().getNumber(), form.getAppPkg()).map(review -> new ReviewDto(review));
    }
    // end os app
    @Transactional
    public Page<ReviewDto> searchByCreatedDateBeforeAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndOsTypeAndAppPkg(pageable, end, form.getOs().getNumber(), form.getAppPkg()).map(review -> new ReviewDto(review));
    }

    // start end os app
    @Transactional
    public Page<ReviewDto> searchByDateAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAAndOsTypeAndAppPkg(pageable, start, end, form.getOs().getNumber(), form.getAppPkg()).map(review -> new ReviewDto(review));
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
