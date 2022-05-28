package hello.advanced.trace.threadloacl;


import hello.advanced.trace.threadloacl.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService service = new ThreadLocalService();

    // 동시성 test
    @Test
    void field(){
        log.info("main start");
        Runnable userA = () -> {
            service.logic("userA");
        };
        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("ThreadA");
        Thread threadB = new Thread(userB);
        threadB.setName("ThreadB");

        threadA.start();
//        sleep(2000); // 동시성X
        sleep(100); // 동시성 발생
        threadB.start();

        sleep(3000); // main thread waiting
        log.info("main exit");

    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
