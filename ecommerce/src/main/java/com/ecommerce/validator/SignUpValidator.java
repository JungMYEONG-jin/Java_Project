package com.ecommerce.validator;

import com.ecommerce.entity.user.dto.UserReqDto;
import com.ecommerce.service.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpValidator implements Validator {

    private final UserService userService;

    public SignUpValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserReqDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserReqDto req = (UserReqDto) target;
        if(req.getPassword() == null || !req.getPassword().equals(req.getPasswordCheck())){
            errors.rejectValue("passwordCheck", "key", "비밀번호가 일치하지 않습니다.");
        }
        if(userService.checkEmailDuplicate(req.getEmail())){
            errors.rejectValue("email", "key", "해당 email은 이미 존재합니다.");
        }
    }
}
