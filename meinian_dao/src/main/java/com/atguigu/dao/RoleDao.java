package com.atguigu.dao;

import com.atguigu.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author lin
 * @create 2022-08-02 11:12
 */
public interface RoleDao {

    /**
     * 这个id是用户的id
     * 我们根据这个id来找到roleId
     * @param userId
     * @return
     */
    Set<Role> findRolesByUserId(@Param("id") Integer userId);
}
