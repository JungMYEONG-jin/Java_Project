package study.datajpa.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.trace.strategy.code.template.Callback;
import study.datajpa.trace.strategy.code.template.TimeLogTemplate;

import java.sql.Time;

@Slf4j
public class TemplateCallbackTest {

    @Test
    void cllabackV1(){
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("business logic 1 is running");
            }
        });

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("business logic 2 is running");
            }
        });
    }

    @Test
    void callbackV2(){
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(()->log.info("business logic 1 is running"));
        template.execute(()->log.info("business logic 2 is running"));
    }
}
