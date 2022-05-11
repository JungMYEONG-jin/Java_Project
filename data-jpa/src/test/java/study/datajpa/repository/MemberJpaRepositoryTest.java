package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember()
    {
        Member member = new Member("AA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);


    }

    @Test
    void basicCRUD()
    {
        Member member1 = new Member("A");
        Member member2 = new Member("B");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        // single result
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        // list result

        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 확인
        long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        // 삭제 후 카운트 확인
        long Dcount = memberJpaRepository.count();
        Assertions.assertThat(Dcount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGTTest()
    {
        Member aa = new Member("AA", 10);
        Member aa1 = new Member("AA", 20);

        memberJpaRepository.save(aa);
        memberJpaRepository.save(aa1);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGT("AA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);


    }

    @Test
    void paging()
    {
        memberJpaRepository.save(new Member("mem1", 10));
        memberJpaRepository.save(new Member("mem2", 10));
        memberJpaRepository.save(new Member("mem3", 10));
        memberJpaRepository.save(new Member("mem4", 10));
        memberJpaRepository.save(new Member("mem5", 10));
        memberJpaRepository.save(new Member("mem6", 10));

        int age =10;
        int offset=0;
        int limit=3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(6);



    }


}