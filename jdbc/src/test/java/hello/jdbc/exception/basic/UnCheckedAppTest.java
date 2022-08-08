package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

// throw 다 제거
@Slf4j
public class UnCheckedAppTest {


    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request()).isInstanceOf(RuntimeSQLException.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try{
            controller.request();
        }catch (Exception e){
            log.info("ex", e);
        }
    }

    static class Controller{
        Service service = new Service();

        public void request(){
            service.login();
        }
    }



    static class Service{
        Repository repository = new Repository();
        NetworkClient client = new NetworkClient();

        public void login(){
            repository.call();
            client.call();
        }
    }

    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectionException("EX");
        }
    }

    static class Repository{
        public void call() {
            try{
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException{
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectionException extends RuntimeException{
        public RuntimeConnectionException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(Throwable message) {
            super(message);
        }
    }
}
