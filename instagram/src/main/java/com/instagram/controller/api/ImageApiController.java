package com.instagram.controller.api;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.CMRespDto;
import com.instagram.entity.Image;
import com.instagram.service.ImageService;
import com.instagram.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageApiController {

    private final ImageService imageService;
    private final LikesService likesService;

    @GetMapping("/api/images")
    public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size=3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        Page<Image> images = imageService.imageStory(principalDetails.getUser(), pageable);
        return new ResponseEntity<>(new CMRespDto<>(1, "story image", images.get()), HttpStatus.OK);
    }

    @PostMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> likes(@PathVariable long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Image findOne = imageService.findOne(imageId);
        if(findOne != null){
            likesService.like(findOne, principalDetails.getUser());
            return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CMRespDto<>(2, "좋아요 실패", null), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> unlikes(@PathVariable long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails){

        Image findOne = imageService.findOne(imageId);
        if (findOne != null){
            likesService.unlike(findOne, principalDetails.getUser());
            return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);

        }
        return new ResponseEntity<>(new CMRespDto<>(2, "좋아요 취소 실패", null), HttpStatus.BAD_REQUEST);
    }




}
