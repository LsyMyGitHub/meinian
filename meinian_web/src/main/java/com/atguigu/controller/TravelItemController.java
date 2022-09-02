package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.util.ManifestEntryVerifier;

import java.util.List;

/**
 * 控制模块，调用服务模块，用 @Reference进行远程调用
 *
 * @author lin
 * @create 2022-07-22 12:26
 */
@RestController
@RequestMapping("/pages/travelItem")
public class TravelItemController {
    @Reference
    private TravelItemService travelItemService;

    /**
     * 新增功能
     *
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    //前端传输过来的是一个完整对象，而且是以json形式传输的就需要用RequestBody
    public Result add(@RequestBody TravelItem travelItem) {

        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }

    }


    /**
     * 进行分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = travelItemService.findPage(queryPageBean);


        return pageResult;

    }

    /**
     * 删除操作
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(Integer id) {
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }

    }

    /**
     * 回显表单操作
     */
    @RequestMapping("/getById")
    public Result getById(Integer id) {
        try {
            TravelItem travelItem = travelItemService.getById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);

    }

    /**
     * 开始编辑操作
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result edit(@RequestBody TravelItem travelItem) {
        try {
            travelItemService.edit(travelItem);
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
    }

    /**
     * 查询所有自由行
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<TravelItem> travelItem = travelItemService.findAll();
        return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
    }
}
