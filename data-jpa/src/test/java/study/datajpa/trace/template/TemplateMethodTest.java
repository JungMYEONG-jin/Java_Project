package study.datajpa.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.trace.template.code.AbstractTemplate;
import study.datajpa.trace.template.code.SubClassLogic1;
import study.datajpa.trace.template.code.SubClassLogic2;

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

    @Test
    void templateMethodV1(){
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    @Test
    void templateMethodV2(){

        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("business 1 is running");
            }
        };
        log.info("class name1={}", template1.getClass());
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("business 2 is running");
            }
        };
        log.info("class name2={}", template2.getClass());
        template2.execute();
    }
}
