package com.instagram.config.auth;

import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

// password spring security에서 체크.
// user 객체 return 되면 자동으로 세션 생성.
@RequiredArgsConstructor
@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 유저이름 중복 불가!
    // 향후 수정해야 할수도
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<User> result = userRepository.findByUsername(username);
        log.info("username={}", username);
        if(result.isEmpty()){

            return null;
        }else{
            return new PrincipalDetails(result.get(0));
        }
    }
}
