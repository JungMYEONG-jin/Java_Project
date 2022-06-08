package crawler.playStore.crawler.repository;

import crawler.playStore.crawler.entity.App;
import crawler.playStore.crawler.entity.VersionInfo;
import crawler.playStore.crawler.entity.compositekey.VersionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VersionInfoRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(VersionInfo versionInfo){
        versionInfo.setCreatedDate();
        em.persist(versionInfo);
    }

    public VersionInfo findById(VersionId versionId){
        return em.find(VersionInfo.class, versionId);
    }

    public List<VersionInfo> findAll(){
        return em.createQuery("select v from VersionInfo v", VersionInfo.class).getResultList();
    }

    public List<VersionInfo> findByName(String name){
        return em.createQuery("select v from VersionInfo v where v.versionId.appName = :name", VersionInfo.class).setParameter("name", name).getResultList();
    }


}
