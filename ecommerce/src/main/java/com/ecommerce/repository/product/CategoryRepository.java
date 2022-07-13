package com.ecommerce.repository.product;

import com.ecommerce.entity.product.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = {"parent"}) // loop 빠질 수 있어서 미리 처리
    Optional<Category> findById(Long id);

    @EntityGraph(attributePaths = {"parent"})
    List<Category> findAllByParentId(Long parentId);
}
