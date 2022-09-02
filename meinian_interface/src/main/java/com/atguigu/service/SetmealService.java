package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-26 10:03
 */
public interface SetmealService {
    void add(Integer[] travelgroupIds, Setmeal setmeal);

    PageResult findPage(String queryString, Integer currentPage, Integer pageSize);

    void delete(Integer setmealId);

    Setmeal getById(Integer id);

    List<Integer> bySetmealGetTravelgroupId(Integer id);

    void edit(Integer[] travelgroupIds, Setmeal setmeal);

    List<Setmeal> getSetmeal();


    Setmeal findAllById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> getSetmealReport();


}
