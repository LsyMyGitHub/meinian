package Bug;

/**
 * @author lin
 * @create 2022-08-01 10:10
 */
public class MyBatis {
    /**
     * 什么时候返回值类型是resultMap，
     * 涉及到多表联查，且关系是一对多获者是多对一的情况下
     *
     * 如果有多表联查，但是没有一对多或多对一，那就不用用resultMap
     * 说明白点，如果一个表有其他表的主键，且我们就是根据这个主键查一个信息，那就不要resultMap
     */

    /**
     * 如果出现这个异常   Incorrect DATE value:‘一个日期’  (SQl错误1525)
     * 那就是你传的数据和数据库的不能够对应上，比如数据库(年月日)
     * 我传的是年月格式，那就转成字符串,然后拼接一个  "-01"上去
     *
     *  SimpleDateForat sdf = new SimpleDateFormat("yyyy-MM-dd");
     *             Date date = sdf.parse(dateSte);s
     * 如果格式是（年月日，时分秒），那我们就用SimpleDateForat()来指定格式
     *
     *
     *或者在Mybatis哪里用模糊查询
     */
    public static void main(String[] args) {
        System.out.println("有BUG");
    }
}
