package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lin
 * @create 2022-08-02 17:02
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;


    @Reference
    private ReportService reportService;

    /**
     * 下载报表
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        //因为这个和之前那个下载模板有所不同，下载模板数据是已经在的了，我们这个要自己写入数据
        //1.获取所有数据
        try {
            Map result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //2.获取模板文件
            //使用绝对路径来获取
            //有域linux系统和windows系统是不一样的,一个是用/一个是用\为了代码的健壮性，我们用File.separator来代替斜杠，他会自动区分
            String filepath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            // 3.工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(filepath);
            //获取第一个工作簿，exl默认有三个工作簿
            XSSFSheet sheet = workbook.getSheetAt(0);
            //4.写数据
            //从第三行开始
            XSSFRow row = sheet.getRow(2);
            //第六列
            XSSFCell cell = row.getCell(5);
            //在第三行，第六列设置值为reportDate
            cell.setCellValue(reportDate);


            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            //从12行开始
            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比

            }
            //5.输出文件，以流形式文件下载，另存为操作
            //因为要在浏览器(客户端显示),所有不可以自己new流
            ServletOutputStream out = response.getOutputStream();

            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            //"content-Disposition"固定的
            //"application/vnd.ms-excel"固定的
            //"attachment;filename=report.xlsx":这个是下载之后要显示的名称
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            workbook.write(out); //写给浏览器，文件下载
            //6.关流
            //6.关闭
            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
        }

    }

    /**
     * 查询会员数量
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            Map map = new HashMap();
            //代表日期的集合
            List<String> months = new ArrayList<>();
            //代表日期对应会员数的集合
            List<Integer> memberCount = new ArrayList<>();
            //查询12个月之前的会员数量
            //先把当前日期按月向前偏移12
            //借助calender这个类
            Calendar calendar = Calendar.getInstance();
            //Calendar.MONDAY获取当前月
            calendar.add(Calendar.MONDAY, -12);
            //现在我们拿到的日期角色2021年8/2，那我们向又偏移回来
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONDAY, 1);
                //获取每次偏移的时间
                Date time = calendar.getTime();
                //格式化，因为我们要的是字符串类型的
                String strDate = sdf.format(time);
                //加入到集合里面去
                months.add(strDate);
            }
            memberCount = memberService.findMemberCountByMonth(months);
            map.put("months", months);
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 饼状图,查询套餐
     * 要返回一个map集合
     * map集合里面有一个list<String>
     * list<Map>
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {

        List<String> setmealNames = new ArrayList<>();
        List<Map> setmealCount = new ArrayList<>();
        setmealCount = setmealService.getSetmealReport();

        for (Map map : setmealCount) {
            String name = String.valueOf(map.get("name"));
            setmealNames.add(name);
        }

        Map map = new HashMap();
        map.put("setmealNames", setmealNames);
        map.put("setmealCount", setmealCount);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, map);
    }

    /**
     * 这个方法是获取报表数据
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
