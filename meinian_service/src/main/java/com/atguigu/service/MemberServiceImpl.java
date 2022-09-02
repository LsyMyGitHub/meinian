package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lin
 * @create 2022-08-01 12:33
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member login(String telephone) {
        //在这里判断业务逻辑
        /**
         * 首先通过telephone判断该用户有没有注册
         * 如果注册了，那我们就直接登录
         * 如果没有注册，那我们就先帮和他进行一个快速注册，有手机号码和注册时间
         */
        Member member = memberDao.getByTelephone(telephone);

        if(member!=null){
            //会员存在
            return member;
        }else{
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        return member;


    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list=new ArrayList<>();

        if(months!=null && months.size()>0){
            for (String month : months) {
                month=month+"-01";
                int count = memberDao.findMemberCountByMonth(month);
                list.add(count);
            }
        }
        return list;
    }
}
