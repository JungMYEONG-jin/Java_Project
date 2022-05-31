package study.datajpa.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.pureproxy.decorator.code.*;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecoratorTest(){
        Component realComp = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComp);
        client.execute();
    }

    @Test
    void decoratorTest(){
        Component messageComp = new MessageDecorator(new RealComponent());
        // client -> messageDecorator -> realComponent 의존관계 만들고 실행
        DecoratorPatternClient client = new DecoratorPatternClient(messageComp);
        client.execute();
    }

    // 2개의 데코레이터 추가..
    @Test
    void decoratorChainTest(){
        Component timeComp = new TimeDecorator(new MessageDecorator(new RealComponent()));
        DecoratorPatternClient client = new DecoratorPatternClient(timeComp);
        client.execute();
    }

    @Test
    void decoratorChainEasyTest(){
        Component realComp = new RealComponent();
        Component messageComp = new MessageDecorator(realComp);
        Component timeComp = new TimeDecorator(messageComp);
        DecoratorPatternClient client = new DecoratorPatternClient(timeComp);
        client.execute();
    }
}
