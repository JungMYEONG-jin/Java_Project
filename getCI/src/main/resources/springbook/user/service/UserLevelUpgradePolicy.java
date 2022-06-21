package kakao.getCI.springbook.user.service;

import kakao.getCI.springbook.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);

}
