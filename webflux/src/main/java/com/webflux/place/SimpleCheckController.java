package com.webflux.place;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class SimpleCheckController {

    @GetMapping("/example")
    public ResponseEntity<String> checkCORS(){
        return new ResponseEntity<>("WORKING!!!", HttpStatus.OK);
    }
}
