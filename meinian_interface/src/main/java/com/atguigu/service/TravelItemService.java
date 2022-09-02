package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lin
 * @create 2022-07-22 12:22
 */
public interface TravelItemService {

    void add(TravelItem travelItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);


    List<TravelItem> findAll();
}
