package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-26 10:04
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetMealAndTravelGroup(Map<String, Integer> hashMap);

    Page<Setmeal> findPage(String queryString);

    long findSetMealAndTravelGroupId(Integer setmealId);

    void delete(Integer setmealId);

    Setmeal getById(Integer id);

    List<Integer> bySetmealGetTravelgroupId(Integer id);

    void edit(Setmeal setmeal);

    void byStmealIdeDeleteTravelgroupIds(Integer setmealId);

    List<Setmeal> getSetmeal();

    Setmeal findAllById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> getSetmealReport();
}
