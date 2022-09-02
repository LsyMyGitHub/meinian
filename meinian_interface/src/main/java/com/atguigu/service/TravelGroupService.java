package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

/**
 * @author lin
 * @create 2022-07-24 19:16
 */
public interface TravelGroupService {
    void add(Integer[] travelItemIds, TravelGroup travelGroup);

    PageResult findPage(QueryPageBean queryPageBean);

    TravelGroup getById(Integer id);

    List<Integer> byTravelGroupIdGetTravelItemId(Integer id);

    void edit(Integer[] travelItemIds, TravelGroup travelGroup);



    void deleteGroup(Integer id);

    List<TravelGroup> findAll();
}
