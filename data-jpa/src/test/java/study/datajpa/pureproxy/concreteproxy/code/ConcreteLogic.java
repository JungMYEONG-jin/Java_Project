package study.datajpa.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteLogic {

    public String operation(){
        log.info("concreate Logic run");
        return "data";
    }
}

