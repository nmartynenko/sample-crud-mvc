package com.aimprosoft.glossary.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
public class BaseTest extends TestCase {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test() throws Exception {
        System.out.println(dataSource.getConnection());
    }

}
