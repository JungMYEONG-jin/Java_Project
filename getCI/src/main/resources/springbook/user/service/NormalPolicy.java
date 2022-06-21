package kakao.getCI.springbook.user.service;

import kakao.getCI.springbook.user.domain.Level;
import kakao.getCI.springbook.user.domain.User;

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
