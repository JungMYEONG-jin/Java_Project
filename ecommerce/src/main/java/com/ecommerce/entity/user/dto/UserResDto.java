package com.ecommerce.entity.user.dto;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
@Data
public class UserResDto {

    private Long id;
    private String email;
    private String username;
    private String phoneNumber;
    private boolean rocketMembership;

    public static UserResDto of(User user){
        return ModelMapperUtils.getModelMapper().map(user, UserResDto.class);
    }
}
