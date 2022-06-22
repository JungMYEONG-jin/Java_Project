package com.simpleauthJPA.service;

import com.simpleauthJPA.entity.UserLog;
import com.simpleauthJPA.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepository;

    public UserLog save(UserLog userLog){
        return userLogRepository.save(userLog);
    }

    public List<UserLog> findByCusno(String cusno){
        return userLogRepository.findByCusno(cusno);
    }

}
