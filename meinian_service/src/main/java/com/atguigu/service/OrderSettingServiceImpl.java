package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-28 12:10
 */

@Service(interfaceClass =OrderSettingService.class )
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addBatch(List<OrderSetting> orderSettingList) {

        //判断一下，如果是第一次导入，事件数据那就直接执行增加操作，
        // 如果是在同一个时间，修改了数据，那就执行更新操作
        for (OrderSetting orderSetting : orderSettingList) {
            long count=orderSettingDao.findOrderSettingData(orderSetting.getOrderDate());
            if(count>0){
                orderSettingDao.edit(orderSetting);
            }else{
                orderSettingDao.addBatch(orderSetting);
            }


        }

    }

    @Override
//    { date: 1, number: 120, reservations: 1 },
    //        { date: 3, number: 120, reservations: 1 },
    //        { date: 4, number: 120, reservations: 120 },
    //        { date: 6, number: 120, reservations: 1 },
    //        { date: 8, number: 120, reservations: 1 }

    public List<Map> getOrderSettingByMonth(String date) {
        //开始时间
        String dateStart=date+"-"+1;
        //结束时间,这个到31是肯定没错的，如果一个月30天，也没事，也在这个范围
        String dateEnd=date+"-"+31;
        //以map集合的方式传给数据库
        Map map=new HashMap();

        map.put("dateStart",dateStart);
        map.put("dateEnd",dateEnd);

        List<OrderSetting> orderSettingList=orderSettingDao.getOrderSettingByMonth(map);

        //现在我们获取到了orderSettingList数据，但是我们要把它放在List<Map>集合里面，等于是
        //吧这个数据从这个篮子放到里一个篮子

        List<Map> mapList=new ArrayList<>();



        for (OrderSetting orderSetting : orderSettingList) {
            Map hashMap = new HashMap();
            hashMap.put("date",orderSetting.getOrderDate().getDate());
            hashMap.put("number",orderSetting.getNumber());
            hashMap.put("reservations",orderSetting.getReservations());
            mapList.add(hashMap);
        }
        return mapList;
    }

    @Override
    public void byOrderDateUpdateNum(OrderSetting orderSetting) {
        /**
         * 进行判断，如果这个时间数据库里面不存在，那我们就
         *         执行添加操作
         */
        long count = orderSettingDao.findOrderSettingData(orderSetting.getOrderDate());
        if(count>0){//说明里面有这个数据，那我们就执行跟新操作
            orderSettingDao.edit(orderSetting);
        }else{//没有数据，就执行添加操作
            orderSettingDao.addBatch(orderSetting);
        }

    }
}
