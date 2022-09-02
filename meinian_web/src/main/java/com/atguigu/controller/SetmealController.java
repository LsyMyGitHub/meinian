package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.List;
import java.util.UUID;

/**
 * @author lin
 * @create 2022-07-26 10:01
 */
@RestController
//@Controller
//@ResponseBody
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;


    @Autowired
    private JedisPool jedisPool;
    /**
     * 机进行文件上传操作
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {

        try {
            //获取原来文件名
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");//保留文件的后缀名
            String suffix = originalFilename.substring(index);//取出文件后缀名
            //修改原来文件名
            // .（防止重名如何内容覆盖）
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + suffix;
            //进行图片上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);

            //*********************补充代码，解决垃圾图片问题***************

            System.out.println(jedisPool.getResource().toString());
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //*********************************************************
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 添加数据
     */
    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.add(travelgroupIds, setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult result = setmealService.findPage(queryPageBean.getQueryString(),
                queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        return result;
    }

    @RequestMapping("delete")
    public Result delete(@RequestBody Setmeal setmeal) {
        //删除有两步骤
        //第一个先把图片删掉，从七牛云里面
        //第二步：从数据吧删除，根据id
        try {
            String img = setmeal.getImg();
            Integer setmealId = setmeal.getId();
            setmealService.delete(setmealId);
            QiniuUtils.deleteFileFromQiniu(img);
            return  new Result(true,"删除套餐成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"删除套餐失败");
        }


    }

    /**
     *通过id查询套餐
     */
    @RequestMapping("/getById")
    public Result getById(Integer id, HttpSession session){
        try {
            Setmeal setmeal=setmealService.getById(id);

            session.setAttribute("id",id);

            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }

    /**
     * 通过SetmealId (套餐id)
     * 查询GetTravelgroupId （跟团游id）
     */
    @RequestMapping("/bySetmealGetTravelgroupId")
    public Result bySetmealGetTravelgroupId(Integer id){
        try {
            List<Integer> count=setmealService.bySetmealGetTravelgroupId(id);
            return new Result(true,"查询跟团游id成功",count);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询跟团游id失败");
        }

    }

    /*
        执行更行的思路
        一：我们进行图片的更新操作，可是我们无法获得到需要跟新图片的图片名称
        二：我们先进行ajax请求，通过id删除图片，和id对应的数据，如何在执行添加图片操作，完成更新
            第二个方法也不行，这样子的话，在执行更新操作的时候，就把我们新传入的图片删掉了
        三：我又想到了，在我们回显数据的时候，吧id放进请求与里面，如何执行更新图片的时候拿出来

     */


    @RequestMapping("/edit")
    public Result edit(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){
        //有3个步骤
        //1.先删除七牛云的图片
//        String setmealImg = setmeal.getImg();
//        QiniuUtils.deleteFileFromQiniu(setmealImg);

        //2.执行更新操作
        try {
            setmealService.edit(travelgroupIds,setmeal);
            return new Result(true,"编辑套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑套餐失败");
        }
        //3.SetmealId是否在关联表有数据，如果有就先删除关联表的数据(Service层执行),因为我们要重新加入关联表的id
    }
    /**
     * 执行修改图片的操作
     * 调用工具类
     * @param imgFile
     * @return
     */
    @RequestMapping("/updateUpload")
    public Result updateUpload(MultipartFile imgFile,  HttpSession session){

        try {

            //从请求域里面把id拿出来
            Integer id = (Integer) session.getAttribute("id");

            //一旦我们点击修改图片，就执行到这里吧原来的删除
            deleteImg(id);

//            System.out.println(id);
            //获取原来文件名
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");//保留文件的后缀名
            String suffix = originalFilename.substring(index);//取出文件后缀名
            //修改原来文件名
            // .（防止重名如何内容覆盖）
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + suffix;
            //进行图片上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 删除图片
     * 让我们更新图片之前调用这个方法
     */
    @RequestMapping("/deleteImg")
    public Result deleteImg(Integer id){
        //1.先删除七牛云的图片
        try {
            Setmeal setmeal = setmealService.getById(id);
            String setmealImg = setmeal.getImg();

            QiniuUtils.deleteFileFromQiniu(setmealImg);
            return  new Result(true,"删除图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除图片失败");
        }


    }


}
