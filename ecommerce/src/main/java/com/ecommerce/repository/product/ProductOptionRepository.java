package com.ecommerce.repository.product;

import com.ecommerce.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
