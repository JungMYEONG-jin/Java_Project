package study.datajpa.pureproxy.proxy.common.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteService {
    public void call(){
        log.info("Concrete Service CALL");
    }
}
