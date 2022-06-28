package com.instagram.repository;

import com.instagram.entity.Image;
import com.instagram.entity.Likes;
import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ImageRepositoryTest {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikesRepository likesRepository;

    @Test
    void pageTest() {
        User user = User.builder().username("kim").password("kak233##@").email("gotmail.com").name("sunghhon").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Image image = Image.builder().user(user).caption("akak11").postImageUrl("karena").build();
        Image image2 = Image.builder().user(user3).caption("gogi").postImageUrl("show me").build();
        Image image3 = Image.builder().user(user3).caption("pizza").postImageUrl("give").build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Subscribe sub = Subscribe.builder().toUser(user2).fromUser(user).build();
        Subscribe sub2 = Subscribe.builder().toUser(user3).fromUser(user).build();

        subscribeRepository.save(sub);
        subscribeRepository.save(sub2
        );

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));

        Page<Image> imageByPaging = imageRepository.getImageByPaging(user, pageRequest);
        List<Image> content = imageByPaging.getContent();
        for (Image image1 : content) {
            System.out.println("image1 = " + image1);
        }

    }


    @Test
    void getPopularTest() {
        User user = User.builder().username("kim").password("kak233##@").email("gotmail.com").name("sunghhon").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Image image = Image.builder().user(user).caption("akak11").postImageUrl("karena").build();
        Image image2 = Image.builder().user(user3).caption("gogi").postImageUrl("show me").build();
        Image image3 = Image.builder().user(user3).caption("pizza").postImageUrl("give").build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Likes like1 = Likes.builder().user(user).image(image).build();
        Likes like2 = Likes.builder().user(user2).image(image).build();
        Likes like3 = Likes.builder().user(user3).image(image2).build();
        Likes like4 = Likes.builder().user(user3).image(image3).build();

        likesRepository.save(like1);
        likesRepository.save(like2);
        likesRepository.save(like3);
        likesRepository.save(like4);

        List<Image> result = imageRepository.getImagesByPopularDesc();
        for (Image image1 : result) {
            System.out.println("image result = " + image1);
        }

    }
}