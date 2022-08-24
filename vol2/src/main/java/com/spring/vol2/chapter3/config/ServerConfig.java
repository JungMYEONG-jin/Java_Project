package com.spring.vol2.chapter3.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Bean
    public ServletRegistrationBean<SomeServlet> getServletRegistrationBean(){
        ServletRegistrationBean<SomeServlet> registrationBean = new ServletRegistrationBean<>(new SomeServlet());
        registrationBean.addUrlMappings("/app/*");
        return registrationBean;
    }
}
