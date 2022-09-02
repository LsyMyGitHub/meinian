package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-28 12:12
 */
public interface OrderSettingService {
    void addBatch(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String date);

    void byOrderDateUpdateNum(OrderSetting orderSetting);
}
