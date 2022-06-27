package com.instagram.service;

import com.instagram.dto.user.UserProfileDto;
import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import com.instagram.handler.exception.CustomAPIException;
import com.instagram.handler.exception.CustomException;
import com.instagram.handler.exception.CustomValidationAPIException;
import com.instagram.repository.SubscribeRepository;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Transactional(readOnly = true)
    public UserProfileDto userProfile(User pageUser, User principalUser){

        UserProfileDto userProfileDto = new UserProfileDto();

        User findUser = userRepository.findById(pageUser.getId()).orElseThrow(() -> new CustomException("해당 프로필 페이지는 존재하지 안습니다."));

        userProfileDto.setUser(findUser);
        userProfileDto.setPageOwnerState(pageUser.getId() == principalUser.getId());
        userProfileDto.setImageCount(findUser.getImages().size());

        List<Subscribe> res = subscribeRepository.findByFromUserAndToUserEquals(principalUser, pageUser);
        List<Subscribe> res2 = subscribeRepository.findByFromUser(pageUser);
        userProfileDto.setSubscribeCount(res2.size());
        userProfileDto.setSubscribeState(res.size() == 1); // 구독관계는 결과가 1명이 나와야함.
        // A가 B를 구독하면 단 하나의 관계가 성립하는것임.ㄴ

        // 회원 프로필 페이지 이미지에 좋아요 횟수 표시
        findUser.getImages().forEach(i -> {
            i.setLikeCnt(i.getLikes().size());
        });

        return userProfileDto;
    }

    /**
     * id로 찾고 전달받은 user 정보로 업데이트
     * @param id
     * @param user
     * @return
     */
    @Transactional
    public User updateInfo(long id, User user){
        User findUser = userRepository.findById(id).orElseThrow(() -> new CustomValidationAPIException("찾을 수 없는 아이디입니다."));

        // 영속성 관리중이므로 자동으로 수정됨
        findUser.setName(user.getName());
        String encodedPassword = getEncodedPassword(user);
        findUser.setPassword(encodedPassword);

        findUser.setBio(user.getBio());
        findUser.setWebsite(user.getWebsite());
        findUser.setPhone(user.getPhone());
        findUser.setGender(user.getGender());
        return findUser;
    }

    @Transactional
    public User userProfileUpdate(long principalId, MultipartFile profileImageFile){
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();

        Path filePath = Paths.get(uploadDir + imageFileName);
        try{
            Files.write(filePath, profileImageFile.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        // 사진 DB 저장
        User user = userRepository.findById(principalId).orElseThrow(() -> new CustomAPIException("해당 ID에 해당하는 유저가 존재하지 않습니다,."));
        user.setProfileImageUrl(imageFileName);
        return user;
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

    private String getEncodedPassword(User user){
        return passwordEncoder.encode(user.getPassword());
    }


}
