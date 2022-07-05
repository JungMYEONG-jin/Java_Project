package com.instagram.controller.api;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.CMRespDto;
import com.instagram.dto.comment.CommentDto;
import com.instagram.entity.Comment;
import com.instagram.entity.Image;
import com.instagram.service.CommentService;
import com.instagram.service.ImageService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;
    private final ImageService imageService;

    @PostMapping("/api/comment")
    public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){


        Image image = imageService.findOne(commentDto.getImageId());
        Comment comment = commentService.write(commentDto.getContent(), image, principalDetails.getUser());

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글 쓰기 성공", comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable long id){
        commentService.unComment(id);
        return new ResponseEntity<>(new CMRespDto<>(1, "댓글 삭제 서공", null), HttpStatus.OK);
    }
}
