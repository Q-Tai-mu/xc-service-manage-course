package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuangjing
 * @create 2020/11/6-19:09
 * @Description:
 */
@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if(teachplanNode==null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return teachplanNode;
    }
}
