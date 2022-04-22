package kakao.chapter1;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

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
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

    }

    @Test
    void sameTest()
    {
        UserDaoFactory factory = new UserDaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("dao1 = " + dao1);
        System.out.println("dao2 = " + dao2);


        ApplicationContext context = new AnnotationConfigApplicationContext(UserDaoFactory.class);
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println("dao3 = " + dao3);
        System.out.println(dao4);
    }

    @Test
    void xmlBasedAddTest() throws SQLException, ClassNotFoundException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setPassword("onc");
        user.setName("pk");
        user.setId("ak4");

        userDao.add(user);

        System.out.println("user 등록 성공 " + user.getId());

        User findUser = userDao.findById(user.getId());

        //then
        Assertions.assertThat(user.getId()).isEqualTo(findUser.getId());
        Assertions.assertThat(user.getName()).isEqualTo(findUser.getName());
        Assertions.assertThat(user.getPassword()).isEqualTo(findUser.getPassword());


    }
}
