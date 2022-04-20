package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/tobey", "sa", "");
        PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        if(ps!=null)
            ps.close();
        if(con!=null)
            con.close();

    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/tobey", "sa", "");

        PreparedStatement ps = con.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        con.close();

        return user;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();

        User user = new User();
        user.setId("hello");
        user.setName("spring");
        user.setPassword("1q2w3e4r!!");

        userDao.add(user);

        System.out.println(user.getId()+" success");

        User user2 = userDao.findById(user.getId());
        System.out.println("user2.getName() = " + user2.getName());
        System.out.println("user2 = " + user2.getPassword());

    }


}
