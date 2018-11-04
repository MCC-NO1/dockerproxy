package com.huya.docker.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseData {
    List<RegistoryError> errors;

    private String token;
    private int expires_in;
    private String issued_at;
}
