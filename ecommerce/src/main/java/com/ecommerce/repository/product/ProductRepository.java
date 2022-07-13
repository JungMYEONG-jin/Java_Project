package com.ecommerce.repository.product;

import com.ecommerce.entity.product.Category;
import com.ecommerce.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    @Query("select count(p) from Product p")
    int countAll();

    @EntityGraph(attributePaths = {"type"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findById(Long productId);

    // 4개 뽑아옴
    List<Product> findFirst4ByCompanyIdAndIdNot(@Param("company_id") Long companyId, @Param("product_id") Long productId);

    @Query("select count(p) from Product p where p.title like %:keyword%")
    int countAllByFilter(@Param("keyword") String keyword);

    @Query("select count(p) from Product p where p.title like %:keyword% and p.rocketShipping = true")
    int countAllByFilterWithRocket(@Param("keyword") String keyword);

    @Query("select p from Product p join ProductImage  pi on p = pi.product")
    Page<Product> getProductPage(Pageable pageable);

    @Query("select p from Product p join ProductImage pi on p = pi.product where p.type in :category")
    Page<Product> getProductPageWithCategory(@Param("category")List<Category> categories, Pageable pageable);

    @Query("select p from Product p join ProductImage pi on p = pi.product where p.company = :company_id")
    Page<Product> getProductPageWithCompanyId(@Param("company_id") Long companyId, Pageable pageable);

    @Query("select p from Product p join ProductImage pi on p = pi.product where p.title like %:keyword% and p.rocketShipping =  true")
    Page<Product> getProductPageBySearchWithRocket(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Product p join ProductImage  pi on p = pi.product where p.title like %:keyword%")
    Page<Product> getProductPageBySearch(String keyword, Pageable pageable);


}
