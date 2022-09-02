package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lin
 * @create 2022-08-02 11:18
 */
@Component
public class SpringSecurityUserService  implements UserDetailsService {
    @Reference
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //先根据username查询user是否存在
        com.atguigu.pojo.User user=userService.findByUsername(username);

        if(user==null){
            return null;//返回一个空，框架会帮我们进行异常处理，然后抛出去
        }

        //这个是获取权限的集合
        List<GrantedAuthority> lists = new ArrayList<>();
        //先user登录->然后判断这个user是否是role->然后在获取权限
        //获取到用户
        Set<Role> roles = user.getRoles();
        if(roles==null){
            return null;//这个是判断user有没有role功能
        }
        for (Role role : roles) {
            //获取用户的权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //这里已经吧用户所有的权限都遍历出来了
                lists.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }

        }
        org.springframework.security.core.userdetails.User securityUser =
                new  org.springframework.security.core.userdetails.User(
                        user.getUsername(),user.getPassword(),lists);

        return securityUser;

    }


}
