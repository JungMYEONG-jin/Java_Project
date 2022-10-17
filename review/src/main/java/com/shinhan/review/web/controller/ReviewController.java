package com.shinhan.review.web.controller;

import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.excel.ver2.ExcelException;
import com.shinhan.review.excel.ver2.excel.ExcelFile;
import com.shinhan.review.excel.ver2.excel.multiplesheet.MultiSheetExcelFile;
import com.shinhan.review.search.form.CrawlingForm;
import com.shinhan.review.search.form.DownloadForm;
import com.shinhan.review.search.form.SearchForm;
import com.shinhan.review.web.service.ReviewService;
import com.sun.javafx.font.directwrite.DWFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SearchForm form = new SearchForm();

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

    @GetMapping("/crawling/{packageName}/{osType}")
    public String crawlingReviews(@PathVariable String packageName, @PathVariable String osType){
        logger.info("crawling 시작");
        reviewService.saveReviews(packageName, osType);
        return "redirect:/";
    }

    @GetMapping("/crawling/search")
    public String getCrawlingSearchForm(Model model){
        model.addAttribute("crawlingForm", new CrawlingForm());
        return "review/searchForm";
    }

    @PostMapping("/crawling/search")
    public String postCrawlingSearchForm(@ModelAttribute("crawlingForm") CrawlingForm crawlingForm, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        logger.info("수동 크롤링을 시작합니다...");
        if (result.hasErrors()){
            logger.info("수동 크롤링 실패...옵션을 확인해주세요");
            return "review/searchForm";
        }
        redirectAttributes.addAttribute("app", crawlingForm.getAppId());
        redirectAttributes.addAttribute("osType", crawlingForm.getOs().getNumber());
        reviewService.saveReviews(crawlingForm.getAppId(), crawlingForm.getOs().getNumber());
        return "redirect:/";
    }


//    @GetMapping("/reviews")
//    public String getReviewList(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
//        Page<Review> reviews = reviewService.findAll(pageable);
//        model.addAttribute("reviews", reviews);
//        return "review/reviewList";
//    }

    @GetMapping("/reviews/search")
    public String searchReviewListGet(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Review> reviews = reviewService.searchByCondition(pageable, form); //처음만 init 하면
        logger.info("reviews page size {}", reviews.getTotalElements());
        model.addAttribute("searchForm", form);
        model.addAttribute("reviews", reviews);
        return "review/searchPage";
    }

    @PostMapping("/reviews/search")
    public String searchReviewListPost(Model model, @ModelAttribute("searchForm") SearchForm searchForm, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        this.form = searchForm;
        Page<Review> reviews = reviewService.searchByCondition(pageable, form);
        model.addAttribute("reviews",reviews);
        return "review/searchPage";
    }

    @GetMapping("/reviews/download")
    public void goDownloadPage(HttpServletResponse response){
        List<ReviewDto> reviews = reviewService.searchByCondition(form);
        logger.info("reviews size {} ", reviews.size());
        // 파일명 지정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "review_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString()+".xls" + "\";");
        // 인코딩
        response.setContentType("application/vnd.ms-excel; charset=euc-kr");
        ExcelFile excelFile = new MultiSheetExcelFile<>(reviews, ReviewDto.class);
        try {
            excelFile.write(response.getOutputStream());
        } catch (IOException e) {
            throw new ExcelException(String.format("%s %s 조건 다운로드 처리중 에러가 발생했습니다.", form.getAppPkg(), form.getOs().name()), e);
        }
    }


}
