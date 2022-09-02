package com.atguigu.controller;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 进行订单设置
 * @author lin
 * @create 2022-07-28 12:12
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 进行文件上传到数据库
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile)   {

        try {
            List<String[]> lists = POIUtils.readExcel(excelFile);

            //exl表格的数据在lists里面了，现在想办法，怎么样才可以存到数据库里面

            List<OrderSetting> orderSettingList=new ArrayList<>();

            for (String[] list : lists) {
                 String dataStr = list[0];
                 Integer numStr = Integer.valueOf(list[1]);
                 OrderSetting orderSetting=new OrderSetting(new Date(dataStr),numStr);
                 orderSettingList.add((orderSetting));
            }
            orderSettingService.addBatch(orderSettingList);
            return new Result(true,"上传excel表格成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }

        return new Result(false,"上传excel表格失败");
    }

    /**
     * 获取数据库中的时间数据
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> mapList=orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }

    /**
     * 通过时间来进行预约人数的修改
     * @return
     */
    @RequestMapping("/byOrderDateUpdateNum")
    public Result byOrderDateUpdateNum(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.byOrderDateUpdateNum(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
