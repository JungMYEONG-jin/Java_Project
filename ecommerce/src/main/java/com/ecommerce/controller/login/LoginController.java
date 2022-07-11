package com.ecommerce.controller.login;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserReqDto;
import com.ecommerce.entity.user.dto.UserResDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception, Model model){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }


    @PostMapping("/confirmPassword")
    public ResponseEntity confirmPassword(@AuthenticationPrincipal Principal principal, @RequestBody UserReqDto dto, ModelMapper modelMapper){
        if(new BCryptPasswordEncoder().matches(dto.getPassword(), principal.getPassword())){
            return ResponseEntity.status(200).body(modelMapper.map(principal.getUser(), UserResDto.class));
        }
        return ResponseEntity.badRequest().body("로그인중 에러가 발생하였습니다.....");
    }
}
