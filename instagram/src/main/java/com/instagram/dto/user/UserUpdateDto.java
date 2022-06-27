package com.instagram.dto.user;

import com.instagram.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateDto {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
    private String website;
    private String bio; // 소개
    private String phone;
    private String gender;

    public User toUser(){
        return User.builder().name(name).password(password)
                .website(website).bio(bio).phone(phone).gender(gender).build();
    }
}
