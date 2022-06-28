package com.instagram.service;

import com.instagram.entity.Image;
import com.instagram.entity.Likes;
import com.instagram.entity.User;
import com.instagram.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    @Transactional
    public void like(Image image, User principal){
        Likes likes = Likes.builder().user(principal).image(image).build();
        likesRepository.save(likes); // likes 저장
        // 기존 네이티브쿼리도 가능하지만, 최대한 간결하고 객체지향적으로 구현.
    }

    @Transactional
    public void unlike(Image image, User principal){
//        likesRepository.unlike(image.getId(), principal.getId());
        likesRepository.deleteByUserAndImageEquals(principal, image);
    }
}
