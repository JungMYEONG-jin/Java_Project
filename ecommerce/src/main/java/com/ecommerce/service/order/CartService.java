package com.ecommerce.service.order;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.order.Cart;
import com.ecommerce.entity.order.dto.CartDetailDto;
import com.ecommerce.entity.order.dto.CartDto;
import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.product.ProductOption;
import com.ecommerce.entity.user.User;
import com.ecommerce.repository.order.CartRepository;
import com.ecommerce.repository.product.ProductOptionRepository;
import com.ecommerce.repository.product.ProductRepository;
import com.ecommerce.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public Long addCart(CartDto request, Long userId){
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("해당 상품 ID에 대응하는 상품이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElse(null);
        Cart savedCart = cartRepository.findByProductIdAndUserId(product.getId(), userId);

        // 장바구니에 존재
        if (savedCart != null){
            savedCart.addCount(request.getCount());
            return cartRepository.save(savedCart).getId();
        }
        Cart cart = ModelMapperUtils.getModelMapper().map(request, Cart.class);
        cart.setProduct(product);
        cart.setUser(user);
        if (request.getOptionId() != null){
            ProductOption productOption = productOptionRepository.findById(request.getOptionId()).orElseThrow(() -> new RuntimeException("해당 상품 옵션ID에 대응하는 데이터가 없습니다."));
            cart.setProductOption(productOption);
            return cartRepository.save(cart).getId();
        }
        return cartRepository.save(cart).getId();
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartItems(Long userId){
        List<Cart> carts = cartRepository.findByUserId(userId);
        return carts.stream().map(CartDetailDto::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteCart(Long userId, Long cartId){
        cartRepository.deleteByIdAndUserId(cartId, userId);
    }

    public void toggleCartItemSelected(Long productId, Long userId){
        Cart cart = cartRepository.findByProductIdAndUserId(productId, userId);
        cart.setSelected(!cart.isSelected());
        cartRepository.save(cart);
    }

    public void changeCartItemCount(Long productId, int count, Long userId){
        Cart cart = cartRepository.findByProductIdAndUserId(productId, userId);
        cart.setCount(count);
        cartRepository.save(cart);
    }

    public List<CartDetailDto> getCheckCartProduct(Long userId){
        return cartRepository.findByUserIdAndSelected(userId, true).stream().map(CartDetailDto::of).collect(Collectors.toList());
    }


}
