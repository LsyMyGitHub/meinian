package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 用户校验码的controller
 * @author lin
 * @create 2022-07-31 13:47
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用于发送短信，且把短信存入Redis里面
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String  telephone){
        try {
            //先随机获取四位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //在吧验证码和电话号码传给工具类用于发送出去

            SMSUtils.sendShortMessage(telephone,code.toString());

            //然后在把验证码存入Redis里面，方便与后面比对,且设置五分钟有效
            //三个参数，第一个是存入里面的   ，名，第二个是有效时长，第三个是验证码
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,
                                             5 * 60,code.toString());

            System.out.println("telephone="+telephone+"code = " + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }


    }

    /**
     * 快速登录验证码校验
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone)  {
        //先随机获取四位验证码
        try {
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            //吧验证码和手机号一起传到redis里面，而且设置五分钟有效期限
            SMSUtils.sendShortMessage(telephone,code.toString());
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,
                    5 * 60,code.toString());

            System.out.println("telephone="+telephone+"code = " + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
