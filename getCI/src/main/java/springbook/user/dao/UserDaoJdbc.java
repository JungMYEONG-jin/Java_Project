package springbook.user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.exception.DuplicateUserIdException;
import springbook.exception.ErrorCode;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;

    private SqlService sqlService;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin((rs.getInt("login")));
            user.setRecommend((rs.getInt("recommend")));
            user.setMail(rs.getString("mail"));
            return user;
        }
    };

    public SqlService getSqlService() {
        return sqlService;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void add(User user) {
            this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),
                    user.getId(), user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getMail());
    }






    public void deleteAll()
    {
        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }





    public int getCount()
    {
        // queryForInt is deprecated
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
    }



    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[]{id},
                this.userRowMapper);
    }

    public List<User> getAll()
    {
        return this.jdbcTemplate.query("select * from users order by id", this.userRowMapper);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
                user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getMail(), user.getId());

    }

}
