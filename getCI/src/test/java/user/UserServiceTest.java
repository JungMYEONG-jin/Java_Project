package user;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.NormalPolicy;
import springbook.user.service.UserService;

import javax.sql.DataSource;

import static springbook.user.service.UserService.*;


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

    @Autowired
    DataSource dataSource;

    private List<User> users;

    User user;

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

        user = new User();

    }


    @Test
    void beanTest()
    {
        assertThat(this.userService).isNotNull();
    }

    @Test
    void updateLevelTest() throws Exception
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

    @Test
    void upgradeLevel()
    {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.getNext()==null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.getNext());
        }
    }





    @Test
    void cannotUpgradeLevel()
    {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.getNext()!=null)
                continue;
            user.setLevel(level);
            Assertions.assertThrows(IllegalStateException.class, ()->{user.upgradeLevel();});
        }
    }



    @Test
    public void upgradeLevels() throws Exception {
        for (User user : users) {
            userDao.add(user);
        }

        userService.uprgradeLevels();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);
    }




    private void checkLevelUpgrade(User user, boolean upgraded) {

        User findUser = userDao.get(user.getId());
        if(upgraded)
        {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel().getNext());
        }else
        {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel());
        }

    }

    // test 대역
    static class TestUserService extends UserService
    {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException
    {

    }

    @Autowired
    PlatformTransactionManager transactionManager;


    @Test
    void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(this.transactionManager);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try{
            testUserService.uprgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e) {

        }

        checkLevelUpgrade(users.get(1), false);
    }



}
