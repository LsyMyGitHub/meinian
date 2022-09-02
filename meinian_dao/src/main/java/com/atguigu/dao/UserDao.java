package com.atguigu.dao;

import com.atguigu.pojo.User;

/**
 * @author lin
 * @create 2022-08-02 11:12
 */
public interface UserDao {

    User findByUsername(String username);
}
