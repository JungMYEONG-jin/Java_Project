package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void testMember()
    {
        Member member = new Member("AA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD()
    {
        Member member1 = new Member("A");
        Member member2 = new Member("B");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // single result
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        // list result

        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 확인
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberRepository.deleteAll();

        // 삭제 후 카운트 확인
        long Dcount = memberRepository.count();
        Assertions.assertThat(Dcount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGTTest()
    {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("AA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testQuery()
    {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        List<Member> result = memberRepository.findUser("AA", 10);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(10);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void findUsernameList()
    {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("username = " + s);
        }
    }

    @Test
    void findDtoTest()
    {
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member aa = new Member("AA", 10);
        memberRepository.save(aa);
        aa.setTeam(team);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }


    @Test
    void findByNames() {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AA", "ABA"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);

        }
    }

    @Test
    void findListByUsername() {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        List<Member> byNames = memberRepository.findListByUsername("AA");
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);

        }

        // 결과가 없을때 단건조회는 NULL
        // collection은 빈 컬렉션 널이 아님!!
    }

    @Test
    void findMemberByUsername() {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        Member byNames = memberRepository.findMemberByUsername("AA");
        System.out.println("byNames = " + byNames);
    }

    @Test
    void findMemberByUsernameOptionalTest() {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("ABA", 20);

        memberRepository.save(aa);
        memberRepository.save(aa1);

        Member byNames = memberRepository.findOptionalByUsername("AA").get();
        System.out.println("byNames = " + byNames);
    }


    @Test
    void findByAgeTest() {

        memberRepository.save(new Member("mem1", 10));
        memberRepository.save(new Member("mem2", 10));
        memberRepository.save(new Member("mem3", 10));
        memberRepository.save(new Member("mem4", 10));
        memberRepository.save(new Member("mem5", 10));
        memberRepository.save(new Member("mem6", 10));

        // spring data jpa page 0부터 시작
        // slice는 limit를 +1 해서 보냄
        // total count는 모든 데이터 읽기 때문에 느림
        // 가급적 사용 자제
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // enetity는 외부로 보내면 큰일남
        // dto로 보내자
        Page<Member> page = memberRepository.findByAge(10, pageRequest);

        Page<MemberDto> result = page.map(m -> new MemberDto(m.getId(), m.getUsername(),null));

        List<MemberDto> memberDtos = result.getContent();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }


        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(6);
        Assertions.assertThat(page.getNumber()).isEqualTo(0); // first page
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); // 0 1 2
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.isLast()).isFalse();
    }


    @Test
    void bulkAgePlus() {
        memberRepository.save(new Member("mem1", 10));
        memberRepository.save(new Member("mem2", 20));
        memberRepository.save(new Member("mem3", 19));
        memberRepository.save(new Member("mem4", 21));
        memberRepository.save(new Member("mem5", 40));

        int resultCnt = memberRepository.bulkAgePlus(20);
//        em.flush();
//        em.clear();

        List<Member> result = memberRepository.findByUsername("mem5");
        Member member = result.get(0);
        System.out.println("member = " + member); // 따라서 영속성 제거 하지 않으면 40살로 나옴. 영속성 제거 필수!


        Assertions.assertThat(resultCnt).isEqualTo(3);
    }


}