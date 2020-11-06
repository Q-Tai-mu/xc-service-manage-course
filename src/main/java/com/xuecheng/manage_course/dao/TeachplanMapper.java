package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wuangjing
 * @create 2020/11/6-18:35
 * @Description:
 */
@Mapper
public interface TeachplanMapper  {

    TeachplanNode selectList(String sourceId);
}
