package com.simpleauthJPA.repository;

import com.simpleauthJPA.aop.SALogAspect;
import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.entity.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Import(SALogAspect.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void FunctionTest1(){
//        log.info(userRepository.getClass().toString());
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

    @Test
    void ObjectTest(){
        List<User> saUserInfo = userRepository.getSAUserInfo("18920104-48b4-48f0-ac02-685063f96562");
        Object a = saUserInfo.get(0);
        User user = (User) a;
        UserDto userDto = new UserDto(user.getId(), user.getPubkey(), user.getUuid(), user.getAppid(), user.getType());
        System.out.println("userDto = " + userDto);
    }

    @Test
    void PagingTest(){
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "regdate"));

        Page<User> byCusno = userRepository.findByCusno("20220333", pageRequest);
        List<User> users = byCusno.getContent();


        Page<UserDto> pageUserDto = byCusno.map(e ->
                new UserDto(e.getId(), e.getPubkey(), e.getUuid(), e.getAppid(), e.getType()));

        List<UserDto> result = pageUserDto.getContent(); // getContent 하면 list로 return 됨

        int idx = 1;
        for (UserDto userDto : result) {
            System.out.println("user " + idx++ + " = " + userDto);
        }

    }

}