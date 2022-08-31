package com.simpleauthJPA;

import com.simpleauthJPA.shinhan.security.simpleauth.message.SAMessageUtil;

import java.util.Date;

public class test {

    public static void main(String[] args) {
        System.out.println(SAMessageUtil.getDate(new Date()));
    }
}
