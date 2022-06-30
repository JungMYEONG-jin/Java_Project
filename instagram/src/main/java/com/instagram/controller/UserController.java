package com.instagram.controller;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.user.UserProfileDto;
import com.instagram.dto.user.UserUpdateDto;
import com.instagram.entity.User;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{pageUesrID}")
    public String profile(@PathVariable int pageUserID, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User pageUser = userService.findOne((long) pageUserID);
        UserProfileDto dto = userService.userProfile(pageUser, principalDetails.getUser());
        model.addAttribute("dto", dto);

        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        log.info("user = {}", user);

        return "user/update";
    }
}
