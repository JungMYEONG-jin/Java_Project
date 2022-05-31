package study.datajpa.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.pureproxy.decorator.code.Component;
import study.datajpa.pureproxy.decorator.code.DecoratorPatternClient;
import study.datajpa.pureproxy.decorator.code.MessageDecorator;
import study.datajpa.pureproxy.decorator.code.RealComponent;

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
}
