package com.ecommerce.service.user;

import com.ecommerce.entity.user.dto.UserReqDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    void createUserTest() {
        UserReqDto userReqDto = new UserReqDto();
        userReqDto.setEmail("abc123@gmail.com");
        userReqDto.setUsername("jeremy");
        userReqDto.setPassword("aaa123");
        userReqDto.setPasswordCheck("aaa123");
        userReqDto.setPhoneNumber("01046740554");
        userService.createUser(userReqDto);
    }

    @Test
    void checkDupTest(){
        UserReqDto userReqDto = new UserReqDto();
        userReqDto.setEmail("abc123@gmail.com");
        userReqDto.setUsername("jeremy");
        userReqDto.setPassword("aaa123");
        userReqDto.setPasswordCheck("aaa123");
        userReqDto.setPhoneNumber("01046740554");
        userService.createUser(userReqDto);

        UserReqDto userReqDto2 = new UserReqDto();
        userReqDto2.setEmail("abc123@gmail.com");
        userReqDto2.setUsername("kanj");
        userReqDto2.setPassword("aaa123");
        userReqDto2.setPasswordCheck("aaa123");
        userReqDto2.setPhoneNumber("01046740554");

        Assertions.assertThrows(IllegalArgumentException.class, ()->userService.createUser(userReqDto2));
    }
}