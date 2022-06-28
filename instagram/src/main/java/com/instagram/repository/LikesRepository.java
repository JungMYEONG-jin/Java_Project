package com.instagram.repository;

import com.instagram.entity.Image;
import com.instagram.entity.Likes;
import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Modifying
    @Query(value = "delete from Likes where imageid = :imageId and userid = :principalId", nativeQuery = true)
    void unlike(@Param("imageId") long imageId, @Param("principalId") long principalId);

    void deleteByUserAndImageEquals(User user, Image image);
    void deleteByUser(User user);

}
