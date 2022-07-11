package com.ecommerce.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class AppConfig {

    @Profile("dev")
    @Configuration
    public class LocalConfig implements WebMvcConfigurer{
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/file/**")
                    .addResourceLocations("file:///")
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }
    }

    @Profile("prod")
    @Configuration
    public class ProdConfig implements WebMvcConfigurer{
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/file/**")
                    .addResourceLocations("file:/")
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }
    }
}
