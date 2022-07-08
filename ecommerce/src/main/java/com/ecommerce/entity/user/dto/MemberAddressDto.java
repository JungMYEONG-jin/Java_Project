package com.ecommerce.entity.user.dto;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.Place;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class MemberAddressDto {

    private Long id;
    @NotBlank(message = "받는 사람을 입력해주세요.")
    private String receiverName;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;
    private String homePhone;
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    private boolean main;
    @NotNull(message = "장소를 선택해주세요.")
    private Place place;

    public static MemberAddressDto of(MemberAddress memberAddress){
        return ModelMapperUtils.getModelMapper().map(memberAddress, MemberAddressDto.class);
    }
}
