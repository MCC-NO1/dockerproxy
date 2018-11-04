package com.huya.docker.service.impl;

import com.huya.docker.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, InitializingBean{
    private List<Map<String, String>> users = new ArrayList<Map<String, String>>();

    @Override
    public void afterPropertiesSet() throws Exception {
        users.add(addUser("dw_zengzhenzhong", "Ab123456"));
        users.add(addUser("dw_zengyongming", "Ab123456"));
        users.add(addUser("dw_mochencheng", "Ab123456"));
        users.add(addUser("mochencheng", "mochencheng"));
    }

    private Map<String, String> addUser(String username, String password){
        Map<String, String> result = new HashMap<String, String>();
        result.put(username, password);
        return result;
    }

    @Override
    public boolean hasAuth(String username, String password) {
        boolean result = false;
        for(Map<String, String> user : users){
            String passwd = user.get(username);
            if(!StringUtils.isEmpty(passwd) && passwd.equals(password)){
                result = true;
                break;
            }
        }
        return result;
    }
}
