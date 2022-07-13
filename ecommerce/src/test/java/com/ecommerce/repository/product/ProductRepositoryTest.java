package com.ecommerce.repository.product;

import com.ecommerce.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void countAllTest() {

        Product product = new Product();
        product.setDetailsPageUrl("abc");
        product.setTitle("kong");
        productRepository.save(product);

        Product product2 = new Product();
        product2.setDetailsPageUrl("abc");
        product2.setTitle("kongalike");
        productRepository.save(product2);

        int res = productRepository.countAll();
        Assertions.assertThat(res).isEqualTo(2);

    }

    @Test
    void filterTest(){
        Product product = new Product();
        product.setDetailsPageUrl("abc");
        product.setTitle("kong");
        productRepository.save(product);

        Product product2 = new Product();
        product2.setDetailsPageUrl("abc");
        product2.setTitle("hosi");
        productRepository.save(product2);

        int filterResult = productRepository.countAllByFilter("kong");
        Assertions.assertThat(filterResult).isEqualTo(1);
    }

    @Test
    void filterAndRocketTest() {
        Product product = new Product();
        product.setDetailsPageUrl("abc");
        product.setTitle("kong");
        productRepository.save(product);

        Product product2 = new Product();
        product2.setDetailsPageUrl("abc");
        product2.setTitle("hosi");
        product2.setRocketShipping(true);
        productRepository.save(product2);

        int filterResult = productRepository.countAllByFilterWithRocket("hosi");
        Assertions.assertThat(filterResult).isEqualTo(1);
    }
}