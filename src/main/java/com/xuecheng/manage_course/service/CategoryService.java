package com.xuecheng.manage_course.service;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuangjing
 * @create 2020/11/8-14:35
 * @Description:
 */
@Service
public class CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryNode findList() {
        CategoryNode cateGoryList = categoryMapper.findCateGoryList();
        if(cateGoryList==null)
            ExceptionCast.cast(CommonCode.FAIL);
        return cateGoryList;
    }
}
