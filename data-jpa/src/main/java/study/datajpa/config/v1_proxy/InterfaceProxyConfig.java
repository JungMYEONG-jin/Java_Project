package study.datajpa.config.v1_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.datajpa.proxy.app.v1.*;
import study.datajpa.trace.logtrace.LogTrace;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace trace){
        OrderControllerV1Impl controller = new OrderControllerV1Impl(orderService(trace));
        return new OrderControllerInterfaceProxy(controller, trace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace trace){
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(trace));
        return new OrderServiceInterfaceProxy(serviceImpl, trace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace trace){
        OrderRepositoryV1Impl repositoryImpl1 = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl1, trace);
    }
}
