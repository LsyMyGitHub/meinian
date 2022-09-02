package com.atguigu.dao;

import com.atguigu.pojo.Permission;

import java.util.Set;

/**
 * @author lin
 * @create 2022-08-02 11:12
 */
public interface PermissionDao {

    /**
     * 这个id是role传过来的id
     * @param roleId
     * @return
     */
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
