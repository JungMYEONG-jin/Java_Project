package user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import springbook.config.AppContext;
import springbook.config.TestAppContext;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.sql.SQLException;

@SpringBootTest(classes = Runnable.class)
//@ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
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
    public void jdbcTemplateGetTest() throws SQLException, ClassNotFoundException {
        User user = new User("karena", "kome", "gkkgk12", Level.SILVER, 2, 0);
        userDao.add(user);


        User findUser = userDao.get(user.getId());
        assertThat(findUser.getName()).isEqualTo(user.getName());
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
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
