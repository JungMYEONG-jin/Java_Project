package com.ecommerce.controller.user;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.MemberAddressDto;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.user.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class MemberAddressController {

    private final MemberAddressService memberAddressService;


    @GetMapping
    public String addressManageForm(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("addressPage", "main");
        model.addAttribute("user", UserResDto.of(principal.getUser()));
        model.addAttribute("addressList", memberAddressService.getAllAddress(principal.getUser()));
        return "my/address/base";
    }

    @GetMapping("/add")
    public String addressAddForm(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("addressPage", "add");
        model.addAttribute("user", UserResDto.of(principal.getUser()));
        model.addAttribute("addressDto", new MemberAddressDto());
        return "my/address/base";
    }

    @PostMapping("/add")
    public String addressAdd(@AuthenticationPrincipal Principal principal, @ModelAttribute("addressDto") @Valid MemberAddressDto req, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("addressPage", "add");
            return "my/address/base";
        }
        memberAddressService.createAddress(principal.getUser(), req);
        return "redirect:/address";
    }

    @GetMapping("/edit/{addressId}")
    public String addressEditForm(@AuthenticationPrincipal Principal principal, Model model, @PathVariable Long addressId){
        model.addAttribute("addressPage", "edit");
        model.addAttribute("user", UserResDto.of(principal.getUser()));
        model.addAttribute("address", memberAddressService.getAddress(principal.getUser(), addressId));
        return "my/address/base";
    }

    @PostMapping("/edit/{addressId}")
    public String addressEdit(@AuthenticationPrincipal Principal principal, Model model, @PathVariable Long addressId, @ModelAttribute("address") @Valid MemberAddressDto req, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            req.setId(addressId);
            model.addAttribute("addressPage", "edit");
            return "my/address/base";
        }
        memberAddressService.updateAddress(principal.getUser(), req, addressId);
        return "redirect:/address";
    }

}
