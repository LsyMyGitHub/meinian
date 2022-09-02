package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author lin
 * @create 2022-08-01 12:31
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 进行登录功能
     *
     *  先吧输入的验证码和Redis的验证码进行比较，如果是一样的在进行下一步操作
     *
     * 检查是否进行注册过，通过手机号码检查
     *    //然后通过手机号查询一下，看这个手机看有没有注册
     *    //如果注册了就直接登录，
     *    //如果没有注册就帮他快速注册并且登录，然后还有设置30天的cookie自动登录
     * @return
     */
    @RequestMapping("/check")

    public Result check(@RequestBody Map map, HttpServletResponse response){
        System.out.println("map = " + map);
        String validateCode = String.valueOf(map.get("validateCode"));//验证码我输入的
        String telephone = String.valueOf(map.get("telephone"));//手机号码

        //取出验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        //验证码为空或者不对
        if(code==null||!code.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else{
            //到这里就进行登录
            Member member=memberService.login(telephone);
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）

            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS,member);
        }


    }
}
