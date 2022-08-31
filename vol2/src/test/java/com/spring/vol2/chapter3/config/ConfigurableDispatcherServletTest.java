package com.spring.vol2.chapter3.config;

import com.spring.vol2.chapter3.HelloSpring;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;

@SpringBootTest
class ConfigurableDispatcherServletTest {

    @Test
    void selfServlet() throws ServletException, IOException {
        ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
        servlet.setRelativeLocations(getClass(), "spring-servlet.xml");

        servlet.setClasses(HelloSpring.class);
        servlet.init(new MockServletConfig("spring"));

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
        req.addParameter("name", "Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();

        servlet.service(req, res);

        ModelAndView mv = servlet.getModelAndView();
        System.out.println(mv.getViewName());
        System.out.println(mv.getModel().get("message").toString());


    }
}