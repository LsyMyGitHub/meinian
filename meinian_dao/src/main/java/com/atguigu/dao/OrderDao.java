package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-31 16:17
 */
public interface OrderDao {
    Order getByMIdDateSId(@Param("memberId") Integer memberId, @Param("date") Date date, @Param("setmealId") Integer setmealId);

    void add(Order order);




    Map<String,Object> findById(Integer id);


    int getTodayOrderNumber(String today);

    int getTodayVisitsNumber(String today);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    List<Map<String, Object>> findHotSetmeal();
}
