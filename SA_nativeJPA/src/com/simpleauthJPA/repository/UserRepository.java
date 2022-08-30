package com.simpleauthJPA.repository;

import com.simpleauthJPA.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(String id);
    List<User> findByIdAndTypeEquals(String id, String type);
    User findByIdAndUnregdateEquals(String id, String unregdate);
    @Query("select u from User u where u.id = :id and u.unregdate = '999999999'")
    List<User> getSAAuthInitInfo(@Param("id") String id);
    @Query("select u from User u where u.id = :id and u.unregdate = '999999999'")
    List<User> getSAUserInfo(@Param("id") String id);

    Page<User> findByCusno(String cusno, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<User> findLockByCusno(String cusno);






}

