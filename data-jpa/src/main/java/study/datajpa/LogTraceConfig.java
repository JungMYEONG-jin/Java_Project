package study.datajpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.datajpa.trace.logtrace.FieldLogTrace;
import study.datajpa.trace.logtrace.LogTrace;
import study.datajpa.trace.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace setLogBean(){
        return new ThreadLocalLogTrace();
    }
}
