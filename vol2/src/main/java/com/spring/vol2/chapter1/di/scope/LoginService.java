package com.spring.vol2.chapter1.di.scope;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

public class LoginService {

    @Autowired LoginUser loginUser;

    public void login(){
        return;
    }
}
