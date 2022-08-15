package com.spring.vol2.chapter1.di.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.time.LocalDateTime;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS) // proxyMode하면 autowired 가능
public class LoginUser {
    String loginId;
    String name;
    LocalDateTime loginTime;
}
