package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lin
 * @create 2022-08-04 10:46
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    @RequestMapping("/findAllMaps")
    /**
     * 查询所有分公司地址
     */
    public Result findAllMaps() {
        try {
            List<Address> addressList = addressService.findAllMaps();
            return new Result(true, "查询所有分公司地址成功", addressList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询所有分公司地址失败");
        }
    }

    /**
     * 分页查询所有分公司地址，以表格的形式，展示
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = addressService.fingPage(queryPageBean);
        return pageResult;

    }

    /**
     * 添加功能
     */
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address) {
        try {
            addressService.addAddress(address);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/deleteById")
    /**
     * 删除地址
     */
    public Result deleteById(Integer id) {
        try {
            addressService.deleteById(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
}
