package com.atguigu.service;

import com.atguigu.pojo.User;

/**
 * @author lin
 * @create 2022-08-02 11:09
 */
public interface UserService {
    User findByUsername(String username);
}
