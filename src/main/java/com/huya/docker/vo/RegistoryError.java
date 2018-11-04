package com.huya.docker.vo;

import lombok.Data;

@Data
public class RegistoryError {
    private String code;
    private String message;
    private String detail;
}
