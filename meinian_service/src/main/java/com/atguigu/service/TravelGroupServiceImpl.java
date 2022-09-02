package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-24 19:15
 */
@Service
public class TravelGroupServiceImpl implements TravelGroupService{
    @Autowired
    private TravelGroupDao travelGroupDao;

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);
        setTravelGroupAndTravelItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //先开启分页功能
        PageHelper.startPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize());

        Page<TravelGroup> page=travelGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public TravelGroup getById(Integer id) {
        TravelGroup travelGroup=travelGroupDao.getById(id);
        return travelGroup;
    }

    @Override
    public List<Integer> byTravelGroupIdGetTravelItemId(Integer id) {
        return travelGroupDao.byTravelGroupIdGetTravelItemId(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);

        Integer travelGroupId = travelGroup.getId();

        //进行删除操作，删掉中间表数据
        travelGroupDao.delete(travelGroupId);
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);

    }

    @Override
    public void deleteGroup(Integer id) {
        //先删除中间表的数据
        travelGroupDao.delete(id);
        //在删除跟团游表的数据
        travelGroupDao.deleteGroup(id);
    }

    @Override
    public List<TravelGroup> findAll() {

        return travelGroupDao.findAll();
    }


    /**
     * 这个方法的作用是：实现一对多关系
     * 一个跟团游里面有多个自由行
     * @param travelGroupId
     * @param travelItemIds
     */
    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        //进行多次循环来解决这个问题
        for(Integer travelItemId:travelItemIds){
            Map<String,Integer> hashMap=new HashMap();
            hashMap.put("travelGroupId",travelGroupId);
            hashMap.put("travelItemIds",travelItemId);
            travelGroupDao.setTravelGroupAndTravelItem(hashMap);
        }
    }
}
