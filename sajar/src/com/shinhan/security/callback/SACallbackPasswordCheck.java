package com.shinhan.security.callback;

public interface SACallbackPasswordCheck {
    boolean CheckPasswordValidation(String password, String authtype);
}
