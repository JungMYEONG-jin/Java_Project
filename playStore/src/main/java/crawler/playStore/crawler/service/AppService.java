package crawler.playStore.crawler.service;

import crawler.playStore.crawler.entity.App;
import crawler.playStore.crawler.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppService {

    private final AppRepository appRepository;

    public Long join(App app){
        appRepository.save(app);
        return app.getId();
    }

    public List<App> findAllAppInfos(){
        return appRepository.findAll();
    }

    public App findById(Long id){
        return appRepository.findById(id);
    }






}
