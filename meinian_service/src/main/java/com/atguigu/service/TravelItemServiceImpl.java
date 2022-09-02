package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务模块，调用接口模块，和dao
 *
 * @author lin
 * @create 2022-07-22 12:28
 */

//表现层，服务层，dao层

//这个是服务层，需要调用dao层的方法

@Service
public class TravelItemServiceImpl implements TravelItemService {


    @Autowired
    private TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
      /*
        开启分页功能
        传两个参数的话，第一个是开始索引，每页页数
         */

//        Integer currentPage = queryPageBean.getCurrentPage();//页码
//        Integer pageSize = queryPageBean.getPageSize();     //页数
//        String queryString = queryPageBean.getQueryString();//查询条件

        // 分页插件的代码必须写到第一行
        //limit(currentPage-1)*pageSize,pageSize
        PageHelper.startPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize());

        //上面那句是分页操作，下面这个剧是查询操作
        Page<TravelItem> page =
                travelItemDao.findPage(queryPageBean.getQueryString());


        return new PageResult(page.getTotal(), page.getResult());//总记录数，分页数集合
    }

    @Override
    public void delete(Integer id) {
        //先判断跟团游里面有没有只由行在进行删除

        long count =travelItemDao. travelItemAndTravelGroup(id);

        if(count>0){
            throw new RuntimeException("删除只有行失败，因为有绑定关系，请先解除绑定关系在进行删除");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem getById(Integer id) {
        return travelItemDao.getById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

    //    @Autowired
//    private TravelItemDao travelItemDao;


}
