package com.ecommerce.service.product;

import com.ecommerce.entity.product.Category;
import com.ecommerce.repository.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getChildCategory(Long categoryId){

        Queue<Category> cq = new LinkedList<>();
        cq.addAll(categoryRepository.findAllByParentId(categoryId));
        List<Category> list = new ArrayList<>();
        if(cq.isEmpty()){
            cq.add(categoryRepository.findById(categoryId).orElse(null));
        }

        while(!cq.isEmpty()){
            Category category = cq.poll();
            List<Category> allByParentId = categoryRepository.findAllByParentId(category.getId());
            if(allByParentId.isEmpty())
                list.add(category);
            else
                list.addAll(allByParentId);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllParentCategory(Long categoryId){
        List<Category> list = new ArrayList<>();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null)
            return list;
        list.add(category);

        while(category.getParent() != null){
            category = categoryRepository.findById(category.getParent().getId()).orElse(null);
            if(category == null)
                break;
            list.add(category);
        }
        Collections.reverse(list);
        return list;
    }



}
