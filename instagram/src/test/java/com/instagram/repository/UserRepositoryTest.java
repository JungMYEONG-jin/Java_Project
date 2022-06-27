package com.instagram.repository;

import com.instagram.config.AppConfig;
import com.instagram.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Test
    void insertTest() {
        User user = new User();
        user.setBio("zzzz");
        user.setComments(null);
        user.setEmail("abc@naver.com");
        user.setGender("Male");
        user.setImages(null);
        user.setUsername("larena");
        user.setPassword("123456");
        user.setPhone("010-1234");
        user.setRole("pro");
        user.setWebsite("Abc.com");
        user.setName("karen");

        encodePassword(user);
        System.out.println("user = " + user.getPassword());
        User savedUser = userRepository.save(user);
        long id = savedUser.getId();

        assertThat(savedUser.getEmail()).isEqualTo(userRepository.findById(id).get().getEmail());
        assertThat(passwordEncoder.matches("123456", savedUser.getPassword())).isTrue(); // 비밀번호 체크
    }

    private void encodePassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
    }
}