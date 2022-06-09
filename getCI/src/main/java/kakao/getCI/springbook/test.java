package kakao.getCI.springbook;

import kakao.getCI.springbook.user.domain.Level;
import kakao.getCI.springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {
        List<User> users = Arrays.asList(new User("id11", "mj", "password123", Level.GOLD, 30, 20),
                new User("id12", "mk", "pass22", Level.SILVER, 0, 10));

        for (User user : users) {
            System.out.println("user = " + user);
        }
    }
}
