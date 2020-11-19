package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.ManageCourseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest(classes = ManageCourseApplication.class)
@RunWith(SpringRunner.class)
public class TestRibbon {


    @Autowired
    RestTemplate restTemplate;

    @Test
    public void test1() {
        //服务id
        for(int i=0;i<10;i++) {
            //通过服务id调用
            //XC-SERVICE-MANAGE-CMS
            CmsPage object = restTemplate.getForObject("http://XC-SERVICE-MANAGE-CMS/cms/page/get/5a795ac7dd573c04508f3a56", CmsPage.class);
            System.out.println(object);
        }

    }



}
