package Bug;

/**
 * @author lin
 * @create 2022-07-30 16:04
 */
public class redisConnection {

    public void test() {
        System.out.println();
        //连接Redis问题
        /*
         * 要想从这里连接到虚拟机的（Linux环境)下的redis
         * 三个配置(vim redis.conf)在这个配置文件里面
         *
         * 	    1.修改redis.conf(128行)文件将里面的daemonize no 改成 yes，让服务在后台启动
         * 	    2.吧我们的#127.0.0.0那个注释掉（大概在 69行，版本不同行数不同）
         * 	    3.吧4.3.2.protected-mode  no(这里原本是yes)  大概89行
         * 	    4.还有要设置防火墙 (这个在root目录下用) systemctl status firewalld（这个是查看防火墙的，如果有一个地方是绿色的，说明防火墙是开启的,我们要关掉它）
         * 			执行syst emctl stop firewalld  如何在执行systemctl status firewalld查看是否没有绿色
         *
         * 做完以上操作要重启服务器
         *
         * 关闭指令：shutdown（在进入redis里面用）
         * 启动：redis-server   redis.conf
         *
         * 查看指令（也是进入到redis文件里面用）  ps -ef |grep redis
         */
    }
}
