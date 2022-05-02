package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserService {

    UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void uprgradeLevels()
    {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try
        {
            List<User> all = userDao.getAll();
            for (User user : all) {
                if (canUpgradeLevel(user))
                    upgradeLevel(user);
            }

            this.transactionManager.commit(status);
        }catch(
        RuntimeException e)

        {
            this.transactionManager.rollback(status);
            throw e;
        }

}



    protected void upgradeLevel(User user) {
        user.upgradeLevel(); // user에서 하도록 처리
//        policy.upgradeLevel(user); // 정책에서 처리
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level)
        {
            case BASIC: return user.getLogin()>=MIN_LOGCOUNT_FOR_SILVER;
            case SILVER: return user.getRecommend()>=MIN_RECCOMEND_FOR_GOLD;
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
