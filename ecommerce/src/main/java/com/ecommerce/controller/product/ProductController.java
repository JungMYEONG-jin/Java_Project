package com.ecommerce.controller.product;

import com.ecommerce.entity.product.Category;
import com.ecommerce.entity.product.Product;
import com.ecommerce.entity.product.dto.ProductDetailDto;
import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.product.CategoryService;
import com.ecommerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{productId}")
    public String productDetailsForm(@PathVariable Long productId, @AuthenticationPrincipal Principal principal, Model model){
        if (principal!=null)
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        ProductDetailDto product = productService.getProduct(productId);

        List<Category> parentCategory = categoryService.getAllParentCategory(product.getType().getId());
        List<Product> companyProduct = productService.getCompanyProductLimit4(productId, product.getCompany().getId());

        model.addAttribute("product", product);
        model.addAttribute("categories", parentCategory);
        model.addAttribute("companyProducts", companyProduct);

        return "productDetails";
    }

    @GetMapping("/categories/{categoryId}")
    public String categoryProductForm(@PathVariable Long categoryId, @AuthenticationPrincipal Principal principal, Model model, @PageableDefault(size = 60, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        if (principal!=null)
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        List<Category> parentCategory = categoryService.getAllParentCategory(categoryId);
        model.addAttribute("productPage", productService.getCategoryProductPage(categoryId,pageable));
        model.addAttribute("categories", parentCategory);
        return "productList";
    }

    @GetMapping("/companies/{companyId}")
    public String companyProductForm(@PathVariable Long companyId, @AuthenticationPrincipal Principal principal, Model model, @PageableDefault(size = 60, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        if(principal!=null)
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        model.addAttribute("productPage", productService.getAllCompanyProductPage(companyId, pageable));
        return "productList";
    }

}
