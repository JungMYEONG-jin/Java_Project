package com.ecommerce.repository.product;

import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.product.dto.ProductDetailDto;
import com.ecommerce.entity.review.Review;
import com.ecommerce.entity.user.User;
import com.ecommerce.repository.review.ReviewRepository;
import com.ecommerce.repository.user.UserRepository;
import com.ecommerce.service.product.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;

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

    @Test
    void productServiceTest() {

        User user = User.builder().username("kim").email("abc123@gmail.com").password("aa123").rocketMembership(true).phoneNumber("01203213").build();
        userRepository.save(user);

        User user2 = User.builder().username("jj").email("1211@gmail.com").password("adsa122").rocketMembership(true).phoneNumber("32132132").build();
        userRepository.save(user2);

        Product product = new Product();
        product.setDetailsPageUrl("abc");
        product.setTitle("kong");
        productRepository.save(product);

        Product product2 = new Product();
        product2.setDetailsPageUrl("abc");
        product2.setTitle("hosi");
        product2.setRocketShipping(true);
        productRepository.save(product2);

        Review review = new Review();
        review.setProduct(product);
        review.setMessage("정말 맛있습니다.");
        review.setStar(4);
        review.setUser(user);

        Review review2 = new Review();
        review2.setProduct(product);
        review2.setMessage("그럭저럭 먹을만해요.");
        review2.setStar(3);
        review2.setUser(user);

        reviewRepository.save(review);
        reviewRepository.save(review2);

        ProductDetailDto findProduct = productService.getProduct(1L);
        System.out.println("findProduct = " + findProduct);


    }
}