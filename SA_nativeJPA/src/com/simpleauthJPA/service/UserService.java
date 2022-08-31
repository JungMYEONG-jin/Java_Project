package com.simpleauthJPA.service;

import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user){
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Transactional(readOnly = true)
    public List<User> findUsersByCusno(String cusno)
    {
        List<User> lockByCusno = userRepository.findLockByCusno(cusno);
        return lockByCusno;
    }

    public User findById(String id){
        return userRepository.findById(id);
    }

    public List<User> findByIdAndType(String id, String type){
        return userRepository.findByIdAndTypeEquals(id, type);
    }

    public List<User> getUserInfo(String id){
        return userRepository.getSAUserInfo(id);
    }
}
