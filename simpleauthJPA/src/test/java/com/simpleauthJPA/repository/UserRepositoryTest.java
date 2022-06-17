package com.simpleauthJPA.repository;

import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.entity.UserDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void FunctionTest1(){
        List<User> saUserInfo = userRepository.getSAUserInfo("18920104-48b4-48f0-ac02-685063f96562");
        for (User user : saUserInfo) {
            UserDto userDto = new UserDto(user.getId(), user.getPubkey(), user.getUuid(), user.getAppid(), user.getType());
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    void FunctionTest2(){
        List<User> saUserInfo = userRepository.getSAUserInfo("18920104-48b4-48f0-ac02-685063f96562");
        User user = saUserInfo.get(0);
        user.setType("1");
        userRepository.save(user);

        List<User> byIdAndTypeEquals = userRepository.findByIdAndTypeEquals("18920104-48b4-48f0-ac02-685063f96562", "1");
        for (User findUser : byIdAndTypeEquals) {
            UserDto userDto = new UserDto(findUser.getId(), findUser.getPubkey(), findUser.getUuid(), findUser.getAppid(), findUser.getType());
            System.out.println("userDto = " + userDto);
        }

    }

}