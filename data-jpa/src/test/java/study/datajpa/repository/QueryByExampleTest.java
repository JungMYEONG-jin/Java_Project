package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class QueryByExampleTest {

    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    void basic() throws Exception
    {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();

        Member m1 = new Member("m1");
        Team team = new Team("teamA");
        m1.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age"); // age 속성을 무시한다.

        Example<Member> example = Example.of(m1, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
