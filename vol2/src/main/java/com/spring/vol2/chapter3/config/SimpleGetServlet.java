package com.spring.vol2.chapter3.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleGetServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        resp.getWriter().print("<HTML><BODY>");
        resp.getWriter().print("Hello " + name);
        resp.getWriter().print("</BODY></HTML>");
    }
}
