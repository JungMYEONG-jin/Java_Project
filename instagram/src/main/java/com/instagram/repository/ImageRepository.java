package com.instagram.repository;

import com.instagram.entity.Image;
import com.instagram.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUser(User user);
    Optional<Image> findById(Long id);
    @Query(value = "select i.* from Image i inner join (select count(imageID) as likeCount, imageID from likes group by imageID) l on i.imageID = l.imageID order by likeCount desc", nativeQuery = true)
    List<Image> getImagesByPopularDesc();

    @Query("select i from Image i where i.user in (select s.toUser from Subscribe s where s.fromUser = :principal)")
    Page<Image> getImageByPaging(@Param("principal") User principal, Pageable pageable);

}
