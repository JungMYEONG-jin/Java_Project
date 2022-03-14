import domain.Item;
import domain.Member;
import domain.Movie;
import domain.embedded.Address;
import domain.embedded.Period;

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
            member.setUsername("hello");
            member.setHomeAddress(new Address("seoul", "gong", "321"));
            member.setWorkPeriod(new Period( LocalDateTime.now(), LocalDateTime.now()));

            em.persist(member);


            tx.commit();
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
