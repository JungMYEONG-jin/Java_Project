package com.ecommerce.service.user;

import com.ecommerce.common.utils.ModelMapperUtils;
import com.ecommerce.entity.user.MemberAddress;
import com.ecommerce.entity.user.User;
import com.ecommerce.entity.user.dto.MemberAddressDto;
import com.ecommerce.repository.user.MemberAddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAddressService {

  private final MemberAddressRepository memberAddressRepository;


  // member address dto 로 변환해서 넘김
  public List<MemberAddressDto> getAllAddress(User user){
      List<MemberAddress> result = memberAddressRepository.findAllByUserIdOrderByMainDesc(user.getId());
      return result.stream().map(MemberAddressDto::of).collect(Collectors.toList());
  }

  public MemberAddressDto getAddress(User user, Long addressId){

      MemberAddress memberAddress = memberAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new RuntimeException("해당 유저와 대응하는 주소 정보가 존재하지 않습니다."));
      return MemberAddressDto.of(memberAddress);
  }

  public MemberAddressDto createAddress(User user,  MemberAddressDto request){
      MemberAddress memberAddress = ModelMapperUtils.getModelMapper().map(request, MemberAddress.class);
      memberAddress.setUser(user);
      memberAddressRepository.save(memberAddress);
      return MemberAddressDto.of(memberAddress);
  }

  public MemberAddressDto updateAddress(User user, MemberAddressDto request, Long addressId){
      ModelMapper modelMapper = ModelMapperUtils.getModelMapper();
      MemberAddress memberAddress = memberAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new RuntimeException("유저 정보와 대응하는 주소가 존재하지 않습니다."));
      // 존재하면
      MemberAddress newAddress = modelMapper.map(request, MemberAddress.class);
      modelMapper.map(newAddress, memberAddress);
      if(memberAddress.isMain()){
          defaultAddressChange(user);
      }
      memberAddressRepository.save(memberAddress);
      return MemberAddressDto.of(memberAddress);
  }

  public void defaultAddressChange(User user){
      MemberAddress memberAddress = memberAddressRepository.findByUserIdAndMain(user.getId(), true).orElse(null);
      if(memberAddress == null)
          return;
      memberAddress.setMain(false); // 기존 main false
      memberAddressRepository.save(memberAddress);
  }

  public MemberAddressDto getMainAddress(User user){
      MemberAddress memberAddress = memberAddressRepository.findByUserIdAndMain(user.getId(), true).orElse(null);
      if(memberAddress == null){
          return null;
      }
      return MemberAddressDto.of(memberAddress);
  }

  public void deleteAddress(User user, Long addressId){
      memberAddressRepository.deleteByIdAndUserId(addressId, user.getId());
  }



}
