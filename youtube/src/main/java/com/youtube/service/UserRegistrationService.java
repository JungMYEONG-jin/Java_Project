package com.youtube.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.dto.UserInfoDto;
import com.youtube.model.User;
import com.youtube.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue){
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDto userInfoDto = objectMapper.readValue(body, UserInfoDto.class);

            Optional<User> findUser = userRepository.findBySub(userInfoDto.getSub());
            if(findUser.isPresent()){
                return findUser.get().getId();
            }else{
                User user = new User();
                user.setFirstName(userInfoDto.getGivenName());
                user.setLastName(userInfoDto.getFamilyName());
                user.setFullName(userInfoDto.getName());
                user.setEmail(userInfoDto.getEmail());
                user.setSub(userInfoDto.getSub());

                return userRepository.save(user).getId();
            }
        }catch (Exception exception){
            throw new RuntimeException("Exception occurred while registering user", exception);
        }
    }
}
