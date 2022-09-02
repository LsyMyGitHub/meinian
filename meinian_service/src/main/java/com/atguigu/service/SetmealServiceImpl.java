package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-26 10:03
 */
//开启事务要加这个：interfaceClass = SetmealService.class
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealDao setmealDao;


    @Autowired

    private JedisPool jedisPool;
    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {//需要获取主键
        setmealDao.add(setmeal);
        //这个id是套餐游的id
        Integer setmealId = setmeal.getId();

        TravelGroupAndSetMeal(travelgroupIds,setmealId);

        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        //先开启分页功能
        //currentPage：页码
        //pageSize：页数
        //(页码-1)*页数=从那个数开始
        PageHelper.startPage(currentPage,pageSize);

        Page<Setmeal> setmealList=setmealDao.findPage(queryString);

        return new PageResult(setmealList.getTotal(),setmealList.getResult());
    }

    @Override
    public void delete(Integer setmealId) {
        //判断这个id有没有中间表数据
        long count=setmealDao.findSetMealAndTravelGroupId(setmealId);
        if(count>0){
            throw new RuntimeException("删除失败，因为有关联数据，请先解除关联数据,在进行删除");
        }

        setmealDao.delete(setmealId);
    }

    @Override
    public Setmeal getById(Integer id) {
        return setmealDao.getById(id);
    }

    @Override
    public List<Integer> bySetmealGetTravelgroupId(Integer id) {

        return setmealDao.bySetmealGetTravelgroupId(id);
    }

    @Override
    public void edit(Integer[] travelgroupIds, Setmeal setmeal) {
        setmealDao.edit(setmeal);

        Integer setmealId = setmeal.getId();

        //删除中间表的数据stmealIde删除travelgroupIds
        setmealDao.byStmealIdeDeleteTravelgroupIds(setmealId);
        TravelGroupAndSetMeal(travelgroupIds,setmealId);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findAllById(Integer id) {
        return setmealDao.findAllById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    /**
     * 一对多的关系，一个套餐游包含多个跟团游的id
     * @param travelgroupIds
     * @param setmealId
     */
    private void TravelGroupAndSetMeal(Integer[] travelgroupIds, Integer setmealId) {
        for (Integer travelgroupId : travelgroupIds) {
            Map<String,Integer> hashMap=new HashMap();
            hashMap.put("setmealId",setmealId);
            hashMap.put("travelgroupIds",travelgroupId);
            setmealDao.addSetMealAndTravelGroup(hashMap);
        }
    }
}
