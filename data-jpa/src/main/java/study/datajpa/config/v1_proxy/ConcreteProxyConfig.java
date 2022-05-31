package study.datajpa.config.v1_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.datajpa.proxy.app.v2.*;
import study.datajpa.trace.logtrace.LogTrace;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace trace){
        OrderControllerV2 orderControllerV2 = new OrderControllerV2(orderServiceV2(trace));
        return new OrderControllerConcreteProxy(orderControllerV2, trace);
    }


    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace trace){
        OrderServiceV2 orderServiceV2 = new OrderServiceV2(orderRepositoryV2(trace));
        return new OrderServiceConcreteProxy(orderServiceV2, trace);
    }


    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace trace){
        OrderRepositoryV2 orderRepositoryV2 = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(orderRepositoryV2, trace);
    }
}
