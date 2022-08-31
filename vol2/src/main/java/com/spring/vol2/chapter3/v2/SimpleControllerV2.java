package com.spring.vol2.chapter3.v2;

import java.util.Map;

public interface SimpleControllerV2 {
    void control(Map<String, String> params, Map<String, Object> model);
}
