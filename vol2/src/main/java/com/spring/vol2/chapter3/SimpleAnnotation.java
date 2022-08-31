package com.spring.vol2.chapter3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SimpleAnnotation {

    @GetMapping("/hello")
    public String Hello(@RequestParam("name") String name, ModelMap map){
        map.put("message", "hello "+name);
        return "/WEB_INF/view/hello.jsp";
    }

}
