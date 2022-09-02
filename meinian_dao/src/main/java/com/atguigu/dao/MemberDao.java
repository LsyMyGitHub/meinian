package com.atguigu.dao;

import com.atguigu.pojo.Member;
import org.apache.ibatis.annotations.Param;

/**
 * @author lin
 * @create 2022-07-31 16:18
 */
public interface MemberDao {
    //注册会员要输入手机号，我们可以根据手机号来查询出是否注册过
    Member getByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(@Param("month") String month);

    int getTodayNewMember(String today);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String weekMonday);
}
