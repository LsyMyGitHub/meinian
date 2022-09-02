package com.atguigu.service;

import com.atguigu.pojo.Member;

import java.util.List;

/**
 * @author lin
 * @create 2022-08-01 12:32
 */
public interface MemberService {
    Member login(String telephone);

    List<Integer> findMemberCountByMonth(List<String> months);
}
