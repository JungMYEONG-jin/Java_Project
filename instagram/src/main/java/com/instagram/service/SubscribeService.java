package com.instagram.service;

import com.instagram.dto.subscribe.SubscribeDto;
import com.instagram.dto.user.UserDto;
import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import com.instagram.handler.exception.CustomAPIException;
import com.instagram.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    // 구독
    @Transactional
    public void subscribe(User fromUser, User toUser){
        try{
            Subscribe sub = Subscribe.builder().fromUser(fromUser).toUser(toUser).build();
            subscribeRepository.save(sub);
        }catch (Exception e){
            throw new CustomAPIException("이미 구독중입니다...");
        }
    }
    // 객체지향적으로 구현하기 위해 객체로 넘김
    @Transactional
    public void unSubscribe(User fromUser, User toUser){
        subscribeRepository.deleteByFromUserAndToUserEquals(fromUser, toUser);
    }

    // 조회는 수정 불가하게
    @Transactional(readOnly = true)
    public List<SubscribeDto> getSubscribeList(User principal, User pageUser){

        return subscribeRepository.getSubscribeListOOP(principal, pageUser);
        // 혹시 문제 발생시 아래 사용
//        return subscribeRepository.getSubscribeList(principal.getId(), pageUser.getId());


    }
}
