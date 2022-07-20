package com.ecommerce.controller.user;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final UserService userService;

    @GetMapping
    public String membershipForm(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("user", UserResDto.of(principal.getUser()));
        return "my/membership";
    }

    @PostMapping
    public String membershipToggle(@AuthenticationPrincipal Principal principal, Model model){
        UserResDto userResDto = userService.changeMembership(principal);
        model.addAttribute("user", userResDto);
        return "my/membership";
    }
}
