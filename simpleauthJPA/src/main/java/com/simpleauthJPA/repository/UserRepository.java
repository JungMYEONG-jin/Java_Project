package com.simpleauthJPA.repository;

import com.simpleauthJPA.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    User findById(String id);
    List<User> findByIdAndTypeEquals(String id, String type);
    User findByIdAndUnregdateEquals(String id, String unregdate);
    @Query("select u from User u where u.id = :id and u.unregdate = '999999999'")
    List<User> getSAAuthInitInfo(@Param("id") String id);
    @Query("select u from User u where u.id = :id and u.unregdate = '999999999'")
    List<User> getSAUserInfo(@Param("id") String id);

}

