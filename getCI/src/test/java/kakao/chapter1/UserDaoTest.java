package kakao.chapter1;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoFactory;

@SpringBootTest(classes = Runnable.class)
public class UserDaoTest {

    @Test
    void daoTest()
    {
        UserDao userDao = new UserDaoFactory().userDao();
    }

    @Test
    void beanTest()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(UserDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

    }
}
