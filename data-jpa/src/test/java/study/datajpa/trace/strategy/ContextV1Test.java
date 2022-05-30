package study.datajpa.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.trace.strategy.code.strategy.ContextV1;
import study.datajpa.trace.strategy.code.strategy.Strategy;
import study.datajpa.trace.strategy.code.strategy.StrategyLogic1;
import study.datajpa.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0(){
        logic1();
        logic2();
    }

    private void logic1(){
        long startTime = System.currentTimeMillis();
        log.info("logic 1 is running");
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2(){
        long startTime = System.currentTimeMillis();
        log.info("logic 2 is running");
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }


    @Test
    void strategyV1(){
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 contextV1 = new ContextV1(strategyLogic1);
        contextV1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 contextV2 = new ContextV1(strategyLogic2);
        contextV2.execute();
    }

    @Test
    void strategyV2(){
        Strategy logic1 = new Strategy(){
            @Override
            public void call(){
                log.info("business logic1 is running");
            }
        };
        log.info("strategyLogic1={}", logic1.getClass());
        ContextV1 contextV1 = new ContextV1(logic1);
        contextV1.execute();

        Strategy logic2 = new Strategy(){
            @Override
            public void call(){
                log.info("business logic2 is running");
            }
        };

        log.info("strategyLogic2={}", logic2.getClass());
        ContextV1 contextV2 = new ContextV1(logic2);
        contextV2.execute();


    }

    @Test
    void strategyV3(){
        ContextV1 contextV1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("business logic 1 is running");
            }
        });
        contextV1.execute();


        ContextV1 contextV2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("business logic 2 is running");
            }
        });
        contextV2.execute();


    }

    @Test
    void strategyV4(){
        ContextV1 contextV1 = new ContextV1(() -> log.info("business logic 1 is running"));
        contextV1.execute();

        ContextV1 contextV2 = new ContextV1(() -> log.info("business logic 2 is running"));
        contextV2.execute();


    }



}
