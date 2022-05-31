package study.datajpa.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator run");
        String result = component.operation();
        String deco = "***" + result + "***";
        log.info("꾸미기전={}, 꾸민후={}", result, deco);
        return deco;
    }
}
