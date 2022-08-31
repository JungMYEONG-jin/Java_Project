package com.spring.vol2.chapter1.di;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
public class ArrBean {
    @Value("1,2,3,4")
    public int[] arr;
}
