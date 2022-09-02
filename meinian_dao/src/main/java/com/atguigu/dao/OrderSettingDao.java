package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-28 12:09
 */
public interface OrderSettingDao {
    long findOrderSettingData(Date orderDate);

    void edit(OrderSetting orderSetting);

    void addBatch(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map map);

    OrderSetting getDateCount(Date date);
// //进行更新操作，跟新reservations
    void editReservations(OrderSetting orderSetting);
}
