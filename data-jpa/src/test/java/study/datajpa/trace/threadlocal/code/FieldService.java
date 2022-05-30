package study.datajpa.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {

    private String nameStore;

    public String logic(String name){
        log.info("save name={}, nameStore={}", name, nameStore);
        nameStore = name;
        sleep(1000);
        log.info("Search Result nameStore={}", nameStore);
        return nameStore;
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
