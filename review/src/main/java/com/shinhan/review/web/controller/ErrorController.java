package com.shinhan.review.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("error")
public class ErrorController {

    private static final String baseDir = "error/";
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("400")
    public String handle400(HttpServletRequest request){
        return baseDir+"400";
    }

    @RequestMapping("404")
    public String handle404(HttpServletRequest request){
        return baseDir+"404";
    }

    @RequestMapping("500")
    public String handle500(HttpServletRequest request){
        return baseDir+"500";
    }

}
