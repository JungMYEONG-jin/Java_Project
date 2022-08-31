package com.spring.vol2.chapter1.di.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataConfig.class) // 해당 클래스의 Configuration 추가
public class AppConfig {
}
