package kakao.getCI.springbook.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import kakao.getCI.springbook.user.domain.Level;
import kakao.getCI.springbook.user.domain.User;
import kakao.getCI.springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class UserDaoJdbc implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
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
