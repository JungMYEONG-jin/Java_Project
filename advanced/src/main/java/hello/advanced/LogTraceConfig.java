package hello.advanced;

import hello.advanced.trace.logtrace.FieldLogTrace;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    // 싱글톤인데 쓰레드는 여러개임. 여러 유저가 요청하면 여러 쓰레드가 동시에 접근해서 동시성 문제가 발생
    @Bean
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace();
    }
}
