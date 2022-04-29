package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void uprgradeLevels()
    {
        List<User> all = userDao.getAll();
        for (User user : all) {
            if(canUpgradeLevel(user))
                upgradeLevel(user);
        }

    }

    private void upgradeLevel(User user) {
        user.upgradeLevel(); // user에서 하도록 처리
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level)
        {
            case BASIC: return user.getLogin()>=50;
            case SILVER: return user.getRecommend()>=30;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : "+level);
        }
    }

    public void add(User user) {
        if(user.getLevel()==null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
