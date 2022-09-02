package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-24 19:17
 */
public interface TravelGroupDao {

    /**
     * 这个是解决一对多映射关系的方法
     * @param hashMap
     */
    void setTravelGroupAndTravelItem(Map<String, Integer> hashMap);

    void add(TravelGroup travelGroup);


    Page<TravelGroup> findPage(@Param("queryString") String queryString);

    TravelGroup getById(Integer id);

    List<Integer> byTravelGroupIdGetTravelItemId(Integer id);

    void delete(Integer travelGroupId);

    void edit(TravelGroup travelGroup);

    void deleteGroup(Integer id);

    List<TravelGroup> findAll();


    /**
     * 这个id是套餐的id
     * 根据套餐的id查询多个跟团游
     * @param id
     * @return
     */
    List<TravelGroup> findTravelGroupById(Integer id);
}
