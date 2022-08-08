package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {


    @Test
    void checkCatch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checkThrow() {
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyCheckedException.class);
    }

    /**
     * 체크예외
     */
    static class MyCheckedException extends Exception{
        public MyCheckedException(String message){
            super(message);
        }
    }


    static class Service{
        Repository repository = new Repository();
        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                //
                log.info("예외 처리 message={}", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    /**
     * check 예외는 던지는걸 선언해야함
     */
    static class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
