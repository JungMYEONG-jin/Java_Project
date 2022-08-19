package com.market.daemon.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//최악의 경우 수동 주입 해주자
@Configuration
public class MyThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setQueueCapacity(100);
        executor.setMaxPoolSize(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("MJ");
        executor.setRejectedExecutionHandler(new UserRejectHandler());
        return executor;
    }

}
