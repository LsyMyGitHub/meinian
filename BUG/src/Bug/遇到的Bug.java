package Bug;

/**
 * @author lin
 * @create 2022-07-23 11:22
 */
public class 遇到的Bug {

    public void Bug(){
        System.out.println();
         /*
      1.环境问题：
        1.Dubbo所扫描的包必须要正确，先搞配置文件在创包
        2.提供者必须要引入@service
        3.消费之必须引入@Cotroller注解
        4.这两注解必须是Dubbo里面的
        5.这里只有dao有数据库的配置文件，如果显示密码，用户名错误，就重新构建项目(install)
            是链接数据库问提的去sqlSessionFactoryBean  setDataSource()145行
        5.搞完之后看看去http://localhost:8080/dubbo-admin-2.6.0/看看有什么有消费者和提供者
     */
    }

}
