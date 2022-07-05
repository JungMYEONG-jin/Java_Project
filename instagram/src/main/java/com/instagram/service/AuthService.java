package com.instagram.service;

import com.instagram.entity.User;
import com.instagram.handler.exception.CustomAPIException;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * repository에 저장하지 않은 유저여야 함!!
     * 굳이 안쓰고 userService에서 처리해도 될듯함.
     * 그렇지 않으면 패스워드가 무의미함.
     * @param user
     * @return
     */
    @Transactional
    public User join(User user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setRole("Normal_USER");
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
