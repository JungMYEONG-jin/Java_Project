package com.shinhan.review.repository;

import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.entity.dto.ReviewExcelDto;
import com.shinhan.review.search.form.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewCustomRepository {
    Page<ReviewDto> findAllBySearchForm(SearchForm searchForm, Pageable pageable);
    List<ReviewExcelDto> getExcelBySearchForm(SearchForm searchForm);
}
