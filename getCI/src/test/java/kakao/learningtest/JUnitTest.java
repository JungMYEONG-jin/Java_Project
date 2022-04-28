package kakao.learningtest;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * junit 학습 테스트
 * 테스트 마다 새로운 오브젝트를 만든다.
 * 따라서 같지 않아야 성공임.
 * 하지만 autowired로 생성시 항상 동일한 오브젝트임
 * 따라서 같아야 성공
 */

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/junitContent.xml")
public class JUnitTest {

    @Autowired
    ApplicationContext context;

    static ApplicationContext contextObject;
    static Set<JUnitTest> testSet = new HashSet<>();

    @Test
    void test1()
    {
        assertThat(testSet).doesNotContain(this);
        testSet.add(this);

        assertThat(contextObject==null || contextObject==this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    void test2()
    {
        assertThat(testSet).doesNotContain(this);
        testSet.add(this);

        org.junit.jupiter.api.Assertions.assertTrue(contextObject==null || contextObject==this.context);
        contextObject = this.context;
    }


}
