package com.ecommerce.service.product;

import com.ecommerce.entity.product.Category;
import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.product.dto.ProductDetailDto;
import com.ecommerce.entity.product.dto.ProductDto;
import com.ecommerce.repository.product.CategoryRepository;
import com.ecommerce.repository.product.ProductRepository;
import com.ecommerce.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ReviewRepository reviewRepository;


    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProductPage(Pageable pageable){
        return productRepository.getProductPage(pageable).map(ProductDto::of);
    }

    @Transactional(readOnly = true)
    public ProductDetailDto getProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("해당 ID 상품이 존재하지 않습니다."));
        ProductDetailDto productDetailDto = ProductDetailDto.of(product);
        int reviewCnt = reviewRepository.productReviewCount(productId);
        int starRate = reviewRepository.productStarRate(productId);
        productDetailDto.setReviewNum(reviewCnt);
        productDetailDto.setStarRate(starRate);
        return productDetailDto;
    }

    @Transactional(readOnly = true)
    public List<Product> getCompanyProductLimit4(Long productId, Long companyId){
        return productRepository.findFirst4ByCompanyIdAndIdNot(companyId, productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getCategoryProductPage(Long categoryId, Pageable pageable){
        List<Category> childList = categoryService.getChildCategory(categoryId);
        return productRepository.getProductPageWithCategory(childList, pageable).map(ProductDto::of);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllCompanyProductPage(Long companyId, Pageable pageable){
        return productRepository.getProductPageWithCompanyId(companyId, pageable).map(ProductDto::of);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProductPageBySearch(String keyword, boolean rocket, Pageable pageable){
        if(rocket){
            return productRepository.getProductPageBySearchWithRocket(keyword, pageable).map(ProductDto::of);
        }
        return productRepository.getProductPageBySearch(keyword, pageable).map(ProductDto::of);
    }



}
