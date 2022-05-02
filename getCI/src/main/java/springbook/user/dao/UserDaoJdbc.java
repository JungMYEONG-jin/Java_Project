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

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;


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



    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void add(User user) {
            this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, mail) values(?,?,?,?,?,?,?)",
                    user.getId(), user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getMail());
    }




    public void add_sqlException(User user) throws DuplicateUserIdException
    {

        Connection connection = null;
        PreparedStatement ps = null;
        try{

//            this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
//                    user.getId(), user.getName(), user.getPassword());

            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/tobey", "sa", "");
            ps = connection.prepareStatement("insert into users(id, name, password, level, login, recommend, mail) values(?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setString(4, String.valueOf(user.getLevel().getValue()));
            ps.setString(5, String.valueOf(user.getLogin()));
            ps.setString(6, String.valueOf(user.getRecommend()));
            ps.setString(7, user.getMail());
            ps.executeUpdate();


        }catch (SQLException e)
        {
            if(e.getErrorCode()== ErrorCode.DUPLICATE_KEY_1)
                throw new DuplicateUserIdException(e); //예외 전환
            else
                throw new RuntimeException(e); // 예외 포장
        }finally {
            if(ps!=null)
            {
                try{
                    ps.close();
                }catch (SQLException e)
                {

                }
            }if(connection!=null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {

                }
            }
        }

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

    @Override
    public void update(User user) {
        this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, mail = ? where id = ?",
                user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getMail(), user.getId());

    }

}
