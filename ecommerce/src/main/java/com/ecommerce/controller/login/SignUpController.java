package com.ecommerce.controller.login;

import com.ecommerce.entity.user.User;
import com.ecommerce.entity.user.dto.UserReqDto;
import com.ecommerce.service.user.UserService;
import com.ecommerce.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    UserService userService;
    @Autowired
    SignUpValidator validator;

    @GetMapping
    public String signUpForm(Model model){
        model.addAttribute("user", new UserReqDto());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("user") @Valid UserReqDto userReqDto, BindingResult bindingResult){
        validator.validate(userReqDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "signup"; // 다시 회원 가입 페이지로
        }
        userService.createUser(userReqDto);
        return "redirect:/login";
    }
}
