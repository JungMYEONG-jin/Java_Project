package com.ecommerce.service.user;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.user.Authority;
import com.ecommerce.entity.user.User;
import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.PasswordChangeDto;
import com.ecommerce.entity.user.dto.UserReqDto;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean checkEmailDuplicate(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public void createUser(UserReqDto signUpDto){

        if(checkEmailDuplicate(signUpDto.getEmail()))
            throw new IllegalArgumentException("이미 존재하는 email 입니다...");

        User user = ModelMapperUtils.getModelMapper().map(signUpDto, User.class);
        Authority authority = new Authority();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authority.setUser(user);
        authority.setAuthority("ROLE_USER");
        user.getAuthorities().add(authority);
        userRepository.save(user);
    }

    public void changePhoneNumber(Principal principal, String phoneNumber){

        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new RuntimeException("not exist user"));
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        principal.setUser(user);
    }

    public void changeUsername(Principal principal, String username){
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new RuntimeException("not exist user"));
        user.setUsername(username);
        userRepository.save(user);
        principal.setUser(user);
    }

    public void changeEmail(Principal principal, String email){
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new RuntimeException("not exist user"));
        user.setEmail(email);
        userRepository.save(user);
        principal.setUser(user);
    }

    public void changePassword(Principal principal, PasswordChangeDto dto){
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new RuntimeException("not exist user"));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        principal.setUser(user);
    }

    public void deleteUser(Principal principal, HttpSession session){
        userRepository.deleteById(principal.getUser().getId());
        session.invalidate();
    }

    public UserResDto changeMembership(Principal principal){
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new RuntimeException("not exist user"));
        user.setRocketMembership(!user.isRocketMembership());
        User savedUser = userRepository.save(user);
        principal.setUser(savedUser);
        return UserResDto.of(user);
    }

}
