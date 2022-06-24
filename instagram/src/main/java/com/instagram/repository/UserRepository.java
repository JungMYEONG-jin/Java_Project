package com.instagram.repository;

import com.instagram.dto.UserDto;
import com.instagram.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String username);

    @Query("select u from User u where u.username = :username and u.email = :email")
    User findUser(@Param("username") String username, @Param("email") String email);
    // 컴파일시 오류 발견이 가능 @Query 이름이 없는 네임드 쿼리라 생각하면됨.

    @Query("select new com.instagram.dto.UserDto(u.username, u.password, u.email) from User u")
    List<UserDto> getMemberDto();

    @Query("select u from User u where u.email in :emails")
    List<User> findByEmails(@Param("emails") List<String> emails);

    @Query("select u from User u where u.username in :names")
    List<User> findByNames(@Param("names") List<String> names);

    Optional<User> findOptionalByEmail(String email); // optional email

    // 페이징과 정렬 사용 예제
    // user count query
    Page<User> findByUsername(String username, Pageable pageable);
    List<User> findByUsername(String username, Sort sort);


    // clear넣으면 알아서 영속성 제거해서 bulk연산 위험성 사라짐
//    @Modifying(clearAutomatically = true) // 이게 있어야 execute 실행됨
//    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
//    int bulkAgePlus(@Param("age") int age);


}
