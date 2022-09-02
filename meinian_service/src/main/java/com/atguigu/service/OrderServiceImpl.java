package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.util.DateUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * if结构不雅超过两层
 * @author lin
 * @create 2022-07-31 16:14
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    //这要跟三个表进行代表编写
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result saveOrder(Map map) throws Exception {
        String telephone = String.valueOf(map.get("telephone"));//电话号码
        String orderDate = String.valueOf(map.get("orderDate"));//日期
        String name = String.valueOf(map.get("name"));//预约人姓名
        String sex = String.valueOf(map.get("sex"));//性别
        String idCard = String.valueOf(map.get("idCard"));//身份证号
        Integer setmealId = Integer.valueOf(String.valueOf(map.get("setmealId")));
        //将日期装成日期类型
        Date date = DateUtils.parseString2Date(orderDate);


        /**
         * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
         * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
         * 3.  检查是否是会员,如果不是会员就进行注册,如果是会员就进行  步骤4
         * 4.检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
         * 5、预约成功，更新当日的已预约人数
         */

//         1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

        //通过日期看能否查询出数据
        OrderSetting orderSetting = orderSettingDao.getDateCount(date);

        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        } else {
            //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
            //因为到这里说明orderSetting不是空,他是有数据的
            int reservations = orderSetting.getReservations();//已预约人数
            int number = orderSetting.getNumber();//最多可预约人数

            if (reservations >= number) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }
        //3.检查是否是会员
        //注册会员要输入手机号，我们可以根据手机号来查询出是否注册过
        Member member = memberDao.getByTelephone(telephone);
        if (member != null) {
            //4.检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
            //说明他是会员
            //那我们就查询他是否会有重复预约(通过三个参数去查找，MemberId,预约时间(orderDate),setmealId),如果能查到结果，说明已经预约过了
            Integer memberId = member.getId();


            Order order =orderDao.getByMIdDateSId(memberId,date,setmealId);
            if(order!=null){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        } else {
             member=new Member();
            //4.如果不是会员就进行注册
            //说明没有注册会员，那我们就帮他注册会员，并且帮他预约
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setName(name);
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberDao.add(member);//主键回显
        }
        //到这里了，就进行预约
        //两步：
        //1.要把OrderSetting里面的reservations+1
        //2.进行预约
        int reservations = orderSetting.getReservations();
        orderSetting.setReservations(reservations+1);
        //进行更新操作，跟新reservations
        orderSettingDao.editReservations(orderSetting);

        String orderType = Order.ORDERTYPE_WEIXIN;
        Order order=new Order(member.getId(),date,orderType,Order.ORDERSTATUS_NO,setmealId);
        orderDao.add(order);//主键回显
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map<String,Object> map=orderDao.findById(id);
        Date date = (Date)map.get("orderDate");
        map.put("orderDate",DateUtils.parseDate2String(date));
        return map;
    }


}


