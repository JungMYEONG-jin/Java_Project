package kakao.getCI.springbook.user.service;

import kakao.getCI.springbook.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import kakao.getCI.springbook.user.domain.Level;
import kakao.getCI.springbook.user.domain.User;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public void upgradeLevels()
    {
        List<User> all = userDao.getAll();
        for (User user : all) {
            if (canUpgradeLevel(user))
                upgradeLevel(user);
        }
    }


    private void sendUpgradeEMail(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getMail());
        mailMessage.setFrom("j67310@gmail.com");
        mailMessage.setSubject("Upgrade Level Information");
        mailMessage.setText("Your Gradle is "+user.getLevel().name());
        System.out.println(mailMessage.getText().toString());

        this.mailSender.send(mailMessage);
    }



    protected void upgradeLevel(User user) {
        user.upgradeLevel(); // user에서 하도록 처리
//        policy.upgradeLevel(user); // 정책에서 처리
        userDao.update(user);
        sendUpgradeEMail(user);
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

    @Override
    public void add(User user) {
        if(user.getLevel()==null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }


    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
