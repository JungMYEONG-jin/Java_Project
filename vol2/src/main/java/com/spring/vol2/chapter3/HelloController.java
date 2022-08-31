package com.spring.vol2.chapter3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HelloController extends SimpleController {

    public HelloController(){
        this.setRequiredParams(new String[]{"name"});
        this.setViewName("/WEB_INF/view/hello.jsp");
    }


    @Override
    public void control(Map<String, String> params, Map<String, Object> model) throws Exception {
        model.put("message", "Hello "+params.get("name"));
    }
}
