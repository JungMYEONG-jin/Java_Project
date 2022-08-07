package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedAppTest {


    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request()).isInstanceOf(SQLException.class);
    }

    static class Controller{
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.login();
        }
    }



    static class Service{
        Repository repository = new Repository();
        NetworkClient client = new NetworkClient();

        public void login() throws ConnectException, SQLException {
            repository.call();
            client.call();
        }
    }

    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("EX");
        }
    }

    static class Repository{
        public void call() throws SQLException{
            throw new SQLException("EX");
        }
    }
}
