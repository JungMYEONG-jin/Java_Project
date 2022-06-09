package springbook.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import kakao.getCI.springbook.Message;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class BeanTest {

    @Autowired
    ApplicationContext context;


    @Test
    void getMessageFromFactoryBean()
    {
        Object message = context.getBean("message");
        Assertions.assertThat(message.getClass()).hasSameClassAs(Message.class);
        Assertions.assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }
}
