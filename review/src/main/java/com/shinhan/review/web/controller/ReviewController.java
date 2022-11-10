package com.shinhan.review.web.controller;

import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.entity.dto.ReviewExcelDto;
import com.shinhan.review.excel.ver2.ExcelException;
import com.shinhan.review.excel.ver2.excel.ExcelFile;
import com.shinhan.review.excel.ver2.excel.multiplesheet.MultiSheetExcelFile;
import com.shinhan.review.search.form.CrawlingForm;
import com.shinhan.review.search.form.SearchForm;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        form.clear();
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

    @GetMapping("/reviews/search")
    public String searchReviewListGet(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
//        Page<ReviewDto> reviews = reviewService.searchByCondition(pageable, form); //처음만 init 하면
        Page<ReviewDto> reviews = reviewService.searchByConditionQueryDSL(form, pageable);
        model.addAttribute("searchForm", form);
        model.addAttribute("reviews", reviews);
        model.addAttribute("totalCnt", reviews.getTotalElements());
        return "review/searchPage";
    }

    @PostMapping("/reviews/search")
    public String searchReviewListPost(@ModelAttribute("searchForm") SearchForm searchForm, Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        this.form = searchForm;
//        Page<ReviewDto> reviews = reviewService.searchByCondition(pageable, form);
        Page<ReviewDto> reviews = reviewService.searchByConditionQueryDSL(form, pageable);
        model.addAttribute("reviews",reviews);
        model.addAttribute("totalCnt", reviews.getTotalElements());
        return "review/searchPage";
    }



    @GetMapping("/reviews/download")
    public void goDownloadPage(HttpServletResponse response){
//        List<ReviewDto> reviews = reviewService.searchByCondition(form);
        List<ReviewExcelDto> reviews = reviewService.getExcelByQueryDSL(form);
        // 파일명 지정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "review_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString()+".xls" + "\";");
        // 인코딩
        response.setContentType("application/vnd.ms-excel; charset=euc-kr");
        ExcelFile excelFile = new MultiSheetExcelFile<>(reviews, ReviewExcelDto.class);
        try {
            excelFile.write(response.getOutputStream());
        } catch (IOException e) {
            throw new ExcelException(String.format("%s %s 조건 다운로드 처리중 에러가 발생했습니다.", form.getAppPkg(), form.getOs().name()), e);
        }
    }

}
