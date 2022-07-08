package com.ecommerce.entity.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class PasswordChangeDto {

    private String originalPassword;
    private String newPassword;
    private String newPasswordCheck; // 다시 입력해서 동일 여부 확인.
}
