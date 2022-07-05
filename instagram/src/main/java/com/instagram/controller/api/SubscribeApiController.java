package com.instagram.controller.api;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.CMRespDto;
import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import com.instagram.handler.exception.CustomValidationException;
import com.instagram.service.SubscribeService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscribeApiController {

    private final SubscribeService subscribeService;
    private final UserService userService;

    @PostMapping("/api/subscribe/{toUserId}")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toUserId){

        User toUser = userService.findOne(toUserId);
        if(toUser!=null){
            subscribeService.subscribe(principalDetails.getUser(), toUser);
            return new ResponseEntity<>(new CMRespDto<>(1, "구독 성공", null), HttpStatus.OK);
        }
        return new ResponseEntity<>("구독 유저나 구독하려는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/api/subscribe/{toUserId}")
    public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toUserId){
        User toUser = userService.findOne(toUserId);
        if(toUser!=null){
            subscribeService.unSubscribe(principalDetails.getUser(), toUser);
            return new ResponseEntity<>(new CMRespDto<>(1, "구독 해제 성공", null), HttpStatus.OK);
        }
        return new ResponseEntity<>("구독 유저나 구독 취소하려는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    }

}
