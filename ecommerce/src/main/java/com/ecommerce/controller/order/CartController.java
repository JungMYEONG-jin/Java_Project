package com.ecommerce.controller.order;

import com.ecommerce.entity.order.dto.CartDetailDto;
import com.ecommerce.entity.order.dto.CartDto;
import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.service.order.CartService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String cartForm(@AuthenticationPrincipal Principal principal, Model model){
        List<CartDetailDto> cartItems = cartService.getCartItems(principal.getUser().getId());
        model.addAttribute("user", principal.getUser());
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity addCart(@RequestBody @Valid CartDto request, BindingResult bindingResult, @AuthenticationPrincipal Principal principal){
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("validation fail...");
        }
        return ResponseEntity.ok().body(cartService.addCart(request, principal.getUser().getId()));
    }

    @DeleteMapping("/{cartId}")
    @ResponseBody
    public HttpStatus deleteCart(@PathVariable Long cartId, @AuthenticationPrincipal Principal principal){
        cartService.deleteCart(principal.getUser().getId(), cartId);
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/product/{productId}/selected")
    @ResponseBody
    public HttpStatus changeCartItemSelectedOption(@PathVariable Long productId, @AuthenticationPrincipal Principal principal){
        cartService.toggleCartItemSelected(productId, principal.getUser().getId());
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/product/{productId}/count")
    @ResponseBody
    public HttpStatus changeCartItemSelectedOption(@PathVariable Long productId, @RequestParam int count, @AuthenticationPrincipal Principal principal) {
        if (count < 1)
            return HttpStatus.NO_CONTENT;
        cartService.changeCartItemCount(productId, count, principal.getUser().getId());
        return HttpStatus.NO_CONTENT;
    }

}
