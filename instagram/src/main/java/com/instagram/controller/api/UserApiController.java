package com.instagram.controller.api;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.CMRespDto;
import com.instagram.dto.subscribe.SubscribeDto;
import com.instagram.dto.user.UserUpdateDto;
import com.instagram.entity.User;
import com.instagram.service.SubscribeService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final SubscribeService subscribeService;

    @PutMapping("/api/user/{id}")
    public CMRespDto<?> update(@PathVariable long id, @Valid UserUpdateDto userUpdateDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = userService.update2(id, userUpdateDto.getName());
        principalDetails.setUser(user);
        return new CMRespDto<>(1, "회원 수정 완료", user);
    }

    @GetMapping("/api/user/{pageUserId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User user = userService.findOne(pageUserId);
        if (user != null) {
            List<SubscribeDto> subscribeList = subscribeService.getSubscribeList(principalDetails.getUser(), user);
            return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 가져오기 성공", subscribeList), HttpStatus.OK);
        }
        return new ResponseEntity<>("구독자 정보 획득에 실패하였습니다. 매칭 정보를 다시 확인해주세여", HttpStatus.BAD_REQUEST);
    }

    //사진 변경
    @PutMapping("/api/user/{princiaplId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable long princiaplId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = userService.userProfileUpdate(princiaplId, profileImageFile);
        principalDetails.setUser(user);
        return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 변경  완료", null), HttpStatus.OK);
    }
}
