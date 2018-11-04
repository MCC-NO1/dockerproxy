package com.huya.docker.service;

public interface  UserService {
    boolean hasAuth(String username, String password);
}
