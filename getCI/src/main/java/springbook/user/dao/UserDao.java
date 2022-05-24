package springbook.user.dao;

import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();

    void deleteAll_jdbcTemplate_inner();

    void add_sqlException(User user);

    int getCount_jdbcTemplate();

    void setDataSource(DataSource dataSource);

    void update(User user);

    SqlService getSqlService();
}
