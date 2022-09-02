package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lin
 * @create 2022-08-04 10:47
 */
public interface AddressDao {
    List<Address> findAllMaps();

    Page<Address> findPage(@Param("queryString")String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
