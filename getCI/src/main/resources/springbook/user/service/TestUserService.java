package kakao.getCI.springbook.user.service;

import kakao.getCI.springbook.user.domain.User;
import kakao.getCI.springbook.exception.TestUserServiceException;

import java.util.List;

public class TestUserService extends UserServiceImpl{
    private String id = "madnite1"; // users(3).getId()

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }

    public List<User> getAll() {
        for(User user : super.getAll()) {
            super.update(user);
        }
        return null;
    }
}

