package kakao.getCI.springbook.user.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("delete from users");
        return ps;
    }
}
