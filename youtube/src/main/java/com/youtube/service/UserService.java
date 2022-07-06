package com.youtube.service;

import com.youtube.model.User;
import com.youtube.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(){
        String sub = ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");

        return userRepository.findBySub(sub).orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));
    }

    public void addToLikesVideos(String videoId){
        User currentUser = getCurrentUser();
        currentUser.addToLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifLikedVideo(String videoId){
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    public boolean ifDisLikedVideo(String videoId){
        return getCurrentUser().getDisLikedVideos().stream().anyMatch(dislike -> dislike.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId){
        User currentUser = getCurrentUser();
        currentUser.removeFromLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public void removeFromDislikedVideos(String videoId){
        User currentUser = getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToDisLikedVideos(String videoId){
        User currentUser = getCurrentUser();
        currentUser.addToDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addVideoToHistory(String videoId){
        User currentUser = getCurrentUser();
        currentUser.addToVideoHistory(videoId);
        userRepository.save(currentUser);
    }

    public void subscribeUser(String userId){
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);

        User user = getUserById(userId);
        user.addToSubscribers(currentUser.getId());

        userRepository.save(user);
        userRepository.save(currentUser);
    }

    public void unSubscribeUser(String userId){
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);
        User user = getUserById(userId);
        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(user);
        userRepository.save(currentUser);

    }

    public Set<String> userHistory(String userId){
        User user = getUserById(userId);
        return user.getVideoHistory();
    }

    private User getUserById(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Cannot find user with userId " + userId));
    }


}
