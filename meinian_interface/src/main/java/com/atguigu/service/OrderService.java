package com.atguigu.service;

import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;

import java.util.Map;

/**
 * @author lin
 * @create 2022-07-31 16:14
 */
public interface OrderService {
    Result saveOrder(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
