package com.xuecheng.manage_course.controller;


import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author wuangjing
 * @create 2020/11/8-14:38
 * @Description:
 */
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.findList();
    }

    @GetMapping("/testribbon")
    public CmsPage testRibbon(@RequestParam("id") String id){
        //XC‐SERVICE‐MANAGE‐CMS
        return this.restTemplate.getForObject("http://XC-SERVICE-MANAGE-CMS/cms/page/get/"+id, CmsPage.class);
    }
}
