package com.shinhan.review.web.controller;

import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.web.service.ReviewService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    ReviewService reviewService;



    @GetMapping("/api/v1/excel/review")
    public void downloadReviewInfo(HttpServletResponse response) throws IOException{

        response.setContentType("application/vnd.ms-excel; charset=euc-kr"); // 한글 깨짐
        // get review list to transfer excel file
        List<ReviewDto> reviewsForExcel = reviewService.getReviewsForExcel();
        // create excel file
        Workbook workbook = new SXSSFWorkbook();
        // create a sheet in excel file
        Sheet sheet = workbook.createSheet();

        // create header
        int rowIdx = 0;
        Row headerRow = sheet.createRow(rowIdx++);
        Cell cell1 = headerRow.createCell(0);
        cell1.setCellValue("앱이름");
        Cell cell2 = headerRow.createCell(1);
        cell2.setCellValue("버전");
        Cell cell3 = headerRow.createCell(2);
        cell3.setCellValue("OS");
        Cell cell4 = headerRow.createCell(3);
        cell4.setCellValue("디바이스");
        Cell cell5 = headerRow.createCell(4);
        cell5.setCellValue("닉네임");
        Cell cell6 = headerRow.createCell(5);
        cell6.setCellValue("작성일");
        Cell cell7 = headerRow.createCell(6);
        cell7.setCellValue("평점");
        Cell cell8 = headerRow.createCell(7);
        cell8.setCellValue("리뷰");
        Cell cell9 = headerRow.createCell(8);
        cell9.setCellValue("답변일");
        Cell cell10 = headerRow.createCell(9);
        cell10.setCellValue("답변");

        for (ReviewDto reviewDto : reviewsForExcel) {
            Row bodyRow = sheet.createRow(rowIdx++);
            Cell bodyCell = bodyRow.createCell(0);
            bodyCell.setCellValue(reviewDto.getAppPkg());
            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(reviewDto.getAppVersion());
            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(reviewService.getMatchedName(reviewDto.getOsType()));
            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(reviewDto.getDevice());
            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(reviewDto.getNickname());
            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(reviewDto.getCreatedDate());
            Cell bodyCell7 = bodyRow.createCell(6);
            bodyCell7.setCellValue(reviewDto.getRating());
            Cell bodyCell8 = bodyRow.createCell(7);
            bodyCell8.setCellValue(reviewDto.getBody());
            Cell bodyCell9 = bodyRow.createCell(8);
            bodyCell9.setCellValue(reviewDto.getAnsweredDate());
            Cell bodyCell10 = bodyRow.createCell(9);
            bodyCell10.setCellValue(reviewDto.getResponseBody());
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
