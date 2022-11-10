package com.shinhan.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shinhan.review.crawler.OS;
import com.shinhan.review.entity.dto.QReviewDto;
import com.shinhan.review.entity.dto.QReviewExcelDto;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.entity.dto.ReviewExcelDto;
import com.shinhan.review.search.form.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.shinhan.review.entity.QReview.review;


@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ReviewDto> findAllBySearchForm(SearchForm searchForm, Pageable pageable) {
        BooleanBuilder builder = getBooleanBuilder(searchForm);

        List<ReviewDto> result = jpaQueryFactory.select(new QReviewDto(review)).from(review).where(builder).offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();
        // list to page
        // need Count
        Long count = jpaQueryFactory.select(review.count()).from(review).where(builder).fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    /**
     * 조건문
     * @param searchForm
     * @return
     */
    private BooleanBuilder getBooleanBuilder(SearchForm searchForm) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(searchForm.getAppPkg())) {
            builder.and(review.appPkg.eq(searchForm.getAppPkg()));
        }
        if (searchForm.getOs()!=null) {
            builder.and(review.osType.eq(searchForm.getOs().getNumber()));
        }
        if (searchForm.getStart()!=null){
            builder.and(review.createdDate.goe(searchForm.getStrStart()));
        }
        if (searchForm.getEnd()!=null){
            builder.and(review.createdDate.lt(searchForm.getStrEnd()));
        }
        return builder;
    }

    // 따로 분리
    private BooleanExpression eqAppPkg(String appPkg){
        if (StringUtils.hasText(appPkg))
            return review.appPkg.eq(appPkg);
        return null;
    }

    private BooleanExpression eqOS(OS os){
        if (os!=null)
            return review.osType.eq(os.getNumber());
        return null;
    }

    private BooleanExpression lessThanDate(String createdDate){
        if (createdDate!=null)
            return review.createdDate.lt(createdDate);
        return null;
    }

    private BooleanExpression greatOrEqualThanDate(String createdDate){
        if (createdDate!=null)
            return review.createdDate.goe(createdDate);
        return null;
    }


    @Override
    public List<ReviewExcelDto> getExcelBySearchForm(SearchForm searchForm) {
        String endDate = searchForm.getStrEnd();
        String startDate = searchForm.getStrStart();
        String appPkg = searchForm.getAppPkg();
        OS os = searchForm.getOs();
        return jpaQueryFactory.select(new QReviewExcelDto(review)).
                from(review).
                where(eqAppPkg(appPkg),
                greatOrEqualThanDate(startDate),
                lessThanDate(endDate),
                eqOS(os)).
                fetch();
    }
}
