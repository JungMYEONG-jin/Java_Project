package com.ecommerce;

import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.review.Review;
import com.ecommerce.entity.user.User;
import com.ecommerce.repository.product.ProductRepository;
import com.ecommerce.repository.review.ReviewRepository;
import com.ecommerce.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EcommerceApplication {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ReviewRepository reviewRepository;



	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}


//	@EventListener(ApplicationReadyEvent.class)
	private void init(){
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
	}
}
