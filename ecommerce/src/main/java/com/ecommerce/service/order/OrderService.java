package com.ecommerce.service.order;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.order.Cart;
import com.ecommerce.entity.order.Order;
import com.ecommerce.entity.order.OrderDetails;
import com.ecommerce.entity.order.dto.OrderReqDto;
import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.User;
import com.ecommerce.repository.order.CartRepository;
import com.ecommerce.repository.order.OrderDetailsRepository;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.repository.product.ProductRepository;
import com.ecommerce.repository.user.MemberAddressRepository;
import com.ecommerce.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final ProductRepository productRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CartRepository cartRepository;

    public void createOrderWithOneProduct(User user, OrderReqDto orderReqDto, boolean oneItem){
        log.info("user id {}", user.getId());
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        MemberAddress address = memberAddressRepository.findById(orderReqDto.getAddressId()).orElseThrow(() -> new RuntimeException("존재하지 않는 주소입니다."));
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhone(orderReqDto.getPhone());
        orderReqDto.setPlace(address.getPlace());
        if (orderReqDto.getPlace() != null)
            order.setPlace(orderReqDto.getPlace());
        if(oneItem){
            Product product = productRepository.findById(orderReqDto.getProductId()).orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
            OrderDetails orderDetails = createOrderDetails(user.isRocketMembership(), product, orderReqDto.getCount());
            order.addDetails(orderDetails);
            orderDetails.setOrder(order);
            orderRepository.save(order);
        }else{
            List<Cart> carts = cartRepository.findByUserIdAndSelected(user.getId(), true);
            carts.forEach(cart -> {
                OrderDetails orderDetails = createOrderDetails(findUser.isRocketMembership(), cart.getProduct(), cart.getCount());
                order.addDetails(orderDetails);
                orderDetails.setOrder(order);
            });
            orderRepository.save(order);
            cartRepository.deleteAll(carts); // 장바구니목록 제거
        }


    }

    public OrderDetails createOrderDetails(boolean rocketMembership, Product product, int count) {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setVisible(true);
        orderDetails.setCount(count);
        orderDetails.setProduct(product);
        orderDetails.setArrivedDate(LocalDateTime.now().plusDays(2).withHour(14));
        int price = (int)(product.getPrice() * 0.95);
        int shippingPrice = 0;

        if(rocketMembership && product.isGoldBox())
            price = (int)(price * 0.9);

        if(product.isRocketShipping())
            orderDetails.setArrivedDate(LocalDateTime.now().plusDays(1).withHour(5));
        if((product.isRocketShipping() && !rocketMembership && price < 10000) || !product.isRocketShipping())
            shippingPrice = 3000;

        orderDetails.setPrice(shippingPrice + price);
        return orderDetails;
    }


}
