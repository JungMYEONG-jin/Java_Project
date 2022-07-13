package com.ecommerce.controller;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoupangController {

    @GetMapping("/coupon")
    public String myCouponForm(Model model, @AuthenticationPrincipal Principal principal){
        if (principal != null){
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        }
        model.addAttribute("myCouponPage", "coupon");
        return "my/coupon";
    }
}
