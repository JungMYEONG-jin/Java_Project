package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    // 컴파일시 오류 발견이 가능 @Query 이름이 없는 네임드 쿼리라 생각하면됨.

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); //optional

    // 페이징과 정렬 사용 예제
    // user count query
//    Page<Member> findByUsername(String username, Pageable pageable);
//    // not use count query
//    Slice<Member> findByUsername(String username, Pageable pageable);
//    // not user count query
//    List<Member> findByUsername(String username, Pageable pageable);

    List<Member> findByUsername(String username, Sort sort);

    // Named Qeury
    // page에 조건
    Slice<Member> findByAge(int age, Pageable pageable);





}
