package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lin
 * @create 2022-07-24 19:15
 */
@RestController
@RequestMapping("/pages/travelGroup")
public class TravelGroupController {
    @Reference
    private TravelGroupService travelGroupService;

    /**
     * 新增跟团游
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup,Integer [] travelItemIds){

        try {
            travelGroupService.add(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult=travelGroupService.findPage(queryPageBean);

        return pageResult;

    }
    /**
     * 通过id查询数据
     */
    @RequestMapping("/getById")
    public Result getById(Integer id){
        try {
            TravelGroup travelGroup= travelGroupService.getById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
    }

    /**
     * 根据组团有的id找到自由行的id
     */
    @RequestMapping("byTravelGroupIdGetTravelItemId")
    public Result byTravelGroupIdGetTravelItemId(Integer id){
        try {
            List<Integer> list=travelGroupService.byTravelGroupIdGetTravelItemId(id);
            if(list==null||list.size() ==0){
                return new Result(false,"查询自由行id失败",list);
            }else{
                return new Result(true,"查询自由行id成功",list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,"查询自由行id失败");
    }

    /**
     * 更新操作，不过这个更新操作要将中间表的数据删除掉，
     *          然后在进行更新
     * @param travelGroup
     * @param travelItemIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelGroup travelGroup,Integer [] travelItemIds){

        try {
            travelGroupService.edit(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
    }

    /**
     * 根据id删除跟团游
     * @param id
     * @return
     */
    @RequestMapping("/deleteGroup")
    public Result delete(Integer id){
        try {
            travelGroupService.deleteGroup(id);
            return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
    }

    /**
     * 查询所有跟团游的信息
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TravelGroup> travelGroup=travelGroupService.findAll();
            return new Result(true,"获取跟团游信息成功",travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"获取跟团游信息失败");

    }
}
