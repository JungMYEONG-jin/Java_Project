package study.datajpa.config.v6_aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import study.datajpa.config.AppV1Config;
import study.datajpa.config.AppV2Config;
import study.datajpa.config.v6_aop.aspect.LogTraceAspect;
import study.datajpa.trace.logtrace.LogTrace;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace trace){
        return new LogTraceAspect(trace);
    }
}
