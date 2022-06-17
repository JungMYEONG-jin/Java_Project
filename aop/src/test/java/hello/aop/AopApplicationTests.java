package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.pool.TypePool;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AopApplicationTests {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;


	@Test
	void aopInfo() {
		log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
		log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));

	}

	@Test
	void success() {
		orderService.orderItem("itemA");
	}

	@Test
	void exception() {
		Assertions.assertThatThrownBy(() -> orderService.orderItem("ex")).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void contextLoads() {
	}

}
