package study.datajpa.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0(){
        logic1();
        logic2();
    }

    private void logic1(){
        long startTime = System.currentTimeMillis();
        // business logic
        log.info("business logic1 start ");
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);

    }

    private void logic2(){
        long startTime = System.currentTimeMillis();
        // business logic
        log.info("business logic2 start ");
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);

    }
}
