package study.datajpa.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.trace.strategy.code.strategy.ContextV2;
import study.datajpa.trace.strategy.code.strategy.Strategy;
import study.datajpa.trace.strategy.code.strategy.StrategyLogic1;
import study.datajpa.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1(){
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(new StrategyLogic1());
        contextV2.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV2(){
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("business logic 1 is running");
            }
        });

        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("business logic 2 is running");
            }
        });
    }

    @Test
    void strategyV3(){
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(() -> log.info("business logic 1 is running"));
        contextV2.execute(() -> log.info("business logic 2 is running"));
    }
}
