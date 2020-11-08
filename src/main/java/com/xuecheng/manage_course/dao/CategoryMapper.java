package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wuangjing
 * @create 2020/11/8-14:13
 * @Description:
 */
@Mapper
public interface CategoryMapper {

    CategoryNode findCateGoryList();
}
