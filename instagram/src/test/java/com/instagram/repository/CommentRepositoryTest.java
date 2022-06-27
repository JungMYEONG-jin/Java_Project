package com.instagram.repository;

import com.instagram.entity.Comment;
import com.instagram.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void saveTest() {
        User user = new User();
        user.setEmail("ac.com");
        user.setPassword("123456");
        user.setUsername("karean");
        user.setName("mink");
        userRepository.save(user); // save 안하면 flush() 하라고 에러 나옴...

        Comment comment = Comment.builder().content("정말 멋지네요.").image(null).user(user).build();

        Comment savedComment = commentRepository.save(comment);
        Assertions.assertThat(savedComment.getContent()).isEqualTo(comment.getContent());
        Assertions.assertThat(savedComment).isEqualTo(comment);
    }

    @Test
    void findTest() {
        User user = new User();
        user.setEmail("ac.com");
        user.setPassword("123456");
        user.setUsername("karean");
        user.setName("mink");
        userRepository.save(user); // save 안하면 flush() 하라고 에러 나옴...

        Comment comment = Comment.builder().content("정말 멋지네요.").image(null).user(user).build();
        Comment comment2 = Comment.builder().content("정말 great.").image(null).user(user).build();

        commentRepository.save(comment);
        commentRepository.save(comment2);

        List<Comment> result = commentRepository.findByUser(user);
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}