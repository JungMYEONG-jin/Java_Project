package com.instagram.repository;

import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SubscribeRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Test
//    @Transactional
    void getSubscribeCountTest() {
        User user = new User();
        user.setEmail("ac.com");
        user.setPassword("123456");
        user.setUsername("karean");
        user.setName("minguk");

        User user2 = new User();
        user2.setEmail("c121.com");
        user2.setPassword("122223456");
        user2.setUsername("korem");
        user2.setName("minaaguk");

        User user3 = new User();
        user3.setEmail("gote22.com");
        user3.setPassword("ap02oa");
        user3.setUsername("amiak");
        user3.setName("mklao");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Subscribe subscribe = new Subscribe();
        subscribe.setFromUser(user);
        subscribe.setToUser(user2);

        Subscribe subscribe2 = new Subscribe();
        subscribe2.setFromUser(user);
        subscribe2.setToUser(user3);

        subscribeRepository.save(subscribe);
        subscribeRepository.save(subscribe2);

        int followerCount = subscribeRepository.getFollowerCount(1);
        Assertions.assertThat(followerCount).isEqualTo(2);

        List<Subscribe> result = subscribeRepository.findByFromUser(user);
        Assertions.assertThat(result.size()).isEqualTo(2);

        List<Subscribe> result2 = subscribeRepository.findByFromUserAndToUserEquals(user, user3);
        Assertions.assertThat(result2.size()).isEqualTo(1);
    }

    @Test
    void deleteTest() {
        User user = new User();
        user.setEmail("ac.com");
        user.setPassword("123456");
        user.setUsername("karean");
        user.setName("minguk");

        User user2 = new User();
        user2.setEmail("c121.com");
        user2.setPassword("122223456");
        user2.setUsername("korem");
        user2.setName("minaaguk");

        User user3 = new User();
        user3.setEmail("gote22.com");
        user3.setPassword("ap02oa");
        user3.setUsername("amiak");
        user3.setName("mklao");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Subscribe subscribe = new Subscribe();
        subscribe.setFromUser(user);
        subscribe.setToUser(user2);

        Subscribe subscribe2 = new Subscribe();
        subscribe2.setFromUser(user);
        subscribe2.setToUser(user3);

        subscribeRepository.save(subscribe);
        subscribeRepository.save(subscribe2);

        subscribeRepository.deleteByFromUser(user);

        int followerCount = subscribeRepository.getFollowerCount(1);
        Assertions.assertThat(followerCount).isEqualTo(0);

    }
}