package com.ecommerce.repository.product;

import com.ecommerce.entity.product.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void findTest() {

        Category category = new Category();
        category.setName("root");

        Category category2 = new Category();
        category2.setName("child1");
        category2.setParent(category);

        Category category3 = new Category();
        category3.setName("child2");
        category3.setParent(category);

        categoryRepository.save(category);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        List<Category> allByParentId = categoryRepository.findAllByParentId(category.getId());
        for (Category category1 : allByParentId) {
            System.out.println("category1 = " + category1);
        }

        Assertions.assertThat(allByParentId.size()).isEqualTo(2);

    }
}