package com.atguigu.dao;

import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lin
 * @create 2022-07-22 12:30
 */
public interface TravelItemDao {
    void add(TravelItem travelItem);

    Page<TravelItem> findPage(@Param(value = "queryPageBean") String queryPageBean);

    void delete(Integer id);

    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    long travelItemAndTravelGroup(Integer id);


    /**
     * 这个id是跟团游的id，根据跟团游的id来查询多个自由行
     * @param id
     * @return
     */
    TravelItem findTravelItemById(Integer id);
}
