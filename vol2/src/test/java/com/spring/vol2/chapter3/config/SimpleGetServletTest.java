package com.spring.vol2.chapter3.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimpleGetServletTest {

    @Test
    void mockTest() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
        req.addParameter("name", "Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();
        SimpleGetServlet servlet = new SimpleGetServlet();
        servlet.service(req, res);
        System.out.println("res.getContentAsString() = " + res.getContentAsString());
    }
}