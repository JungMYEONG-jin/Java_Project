package crawler.playStore.crawler.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(App app){
        em.persist(app);
    }

    public App findById(Long id){
        return em.find(App.class, id);
    }

    public List<App> findAll(){
        return em.createQuery("select a from App a", App.class).getResultList();
    }

    public List<App> findByName(String name){
        return em.createQuery("select a from App a where a.name = :name", App.class).setParameter("name", name).getResultList();
    }



}
