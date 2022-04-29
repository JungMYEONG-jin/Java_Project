package kakao.chapter1;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import springbook.exception.DuplicateUserIdException;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoFactory;
import springbook.user.domain.Level;
import springbook.user.domain.User;

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
        user.setLevel(Level.SILVER);
        user.setLogin(0);
        user.setRecommend(11);
        userDao.add(user);

        User findUser = userDao.get(user.getId());

        //then
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getName()).isEqualTo(findUser.getName());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());

    }

    /**
     * duplicate
     */
    @Test
    void dupTest()
    {
        User user = new User();
        user.setPassword("bak");
        user.setName("p343k");
        user.setId("a333k4");
        user.setLevel(Level.SILVER);
        user.setLogin(0);
        user.setRecommend(11);

        userDao.add_sqlException(user);
        User user2 = new User();
        user2.setPassword("bak");
        user2.setName("p343k");
        user2.setId("a333k4");
        user2.setLevel(Level.SILVER);
        user2.setLogin(0);
        user2.setRecommend(11);

        Assertions.assertThrows(DuplicateUserIdException.class, ()->{userDao.add_sqlException(user2);});
    }

    @Test
    void dupTestBySpring()
    {
        User user = new User();
        user.setPassword("bak");
        user.setName("p343k");
        user.setId("a333k4");
        user.setLevel(Level.SILVER);
        user.setLogin(0);
        user.setRecommend(11);

        userDao.add(user);

        Assertions.assertThrows(DataAccessException.class, ()->{userDao.add(user);});
    }





    @Test
    public void jdbcTemplateTest() throws SQLException, ClassNotFoundException {
        User user = new User("karena", "kome", "gkkgk12", Level.BASIC, 1, 0);
        User user2 = new User("jonsu", "hamburger", "chick", Level.GOLD, 1, 0);

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
        User user = new User("karena", "kome", "gkkgk12", Level.SILVER, 2, 0);
        userDao.add(user);


        User findUser = userDao.get(user.getId());
        assertThat(findUser.getName()).isEqualTo(user.getName());
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void GetAllTest() throws SQLException, ClassNotFoundException {
        User user = new User("0111", "komt", "gkkgk12", Level.GOLD, 3, 0);
        userDao.add(user);
        User user2 = new User("022", "komet", "gkkgk12", Level.BASIC, 1, 0);
        userDao.add(user2);
        User user3 = new User("01113", "pome", "gkkgk12", Level.SILVER, 2, 0);
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

    @Test
    public void updateTest()
    {
        User user = new User("0111", "komt", "gkkgk12", Level.GOLD, 3, 0);
        User user2 = new User("1234", "komet", "g322", Level.BASIC, 1, 10);
        userDao.add(user);
        userDao.add(user2);
        user.setRecommend(33);
        user.setLogin(33);
        user.setLevel(Level.BASIC);
        user.setName("mj");

        userDao.update(user);

        User findUser = userDao.get(user.getId());
        assertThat(findUser.getName()).isEqualTo("mj");

        // user2는 바뀌면 안됨

        User findUser2 = userDao.get(user2.getId());
        assertThat(findUser2.getName()).isEqualTo("komet");

    }




}
