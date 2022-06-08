package crawler.playStore.crawler.controller;

import crawler.playStore.crawler.entity.App;
import crawler.playStore.crawler.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    @GetMapping("/request")
    public String requestInfo(@RequestParam("appId") Long id)
    {
        return appService.findById(id).toString();
    }

    @GetMapping("/request/all")
    public ResponseEntity<List<App>> requestAll(){
        return new ResponseEntity<>(appService.findAllAppInfos(), HttpStatus.OK);
    }
}
