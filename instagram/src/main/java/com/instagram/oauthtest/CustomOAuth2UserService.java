package com.instagram.oauthtest;

import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final GoogleUserRepository userRepository;
    private final HttpSession session;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        GoogleUser googleUser = saveOrUpdate(attributes);
        session.setAttribute("user", new SessionUser(googleUser));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(googleUser.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());


    }

    public GoogleUser saveOrUpdate(OAuthAttributes attributes){
        GoogleUser googleUser = userRepository.findByEmail(attributes.getEmail()).map(e -> e.update(attributes.getName(), attributes.getPicture())).orElse(attributes.toEntity());
        return userRepository.save(googleUser);
    }
}
