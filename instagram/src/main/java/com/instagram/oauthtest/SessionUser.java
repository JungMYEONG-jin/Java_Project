package com.instagram.oauthtest;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(GoogleUser googleUser) {
        this.name = googleUser.getName();
        this.email = googleUser.getEmail();
        this.picture = googleUser.getPicture();
    }
}
