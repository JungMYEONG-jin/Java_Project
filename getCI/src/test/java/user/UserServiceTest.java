package user;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    private List<User> users;

    @BeforeEach
    void init()
    {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("A001", "NJ", "test123", Level.BASIC, 49, 0),
                new User("A002", "Mon", "test123", Level.BASIC, 50, 0),
                new User("A003", "Son", "test123", Level.SILVER, 60, 29),
                new User("A004", "Yo", "test123", Level.SILVER, 60, 30),
                new User("A005", "Qon", "test123", Level.GOLD, 100, 100)
        );
//        userService.setUserDao(userDao);

    }


    @Test
    void beanTest()
    {
        assertThat(this.userService).isNotNull();
    }

    @Test
    void updateLevelTest()
    {

        for(User user : users)
            userDao.add(user);

        userService.uprgradeLevels();

        users = userDao.getAll();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    public void checkLevel(User user, Level level)
    {
        User findUser = userDao.get(user.getId());
        assertThat(user.getLevel()).isEqualTo(level);
    }


    @Test
    void add()
    {
        User withLevelUser = users.get(4);
        User withOutLevel = users.get(0);
        withOutLevel.setLevel(null);

        userService.add(withLevelUser);
        userService.add(withOutLevel);

        User levelUser = userDao.get(withLevelUser.getId());
        User withoutLevelUser = userDao.get(withOutLevel.getId());

        assertThat(levelUser.getLevel()).isEqualTo(Level.GOLD);
        assertThat(withoutLevelUser.getLevel()).isEqualTo(Level.BASIC);
    }



}
