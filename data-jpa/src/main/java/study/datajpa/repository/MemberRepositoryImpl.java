package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

// interface + impl 이름 맞춰야함! 그래야 작동

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{


    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom()
    {
        return em.createQuery("select m from Member m").getResultList();
    }
}
