package com.shinhan.review.web.controller;

import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.excel.ReviewColumnInfo;
import com.shinhan.review.excel.template.SimpleExcelFile;
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
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        List<ReviewColumnInfo> headerColumns = allColumns.get(0); // get header column
        // set header
        headerColumns.forEach(reviewColumnInfo -> {
            Cell cell = headerRow.createCell(reviewColumnInfo.getCol());
            cell.setCellValue(reviewColumnInfo.getText());
        });


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


    @GetMapping("/api/v2/excel/review")
    public void downloadReviewInfo2(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.ms-excel; charset=euc-kr");
        List<ReviewDto> reviews = reviewService.getReviewsForExcel();
        SimpleExcelFile<ReviewDto> excelFile = new SimpleExcelFile<>(reviews, ReviewDto.class);
        excelFile.write(response.getOutputStream());
    }







}
