package com.instagram.controller;

import com.instagram.dto.signup.SignupDto;
import com.instagram.entity.User;
import com.instagram.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm(){
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm(){
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute SignupDto signupDto, BindingResult bindingResult){

        User user = signupDto.toEntity();
        if(!user.getEmail().contains("@")){
            bindingResult.addError(new FieldError("signupDto", "email", "잘못된 이메일 형식입니다. 다시 입력하세요."));
        }

        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "auth/signup";
        }

        // 성공 로직
        authService.join(user);
        return "auth/signin";
    }
}
