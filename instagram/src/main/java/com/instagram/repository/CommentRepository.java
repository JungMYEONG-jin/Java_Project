package com.instagram.repository;

import com.instagram.entity.Comment;
import com.instagram.entity.Image;
import com.instagram.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByUser(User user, Pageable pageable);
    List<Comment> findByUser(User user); // user가 댓글을 많이 달 수 있으므로 list로 받아오자.


    void deleteByUserAndImage(User user, Image image);
}
