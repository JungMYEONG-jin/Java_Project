package springbook.user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.dao.strategy.AddStatement;
import springbook.user.dao.strategy.DeleteAllStatement;
import springbook.user.dao.strategy.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };



    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public void add(User user)
    {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    /**
     * 익명 내부클래스를 사용한 클라이언트 코드
     * @throws SQLException
     */

//    public void jdbc_deleteAll() throws SQLException
//    {
//        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
//                return con.prepareStatement("delete from users");
//            }
//        });
//    }

    /**
     * JdbcTemplate 적용한 메소드
     */
    public void deleteAll_jdbcTemplate()
    {
        this.jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        return con.prepareStatement("delete from users");
                    }
                }
        );
    }

    /**
     * 내장 콜백 사용
     */
    public void deleteAll_jdbcTemplate_inner()
    {
        this.jdbcTemplate.update("delete from users");
    }


    public void deleteAll()
    {
        this.jdbcTemplate.update("delete from users");
    }



    public int getCount_jdbcTemplate()
    {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });
    }

    public int getCount()
    {
        // queryForInt is deprecated
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }






//        Connection connection = dataSource.getConnection();
//        PreparedStatement ps = connection.prepareStatement("select count(*) from users");
//
//        ResultSet res = ps.executeQuery();



    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id},
                this.userRowMapper);
    }

    public List<User> getAll()
    {
        return this.jdbcTemplate.query("select * from users order by id", this.userRowMapper);
    }




}
