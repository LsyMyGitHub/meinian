package com.atguigu.qiniu;

import com.atguigu.util.QiniuUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.UUID;

/**
 * @author lin
 * @create 2022-07-25 23:38
 * 测试七牛云图片上传和删除
 */
public class QiNiuTest {

    //
    // 上传本地文件
    @Test
    public void uploadFile() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());//华南
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "aEJ9oy1aQtO1q3b_Pgmy4IftTMvhOjrqy5uk-o5K";
        String secretKey = "4YGuSDmAAetPJ0F7dgwjPFlum61K93W22R4dsImO";
        String bucket = "meinianlsy";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
        String localFilePath = "C:\\Users\\length\\Downloads\\length.jpeg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);// FspfyEyKfuHZ0kcbXRIc5T9YhCax
            System.out.println(putRet.hash);// FspfyEyKfuHZ0kcbXRIc5T9YhCax
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }


    }

    @Test
    public void testQiNiu() {
        String accessKey = "aEJ9oy1aQtO1q3b_Pgmy4IftTMvhOjrqy5uk-o5K";
        String secretKey = "4YGuSDmAAetPJ0F7dgwjPFlum61K93W22R4dsImO";
        String bucket = "meinianlsy";
        String key = "file key";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, key);
        System.out.println(upToken);
    }

    // 删除空间中的文件
    @Test
    public void deleteFile() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "aEJ9oy1aQtO1q3b_Pgmy4IftTMvhOjrqy5uk-o5K";
        String secretKey = "4YGuSDmAAetPJ0F7dgwjPFlum61K93W22R4dsImO";
        String bucket = "meinianlsy";
        String key = "Fvc8k78UA2Q4SKmudfozVSExADvk";//文件名称
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 测试更新操作
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {


//        QiniuUtils.deleteFileFromQiniu("com.PNG");

//      String localFilePath = "C:\\Users\\length\\Downloads\\length.jpeg";
//      String filename= UUID.randomUUID().toString()+new Date().toString();
//        System.out.println("new Date().toString()="+new Date().toString());
//        try {
//            QiniuUtils.upload2Qiniu(localFilePath,filename);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

        /**
         * bytes[]字节数组
         * dileNameDle:要删除的文件名
         * fileNameAdd:新加入的文件名
         *
         */


    }
}
