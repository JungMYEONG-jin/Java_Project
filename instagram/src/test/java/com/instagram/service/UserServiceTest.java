package com.instagram.service;

import com.instagram.entity.User;
import com.instagram.handler.aop.LogAspect;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Import(LogAspect.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void register() {
        User user = new User();
        user.setEmail("abc.com");
        user.setPassword("123456");
        user.setUsername("karena");
        user.setName("koyachi");

        Long id = userService.join(user);
        User findUser = userService.findOne(id);

        Assertions.assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
        System.out.println("findUser = " + findUser.getPassword());

    }

    @Test
    void idTest() {
        Long aa = 2L;
        Long bb = 2L;
        if(aa == bb)
            System.out.println("Long SAME");
        else
            System.out.println("FALSE");
    }
}