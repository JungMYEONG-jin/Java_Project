package com.instagram.config.oauth;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(this.getClass().getSimpleName() + "." +"loadUser() call");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> userInfo = oAuth2User.getAttributes();
        String name = userInfo.get("name").toString();
        String email = userInfo.get("email").toString();
        String username = "facebook_" + userInfo.get("id").toString();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        Optional<User> user = userRepository.findById((long) userInfo.get("id"));
        if(user.isEmpty()){
            User role_user = User.builder().name(name).username(username).password(password).email(email).role("ROLE_USER").build();
            return new PrincipalDetails(userRepository.save(role_user), oAuth2User.getAttributes());
        }else{
            //기존 유저
            return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
        }
    }
}
