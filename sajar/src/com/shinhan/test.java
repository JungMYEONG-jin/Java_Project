package com.shinhan;

import com.shinhan.security.imple.SASimpleAuthAction;

public class test {
    public static void main(String[] args) {
        SASimpleAuthAction action = new SASimpleAuthAction();
        System.out.println("action.getClass() = " + action.getClass());
    }
}
