package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    @Test
    void strategyV1() {
        ContextV1 context = new ContextV1(new StrategyLogic1());
        context.execute();

        context = new ContextV1(new StrategyLogic2());
        context.execute();

    }

    private void logic1(){
        long startTime = System.currentTimeMillis();

        log.info("logic1 is running");

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }

    private void logic2(){
        long startTime = System.currentTimeMillis();

        log.info("logic2 is running");

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }


    // template method pattern
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        AbstractTemplate template2 = new SubClassLogic2();
        template1.execute();
        template2.execute();
    }


}
