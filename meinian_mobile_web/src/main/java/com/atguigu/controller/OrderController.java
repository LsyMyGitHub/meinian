package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理订单的
 *
 * @author lin
 * @create 2022-07-31 16:13
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 接受预约数据，进行处理
     * 由于传过来的数据，比较乱，不能用实体类对象接受，所有用mao集合来
     * <p>
     * map无敌
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        try {
//            System.out.println("map = " + map);
            //取出我们的 手机号码telephone
            //和验证码 validateCode
            String telephone = String.valueOf(map.get("telephone"));
            String validateCode = String.valueOf(map.get("validateCode"));
            //从我们的redis里面去除校验码，和我们床过来的比对，
            //通过手机号码可以取出校验码
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

            //如果验证码过时了，或者输入不对就返回错误
            if (code == null || !code.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            } else {

                Result result=orderService.saveOrder(map);

                return result;

            }


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map=new HashMap();
             map=orderService.findById(id);
            System.out.println(map);
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }
}
