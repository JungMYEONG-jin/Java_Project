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
import java.util.List;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/applicationContext.xml")
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

        User findUser = userDao.get(user.getId());

        //then
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getName()).isEqualTo(findUser.getName());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());

    }





    @Test
    public void jdbcTemplateTest() throws SQLException, ClassNotFoundException {
        User user = new User("karena", "kome", "gkkgk12");
        User user2 = new User("jonsu", "hamburger", "chick");

        userDao.add(user);
        userDao.add(user2);

        int count_jdbcTemplate = userDao.getCount_jdbcTemplate();
        int count_jdbcTemplateV2 = userDao.getCount();
        assertThat(count_jdbcTemplate).isEqualTo(2);
        assertThat(count_jdbcTemplateV2).isEqualTo(2);

        userDao.deleteAll_jdbcTemplate_inner();

        int cnt = userDao.getCount();
        assertThat(cnt).isEqualTo(0);
    }

    @Test
    public void jdbcTemplateGetTest() throws SQLException, ClassNotFoundException {
        User user = new User("karena", "kome", "gkkgk12");
        userDao.add(user);


        User findUser = userDao.get(user.getId());
        assertThat(findUser.getName()).isEqualTo(user.getName());
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void GetAllTest() throws SQLException, ClassNotFoundException {
        User user = new User("0111", "komt", "gkkgk12");
        userDao.add(user);
        User user2 = new User("022", "komet", "gkkgk12");
        userDao.add(user2);
        User user3 = new User("01113", "pome", "gkkgk12");
        userDao.add(user3);


        List<User> all = userDao.getAll();
        for (User user1 : all) {
            System.out.println("user = " + user1.getId());
        }

        assertThat(all.size()).isEqualTo(3);

        userDao.deleteAll_jdbcTemplate_inner();

        List<User> res = userDao.getAll();

        assertThat(res.size()).isEqualTo(0);
    }


}
