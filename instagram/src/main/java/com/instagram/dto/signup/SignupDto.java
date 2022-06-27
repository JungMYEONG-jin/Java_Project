package com.instagram.dto.signup;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignupDto {

    @Size(min=1, max=20, message = "사용자 이름은 최대 20자 까지 허용합니다.")
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String name;


}
