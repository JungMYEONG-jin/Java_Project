package kakao.learningtest;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * junit 학습 테스트
 * 테스트 마다 새로운 오브젝트를 만든다.
 * 따라서 같지 않아야 성공임.
 */

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/junitContent.xml")
public class JUnitTest {
    static JUnitTest testObject;
    static Set<JUnitTest> testSet = new HashSet<>();

    @Test
    void test1()
    {
        assertThat(testSet).doesNotContain(this);
        testSet.add(this);
    }

    @Test
    void test2()
    {
        assertThat(testSet).doesNotContain(this);
        testSet.add(this);
    }

    @Test
    void test3()
    {
        assertThat(testSet).doesNotContain(this);
        testSet.add(this);

    }

}
