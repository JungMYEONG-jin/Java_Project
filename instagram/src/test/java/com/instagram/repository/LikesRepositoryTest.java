package com.instagram.repository;

import com.instagram.entity.Image;
import com.instagram.entity.Likes;
import com.instagram.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesRepositoryTest {

    @Autowired
    LikesRepository likesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;


    @Test
    void insertTest() {

        User user = User.builder().username("kim").password("kak233##@").email("gotmail.com").name("sunghhon").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Image image = Image.builder().user(user).caption("akak11").postImageUrl("karena").build();
        Image image2 = Image.builder().user(user2).caption("gogi").postImageUrl("show me").build();
        Image image3 = Image.builder().user(user3).caption("pizza").postImageUrl("give").build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Likes likes = Likes.builder().image(image).user(user).build();
        Likes likes2 = Likes.builder().image(image).user(user2).build();
        Likes likes3 = Likes.builder().image(image2).user(user).build();

        likesRepository.save(likes);
        likesRepository.save(likes2);
        likesRepository.save(likes3);

        // check size
        List<Likes> result = likesRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(3);

    }

    @Test
    void unlikeTest() {

        User user = User.builder().username("kim").password("kak233##@").email("gotmail.com").name("sunghhon").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Image image = Image.builder().user(user).caption("akak11").postImageUrl("karena").build();
        Image image2 = Image.builder().user(user2).caption("gogi").postImageUrl("show me").build();
        Image image3 = Image.builder().user(user3).caption("pizza").postImageUrl("give").build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Likes likes = Likes.builder().image(image).user(user).build();
        Likes likes2 = Likes.builder().image(image).user(user2).build();
        Likes likes3 = Likes.builder().image(image2).user(user).build();

        likesRepository.save(likes);
        likesRepository.save(likes2);
        likesRepository.save(likes3);


        likesRepository.unlike(image.getId(), user.getId());

        List<Likes> result = likesRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void deleteByUserTest() {
        User user = User.builder().username("kim").password("kak233##@").email("gotmail.com").name("sunghhon").build();
        User user2 = User.builder().username("kang").password("kang##@").email("hotm.com").name("amy").build();
        User user3 = User.builder().username("song").password("song232##@").email("coldaa.com").name("kjje").build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Image image = Image.builder().user(user).caption("akak11").postImageUrl("karena").build();
        Image image2 = Image.builder().user(user2).caption("gogi").postImageUrl("show me").build();
        Image image3 = Image.builder().user(user3).caption("pizza").postImageUrl("give").build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Likes likes = Likes.builder().image(image).user(user).build();
        Likes likes2 = Likes.builder().image(image).user(user2).build();
        Likes likes3 = Likes.builder().image(image2).user(user).build();

        likesRepository.save(likes);
        likesRepository.save(likes2);
        likesRepository.save(likes3);

        likesRepository.deleteByUser(user);

        List<Likes> result = likesRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}