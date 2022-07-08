package com.ecommerce.repository.user;

import com.ecommerce.entity.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Description("insert 성공 case")
    @Test
    void insertTest() {
        User user = User.builder().username("kim").email("abc123@gmail.com").password("aa123").rocketMembership(true).phoneNumber("01203213").build();
        userRepository.save(user);

        Optional<User> byEmail = userRepository.findByEmail("abc123@gmail.com");
        User findUser = byEmail.get();
        Assertions.assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void uniqueTest(){
        User user = User.builder().username("kim").email("abc123@gmail.com").password("aa123").rocketMembership(true).phoneNumber("01203213").build();
        userRepository.save(user);
        User user2 = User.builder().username("jj").email("abc123@gmail.com").password("adsa122").rocketMembership(true).phoneNumber("32132132").build();
        userRepository.save(user2);
    }

    @Test
    void lastModifiedTest(){
        User user = User.builder().username("kim").email("abc123@gmail.com").password("aa123").rocketMembership(true).phoneNumber("01203213").build();
        userRepository.save(user);

        User user2 = User.builder().username("jj").email("1211@gmail.com").password("adsa122").rocketMembership(true).phoneNumber("32132132").build();
        userRepository.save(user2);

        sleep(1000);
        User findUser = userRepository.findByEmail(user.getEmail()).get();
        findUser.setUsername("jkim");
        userRepository.save(findUser);
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}