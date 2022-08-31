package com.spring.vol2.chapter3.v2;

import java.util.Map;

public class HelloControllerV2 implements SimpleControllerV2{
    @ViewName("/WEB_INF/view/hello.jsp")
    @RequiredParams({"name"})
    @Override
    public void control(Map<String, String> params, Map<String, Object> model) {
        model.put("message", "Hello "+params.get("name"));
    }
}
