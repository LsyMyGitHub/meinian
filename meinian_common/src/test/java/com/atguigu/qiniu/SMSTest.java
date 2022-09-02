package com.atguigu.qiniu;

import com.atguigu.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lin
 * @create 2022-07-31 11:24
 */
public class SMSTest {
    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com";//固定地址
        String path = "/dx/sendSms";//固定映射地址
        String method = "POST";//必须是post请求
        String appcode = "b535c4801de7414f9c3f3022803b15a7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);  //固定格式，注意有一个空格
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15078015216");
        querys.put("param", "code:250250");
        querys.put("tpl_id", "TP1711063");//测试模板id，也是固定的，需要其他模板，联系客服
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
