package com.instagram.service;

import com.instagram.entity.User;
import com.instagram.repository.SubscribeRepository;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscribeRepository subscribeRepository;

    @Value("${file.path}")
    private String uploadDir;

    @Transactional
    public Long join(User user){
        encodePassword(user);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * Search all Users
     */

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public User findOne(Long userID){
        return userRepository.findById(userID).get();
    }

    public List<User> findSpecificUserName(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> findSpecificUserNames(List<String> usernames){
        return userRepository.findByNames(usernames);
    }

    public User findUser(String username, String email){
        return userRepository.findUser(username, email);
    }

    public User findByEmail(String email){
        return userRepository.findOptionalByEmail(email).get();
    }


    @Transactional
    public void update(Long id, String username){
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();
        user.setUsername(username); // 영속성 관리중이라 set만 해도 알아서 update 됨
        // 할당 받은 id가 존재함. 즉 pk가 존재하므로 update로 관리됨.
    }


    private void encodePassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
    }


}
