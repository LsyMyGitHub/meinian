package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lin
 * @create 2022-08-02 14:06
 */
@RestController
@RequestMapping("/user")
public class UsernameController {

    /**
     * 获取用户名,并且存入session域(框架会帮我们存)
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try{
            //框架认证成功，会将用户信息放在session域中，并同时将用户信息绑定到当前线程上。

            //可以通过SecurityContex来获取认证信息
            //Authentication 认证对象
            //Principal 认证主体 ，其实就是自己的User对象
            org.springframework.security.core.userdetails.User user =
                    (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

}
