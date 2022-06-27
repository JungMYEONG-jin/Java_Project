package com.instagram.repository;

import com.instagram.dto.subscribe.SubscribeDto;
import com.instagram.entity.Subscribe;
import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    void deleteByFromUserAndToUserEquals(User fromUser, User toUser); // 구독해제
    void deleteByFromUser(User fromUser); // 모든 구독자 제거.


    @Modifying
    @Query(value = "insert into Subscribe(from_UserID, to_UserID) values(:fromUserID, :toUserID)", nativeQuery = true)
    void doSubscribe(int fromUserID, int toUserID);

    @Modifying
    @Query(value = "delete from Subscribe where from_UserID = :fromUserID and to_UserID = :toUserID", nativeQuery = true)
    void unSubscribe(int fromUserID, int toUserID);



    @Query(value = "select count(*) from Subscribe where from_UserID =:principalID and to_UserID = :pageUserID", nativeQuery = true)
    int getSubscribeCount(int principalID, int pageUserID); // 유저의 구독개수

    //구독자 수
    @Query(value = "select count(*) from Subscribe where from_UserID = :pageUserID", nativeQuery = true)
    int getFollowerCount(@Param("pageUserID") int pageUserID);

    List<Subscribe> findByFromUserAndToUserEquals(User fromUser, User ToUser);
    List<Subscribe> findByFromUser(User pageUser);

    @Query("select new com.instagram.dto.subscribe.SubscribeDto(u.id, u.username, u.profileImageUrl, case when (u.id = :principalID) then 1 else 0 end, " +
            "case when (s.fromUser.id = :principalID and s.toUser.id = u.id) then 1 else 0 end ) " +
            "From User u inner join Subscribe s on u.id = s.toUser.id " +
            "where s.fromUser.id = :pageUserID")
    List<SubscribeDto> getSubscribeList(@Param("principalID") long principalID, @Param("pageUserID") long pageUserID);

}
