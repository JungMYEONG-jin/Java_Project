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
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{pageUserID}")
    public String profile(@PathVariable int pageUserID, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User pageUser = userService.findOne((long) pageUserID);
        UserProfileDto dto = userService.userProfile(pageUser, principalDetails.getUser());
        model.addAttribute("userDto", dto);
        log.info("userDto = {}", dto);
        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        log.info("user = {}", user);

        return "user/update";
    }

    @PostConstruct
    public void insert(){
        User user = User.builder().username("kim").password("aaa123").email("gotmail.com").name("sunghhon").profileImageUrl("aaa").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userService.join(user);
        userService.join(user2);
        userService.join(user3);
    }
}
