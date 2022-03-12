import domain.Item;
import domain.Member;
import domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try
        {
            Member member = new Member();
            member.setName("hello");
            em.persist(member);
            Member member2 = new Member();
            member2.setName("hello2");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.getReference(Member.class, member.getId());
            System.out.println("m1.getClass() = " + m1.getClass());
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(m1) = " + emf.getPersistenceUnitUtil().isLoaded(m1));
            Hibernate.initialize(m1); // 강제초기화
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(m1) = " + emf.getPersistenceUnitUtil().isLoaded(m1));
        }catch(Exception e)
        {
            tx.rollback();

        }finally
        {
            em.close();
        }
        emf.close();
    }


}
