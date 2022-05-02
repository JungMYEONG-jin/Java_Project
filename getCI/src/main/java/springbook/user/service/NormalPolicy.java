package springbook.user.service;

import springbook.user.domain.Level;
import springbook.user.domain.User;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

public class NormalPolicy implements UserLevelUpgradePolicy{
    @Override
    public boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level)
        {
            case BASIC: return user.getLogin()>=0;
            case SILVER: return user.getRecommend()>=0;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : "+level);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
