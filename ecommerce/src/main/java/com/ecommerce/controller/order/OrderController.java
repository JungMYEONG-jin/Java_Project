package com.ecommerce.controller.order;

import com.ecommerce.entity.order.dto.OrderReqDto;
import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.order.CartService;
import com.ecommerce.service.order.OrderService;
import com.ecommerce.service.product.ProductService;
import com.ecommerce.service.user.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.error.Mark;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberAddressService memberAddressService;
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/list")
    public String myCouponOrderList(Model model, @AuthenticationPrincipal Principal principal){
        if (principal != null){
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        }
        model.addAttribute("myCouponPage", "orderList");
        return "my/order/orderList";
    }

    @GetMapping("/direct/{productId}")
    public String orderPage(@AuthenticationPrincipal Principal principal, @RequestParam Map<String, String> item, @PathVariable Long productId, Model model){
        model.addAttribute("user", principal.getUser());
        model.addAttribute("address", memberAddressService.getMainAddress(principal.getUser()));
        model.addAttribute("product", productService.getProduct(productId));
        model.addAttribute("count", item.get("count"));
        model.addAttribute("rocketExpect", LocalDateTime.now().plusHours(24).withMinute(0));
        model.addAttribute("generalExpect", LocalDateTime.now().plusDays(2));
        return "order/orderDirect";
    }

    @GetMapping("/cart")
    public String cartOrderPage(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("user", UserResDto.of(principal.getUser()));
        model.addAttribute("address", memberAddressService.getMainAddress(principal.getUser()));
        model.addAttribute("rocketExpect", LocalDateTime.now().plusHours(24).withMinute(0));
        model.addAttribute("generalExpect", LocalDateTime.now().plusDays(2));
        model.addAttribute("products", cartService.getCheckCartProduct(principal.getUser().getId()));
        return "order/orderCart";
    }

    @PostMapping
    @ResponseBody
    public HttpStatus createOrder(@AuthenticationPrincipal Principal principal, @RequestBody OrderReqDto req){
        orderService.createOrderWithOneProduct(principal.getUser(), req, true);
        return HttpStatus.CREATED;
    }

    @PostMapping("/cart")
    @ResponseBody
    public HttpStatus createCartOrder(@AuthenticationPrincipal Principal principal, @RequestBody OrderReqDto req){
        orderService.createOrderWithOneProduct(principal.getUser(), req, false);
        return HttpStatus.CREATED;
    }


}
