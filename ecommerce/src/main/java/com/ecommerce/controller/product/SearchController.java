package com.ecommerce.controller.product;

import com.ecommerce.entity.product.dto.ProductDto;
import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping
    public String searchResultForm(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "false", name = "rocket") boolean rocket, @AuthenticationPrincipal Principal principal,
                                   Model model, @PageableDefault(size = 60, sort = "price", direction = Sort.Direction.ASC)Pageable pageable){
        if (principal != null)
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        Page<ProductDto> productDtos = productService.getProductPageBySearch(keyword, rocket, pageable);
        model.addAttribute("productPage", productDtos);
        model.addAttribute("sort", pageable.getSort().stream().iterator().next().getProperty());
        model.addAttribute("direction", pageable.getSort().stream().iterator().next().getDirection());
        model.addAttribute("rocket", rocket);
        model.addAttribute("keyword", keyword);
        System.out.println(pageable.getSort().stream().iterator().next().getProperty());
        return "searchProduct";
    }
}
