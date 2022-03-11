import domain.real.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Book book = new Book();
            book.setAuthor("JMJ");
            book.setName("Multivariate Analysis");
            book.setPrice(222211);
            book.setIsbn("98329-11112-12121");
            em.persist(book);


            tx.commit();
        }catch(Exception e){
            tx.rollback();

        }finally{
            em.close();
        }

        emf.close();


    }
}

//항상 다에 외래키가 있어야함
// 기본키로는 불가능하기때문

