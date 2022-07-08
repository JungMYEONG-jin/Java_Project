package com.ecommerce.entity.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class UserReqDto {

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email을 입력하세요.")
    private String email;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    private String passwordCheck;

    @NotBlank(message = "핸드폰 번호를 입력하세요.")
    private String phoneNumber;
}
