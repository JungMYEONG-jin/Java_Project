package com.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMRespDto<T>{

    private int code; // 1 success, -1 fail
    private String message; //
    private T data;
}
