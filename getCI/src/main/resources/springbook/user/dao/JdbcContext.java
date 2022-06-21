package kakao.getCI.springbook.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import kakao.getCI.springbook.user.dao.strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    @Autowired
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = this.dataSource.getConnection();

            ps = strategy.makePreparedStatement(con);

            ps.executeUpdate();
        }catch (SQLException e)
        {
            throw e;
        }finally {
            if(ps!=null)
            {
                try{
                    ps.close();
                }catch(SQLException e)
                {

                }
            }

            if(con!=null)
            {
                try{
                    con.close();
                }catch (SQLException e)
                {

                }
            }
        }
    }

    public void executeSql(final String query) throws SQLException{

        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement(query);
            }
        });
    }
}
