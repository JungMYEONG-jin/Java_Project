package springbook.user.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection con) throws SQLException;
}
