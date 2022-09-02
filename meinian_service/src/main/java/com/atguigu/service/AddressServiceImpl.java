package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lin
 * @create 2022-08-04 10:46
 */
@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {
        return addressDao.findAllMaps();
    }

    @Override
    public PageResult fingPage(QueryPageBean queryPageBean) {
        //开启分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        //这个是给一个查询条件去查询出数据
        Page<Address> page = addressDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }


}
