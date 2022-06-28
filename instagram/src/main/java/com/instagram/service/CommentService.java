package com.instagram.service;

import com.instagram.entity.Comment;
import com.instagram.entity.Image;
import com.instagram.entity.User;
import com.instagram.handler.exception.CustomAPIException;
import com.instagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment write(String content, Image image, User user){
        Comment comment = Comment.builder().user(user).image(image).content(content).build();
        commentRepository.save(comment);
        return comment;
    }


    public void unComment(long id){
        try{
            commentRepository.deleteById(id);
        }catch (Exception e){
            throw new CustomAPIException(e.getMessage());
        }
    }
}







