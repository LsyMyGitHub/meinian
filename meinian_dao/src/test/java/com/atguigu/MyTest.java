package com.atguigu;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lin
 * @create 2022-07-22 20:22
 */
@SpringJUnitConfig(locations = {"classpath:applicationContext-dao.xml"})
public class MyTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void  testData() throws SQLException {
        System.out.println(dataSource);
    }
}
