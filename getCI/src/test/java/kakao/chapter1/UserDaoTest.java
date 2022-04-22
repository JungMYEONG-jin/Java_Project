package kakao.chapter1;

import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoFactory;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
@DirtiesContext // 테스트 메소드에서 application context 구성이나 상태를 변경한다는것을 프레임워크에 알려준다.
public class UserDaoTest {


    @Autowired
    private static UserDao userDao;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void clear() throws SQLException {

        userDao = context.getBean("userDao", UserDao.class);
//        this.userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();

    }


    @Test
    void daoTest()
    {
        UserDao userDao = new UserDaoFactory().userDao();
    }


    @Test
    void sameTest()
    {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDaoFactory factory = new UserDaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("dao1 = " + dao1);
        System.out.println("dao2 = " + dao2);


        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println("dao3 = " + dao3);
        System.out.println(dao4);
    }

    @Test
    void xmlBasedAddTest() throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setPassword("bak");
        user.setName("p343k");
        user.setId("a333k4");

        userDao.add(user);

        User findUser = userDao.findById(user.getId());

        //then
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getName()).isEqualTo(findUser.getName());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());

    }


    // 모든 테스트는 실행 순서에 상관없이 독립적으로 항상 동일한 결과를 낼 수 있어야 한다!!
    @Test
    public void getCountTest() throws SQLException, ClassNotFoundException {
        User user = new User("karena", "kome", "gkkgk12");
        User user2 = new User("jonsu", "hamburger", "chick");

        userDao.add(user);
        userDao.add(user2);

        assertThat(userDao.getCount()).isEqualTo(2);
    }

    /**
     * Empty exception이 떠야 성공하는 test
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void exceptionTest() throws SQLException, ClassNotFoundException {
        assertThat(userDao.getCount()).isEqualTo(0);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
            userDao.findById("hello");
        });
    }


}
