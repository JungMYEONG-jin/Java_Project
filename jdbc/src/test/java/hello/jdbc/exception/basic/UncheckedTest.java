package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {


    @Test
    void uncheckedCatch() {
        Service service = new Service();
        service.callCatch();
    }


    @Test
    void unCheckThrow() {
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyUncheckedException.class);
    }

    /**
     * 언체크예외
     */
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message){
            super(message);
        }
    }


    static class Service{
        Repository repository = new Repository();
        /**
         * 예외를 잡거나 던지는걸 생략해도됨!
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                //
                log.info("예외 처리 message={}", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        public void callThrow() {
            repository.call();
        }


    }

    /**
     * throw 생략 가능
     */
    static class Repository{
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }
}
