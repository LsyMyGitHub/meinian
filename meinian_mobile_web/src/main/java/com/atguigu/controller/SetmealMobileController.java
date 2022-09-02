package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @author lin
 * @create 2022-07-30 18:41
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐信息
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmeal=setmealService.getSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

    @RequestMapping("/findAllById")
    public Result findAllById(Integer id){

        try {
            Setmeal setmeal=setmealService.findAllById(id);
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }

    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){
        Setmeal setmeal=setmealService.getSetmealById(id);
        return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);

    }

}
