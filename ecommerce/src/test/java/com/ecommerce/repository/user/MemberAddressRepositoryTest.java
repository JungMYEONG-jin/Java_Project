package com.ecommerce.repository.user;

import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.Place;
import com.ecommerce.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberAddressRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberAddressRepository memberAddressRepository;


    @Test
    void insertTest() {

        User user = User.builder().username("kim").email("abc123@gmail.com").password("aa123").rocketMembership(true).phoneNumber("01203213").build();
        User user2 = User.builder().username("kong").email("koreak12@gmail.com").password("aa123").rocketMembership(true).phoneNumber("1232321").build();
        User user3 = User.builder().username("hong").email("miandk13@gmail.com").password("aa123").rocketMembership(true).phoneNumber("7786876").build();
        User user4 = User.builder().username("pong").email("akswnfk3223@gmail.com").password("aa123").rocketMembership(true).phoneNumber("543543").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        MemberAddress add1 = MemberAddress.builder().address("동작구 흑석로 5나길11").user(user).homePhone("02-1123-2322").main(true).phone("9932993").place(Place.경비실).receiverName("mj").build();
        MemberAddress add2 = MemberAddress.builder().address("동작구 흑석로 1나길11").user(user2).homePhone("02-1123-2322").main(true).phone("32321").place(Place.경비실).receiverName("mjaa").build();
        MemberAddress add3 = MemberAddress.builder().address("동작구 흑석로 2나길11").user(user).homePhone("02-112223-23222").main(true).phone("342432").place(Place.문앞).receiverName("mjdsadasaa").build();

        memberAddressRepository.save(add1);
        memberAddressRepository.save(add2);
        memberAddressRepository.save(add3);

        MemberAddress memberAddress = memberAddressRepository.findByIdAndUserId(1, 1).get();
        System.out.println("memberAddress = " + memberAddress);
    }
}