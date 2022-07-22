package com.ecommerce.controller.user;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.MemberAddressDto;
import com.ecommerce.service.user.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class MemberAddressApiController {

    private final MemberAddressService memberAddressService;

    @GetMapping
    public ResponseEntity<List<MemberAddressDto>> getAllAddresses(@AuthenticationPrincipal Principal principal){
        List<MemberAddressDto> allAddress = memberAddressService.getAllAddress(principal.getUser());
        return ResponseEntity.ok(allAddress);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> getAddress(@AuthenticationPrincipal Principal principal, @PathVariable Long addressId){
        MemberAddressDto address = memberAddressService.getAddress(principal.getUser(), addressId);
        return ResponseEntity.ok(address);
    }

   @PostMapping
   public ResponseEntity<MemberAddressDto> createAddress(@AuthenticationPrincipal Principal principal, @Valid @RequestBody MemberAddressDto request, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RuntimeException("MemberAddressApiController PostMapping error!!!");
        }
       MemberAddressDto address = memberAddressService.createAddress(principal.getUser(), request);
        return ResponseEntity.ok(address);
   }

   @PutMapping("/{addressId}")
    public ResponseEntity<MemberAddressDto> updateAddress(@AuthenticationPrincipal Principal principal, @RequestBody MemberAddressDto request, @PathVariable Long addressId){
       MemberAddressDto addressDto = memberAddressService.updateAddress(principal.getUser(), request, addressId);
       return ResponseEntity.ok(addressDto);
   }

   @DeleteMapping("/{addressId}")
    public HttpStatus deleteAddress(@AuthenticationPrincipal Principal principal, @PathVariable Long addressId){
        memberAddressService.deleteAddress(principal.getUser(), addressId);
        return HttpStatus.NO_CONTENT;
   }


}
