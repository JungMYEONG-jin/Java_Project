package learningtest.jdk;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;

@SpringBootTest(classes = Runnable.class)
public class ReflectionTest {

    @Test
    void invokeMethod() throws Exception
    {

        String name = "Spring";

        Assertions.assertThat(name.length()).isEqualTo(6);
        Method lengthMethod = String.class.getMethod("length");
        Assertions.assertThat((Integer)lengthMethod.invoke(name)).isEqualTo(6);

        Assertions.assertThat(name.charAt(0)).isEqualTo('S');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        Assertions.assertThat((Character)charAtMethod.invoke(name, 0)).isEqualTo('S');


    }
}
