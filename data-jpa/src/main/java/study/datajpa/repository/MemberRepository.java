package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

// interface 상속받고 메소드는 사실 저걸 구현한 impl거를 실행ㅎ라는거

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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
    // count query 넣어서 쿼리를 분리해도됨.
    @Query(value = "select m from Member m left join m.team t")
    Page<Member> findByAge(int age, Pageable pageable);

    // clear넣으면 알아서 영속성 제거해서 bulk연산 위험성 사라짐
    @Modifying(clearAutomatically = true) // 이게 있어야 execute 실행됨
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value = @QueryHint( name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
