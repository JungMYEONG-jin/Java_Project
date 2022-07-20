package com.ecommerce.controller.user;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.PasswordChangeDto;
import com.ecommerce.entity.user.dto.UserReqDto;
import com.ecommerce.service.user.UserService;
import com.ecommerce.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SignUpValidator signUpValidator;

    @GetMapping("/user/modify")
    public String userModifyForm(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("user", principal.getUser());
        return "my/userModify";
    }

    @PutMapping("/user/email")
    @ResponseBody
    public HttpStatus changeEmail(@AuthenticationPrincipal Principal principal, @RequestBody UserReqDto dto){
        if (userService.checkEmailDuplicate(dto.getEmail()))
            throw new RuntimeException("이메일 중복입니다..");
        userService.changeEmail(principal, dto.getEmail());
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/user/phoneNumber")
    @ResponseBody
    public HttpStatus changePhoneNumber(@AuthenticationPrincipal Principal principal, @RequestBody UserReqDto dto){
        userService.changePhoneNumber(principal, dto.getPhoneNumber());
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/user/username")
    @ResponseBody
    public HttpStatus changeUsername(@AuthenticationPrincipal Principal principal, @RequestBody UserReqDto dto){
        userService.changeUsername(principal, dto.getUsername());
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/user/password")
    @ResponseBody
    public HttpStatus changePassword(@AuthenticationPrincipal Principal principal, @RequestBody PasswordChangeDto dto){
        if(!new BCryptPasswordEncoder().matches(principal.getPassword(), dto.getOriginalPassword()))
            throw new RuntimeException("비밀번호가 틀립니다..");
        if(dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getNewPasswordCheck()))
            throw new RuntimeException("에러!!!");
        userService.changePassword(principal, dto);
        return HttpStatus.NO_CONTENT;
    }

    @DeleteMapping("/user/me")
    @ResponseBody
    public HttpStatus deleteUser(@AuthenticationPrincipal Principal principal, HttpSession session){
        userService.deleteUser(principal, session);
        return HttpStatus.NO_CONTENT;
    }

}
